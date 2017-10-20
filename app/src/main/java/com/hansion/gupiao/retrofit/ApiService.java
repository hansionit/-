package com.hansion.gupiao.retrofit;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/9/14 14:28
 */
public interface ApiService {

    //获取当前模式    http://hq.sinajs.cn/&list=s_sz002441
    @GET("/")
    Observable<ResponseBody> getInfo(@Query("list") String number);
}
