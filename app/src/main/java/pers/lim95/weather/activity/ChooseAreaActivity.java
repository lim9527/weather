package pers.lim95.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pers.lim95.weather.R;
import pers.lim95.weather.db.WeatherDB;
import pers.lim95.weather.model.City;
import pers.lim95.weather.model.County;
import pers.lim95.weather.model.Province;
import pers.lim95.weather.util.IHttpCallbackListener;
import pers.lim95.weather.util.HttpUtil;
import pers.lim95.weather.util.Utility;

public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView mTitleText;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private WeatherDB mWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;
    private Province selectedProvince;
    private City selectedCity;
    //private County selectedCounty;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_area);

        mListView = (ListView) findViewById(R.id.list_view);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);
        mWeatherDB = WeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = mProvinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = mCityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    /**
     *查询全国所有的省，优先到数据库查询，如果没有再到服务器上查询。
     */
    private void queryProvinces(){
        mProvinceList = mWeatherDB.loadProvinces();
        if (mProvinceList.size() > 0){
            dataList.clear();
            for (Province province:mProvinceList){
                dataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null, "province");
        }
    }

    /**
     *查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities(){
        mCityList = mWeatherDB.loadCities(selectedProvince.getId());
        if (mCityList.size() > 0){
            dataList.clear();
            for (City city:mCityList){
                dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     *查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties(){
        mCountyList = mWeatherDB.loadCounties(selectedCity.getId());
        if (mCountyList.size() > 0){
            dataList.clear();
            for (County county:mCountyList){
                dataList.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(final String code, final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }

        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new IHttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvincesResponse(mWeatherDB, response);
                } else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(mWeatherDB,
                            response, selectedProvince.getId());
                } else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(mWeatherDB,
                            response, selectedCity.getId());
                }

                if (result){
                    //通过runOnUiThread()方法回到主线程处理逻辑。
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    /**
     * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed(){
        if (currentLevel == LEVEL_COUNTY){
            queryCities();
        } else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            finish();
        }
    }




}
