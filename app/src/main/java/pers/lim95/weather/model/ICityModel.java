package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public interface ICityModel {
    void setId(int id);
    int getId();

    void setCityName(String cityName);
    String getCityName();

    void setCityCode(String cityCode);
    String getCityCode();

    void setProvinceId(int provinceId);
    int getProvinceId();
}
