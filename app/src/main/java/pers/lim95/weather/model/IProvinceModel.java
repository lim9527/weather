package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public interface IProvinceModel {
    void setId(int id);
    int getId();

    void setProvinceName(String provinceName);
    String getProvinceName();

    void setProvinceCode(String provinceCode);
    String getProvinceCode();
}
