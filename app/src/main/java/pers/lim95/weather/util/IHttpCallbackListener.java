package pers.lim95.weather.util;

/**
 * Created by lim9527 on 2/21 0021.
 */

public interface IHttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
