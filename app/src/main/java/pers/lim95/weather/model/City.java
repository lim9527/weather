package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public class City implements ICityModel{
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String getCityName() {
        return this.cityName;
    }

    @Override
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String getCityCode() {
        return this.cityCode;
    }

    @Override
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public int getProvinceId() {
        return this.provinceId;
    }
}
