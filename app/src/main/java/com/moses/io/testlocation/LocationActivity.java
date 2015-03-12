package com.moses.io.testlocation;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.moses.io.R;

import java.util.List;

/**
 * Created by Moses on 2015
 */
public class LocationActivity extends Activity {
    private TextView locationTxt;
    private LocationManager mLocationManager;
    String mProvider;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    protected void initView() {
        locationTxt = (TextView) findViewById(R.id.location_text_view);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providerList = mLocationManager.getAllProviders();
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            mProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //当前没有可用的位置提供器时，弹出Toast提示用户。
            Toast.makeText(this, "no location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = mLocationManager.getLastKnownLocation(mProvider);
        Log.e("TAG", "location>>>>>>>>>>>>>" + location);
        if (location != null) {
            //显示当前设备的位置信息
            showLocation(location);
        }

        mLocationManager.requestLocationUpdates(mProvider, 5000, 1, locationListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            //关闭程序是将监听器移除
            mLocationManager.removeUpdates(locationListener);
        }
    }

    private void showLocation(Location location) {
        String currentPosition = "Latitude is : " + location.getLatitude() + "\n" +
                "Longitude is : " + location.getLongitude();
        Log.e("TAG", "currentPosition>>>>>>>>>>>>>" + currentPosition);
        locationTxt.setText(currentPosition);
    }

}
