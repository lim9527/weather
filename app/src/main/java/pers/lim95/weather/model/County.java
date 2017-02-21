package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public class County implements ICountyModel{
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Override
    public String getCountyName() {
        return this.countyName;
    }

    @Override
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    @Override
    public String getCountyCode() {
        return this.countyCode;
    }

    @Override
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public int getCityId() {
        return this.cityId;
    }
}
