package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public interface ICountyModel {
    void setId(int id);
    int getId();

    void setCountyName(String countyName);
    String getCountyName();

    void setCountyCode(String countyCode);
    String getCountyCode();

    void setCityId(int cityId);
    int getCityId();
}
