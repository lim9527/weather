package pers.lim95.weather.model;

/**
 * Created by lim9527 on 2/21 0021.
 */

public class Province implements IProvinceModel{
    private int id;
    private String provinceName;
    private String provinceCode;

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String getProvinceName() {
        return this.provinceName;
    }

    @Override
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String getProvinceCode() {
        return this.provinceCode;
    }
}
