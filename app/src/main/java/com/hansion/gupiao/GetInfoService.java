package com.hansion.gupiao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.hansion.gupiao.retrofit.MyRetrofit;
import com.hansion.gupiao.utils.NetworkUtils;
import com.hansion.gupiao.utils.SharedPrefsUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/9/14 14:26
 */
public class GetInfoService extends Service {

    private Notification notification;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isWorkDay() && isChangeTime() && NetworkUtils.isWifi(GetInfoService.this)) {
                        getInfo();
                    } else {
                        mNotificationManager.cancel(1);
                    }
                    handler.sendEmptyMessageDelayed(0, 1000);
                    break;
            }
            return false;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new NotificationCompat.Builder(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        handler.sendEmptyMessageDelayed(0, 0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isWorkDay() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        int i = Integer.parseInt(mWay);

        if (i >= 2 && i <= 6) { //周一到周五
            return true;
        }
        return false;
    }

    private boolean isChangeTime() {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数

        int startAM = 9 * 60 + 15;  //9:15开始
        int endAM = 11 * 60 + 30;   //11:30结束
        int startPM = 13 * 60;  //13:00开始
        int endPM = 15 * 60;   //15:00结束

        if ((minuteOfDay >= startAM && minuteOfDay <= endAM) || (minuteOfDay >= startPM && minuteOfDay <= endPM)) {
            return true;
        }
        return false;
    }

    private void getInfo() {
        String value = SharedPrefsUtil.getValue(this, "num", "num", "");
        if(TextUtils.isEmpty(value)) {
            return;
        }
        MyRetrofit.getInstance().getInfo(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    //var hq_str_s_sz002441="众业达,13.05,-0.20,-1.51,138842,18170";
                    String string = responseBody.string();
                    String[] split = string.split("\"");
                    String[] split1 = split[1].split(",");
                    String msg;
                    if (string.contains("s_sz")) {
                        msg = split1[0] + " " + split1[1];
                    } else {
                        msg = split1[0] + " " + split1[3];
                    }
                    notification = builder
                            .setContentTitle(msg)
                            .setContentText("")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("This is a notification")
                            .setLargeIcon(BitmapFactory.decodeResource(
                                    getResources(), R.mipmap.ic_launcher))
                            .build();
                    mNotificationManager.notify(1, notification);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, value);
    }
}
