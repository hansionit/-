package com.hansion.gupiao.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Description：Retrofit封装类
 * Author: Hansion
 * Time: 2016/9/20
 */
public class MyRetrofit {

    private static final int DEFAULT_TIMEOUT = 3;
    private Retrofit retrofit;
    private ApiService apiService;

    //构造方法私有
    private MyRetrofit() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://hq.sinajs.cn/")
//                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final MyRetrofit INSTANCE = new MyRetrofit();
    }

    //获取单例
    public static MyRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getInfo(Subscriber<ResponseBody> subscriber, String number) {
        String num;
        if(number.startsWith("0")) {
            num = "s_sz"+number;
        } else {
            num = "sh"+number;
        }
        toSubscribe(apiService.getInfo(num), subscriber);
    }

}
