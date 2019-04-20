package com.example.henry.marathon.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.henry.marathon.R;

public class detial extends AppCompatActivity {
    private TextView name_text;
    private TextView decribe_text;
    private TextView date_text;
    private TextView person_name_text;
    private TextView person_tel_text;
    private String name,decribe,person_name,person_tel;
    private int date;
    private LatLng point;
    private double latitude;
    private double longitude;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bitmapDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_detial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorTextOrIcons),PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        decribe = intent.getStringExtra("describe");
        person_name = intent.getStringExtra("per_name");
        person_tel = intent.getStringExtra("tel");
        latitude = intent.getDoubleExtra("latitude",39.963175);
        longitude = intent.getDoubleExtra("longitude",116.400244);
        point = new LatLng(latitude,longitude);
        date = intent.getIntExtra("date",0);
        initView();
        name_text.setText(name);
        decribe_text.setText(decribe);
        person_name_text.setText(person_name);
        person_tel_text.setText(person_tel);
        bitmapDes = BitmapDescriptorFactory.fromResource(R.drawable.location_tag);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmapDes);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomBy(16f);
        mBaiduMap.animateMapStatus(update);
        int year = date/10000;
        int month = date%10000/100;
        int day = date%100;
        date_text.setText(String.valueOf(year)+"/"+month+"/"+day);
    }
    //控件初始化
    private void initView(){
        name_text = findViewById(R.id.obj_name_detial);
        decribe_text = findViewById(R.id.obj_describe_detial);
        date_text = findViewById(R.id.date_detial);
        person_name_text = findViewById(R.id.person_name_detial);
        person_tel_text = findViewById(R.id.person_tel_detial);
        mMapView = (MapView) findViewById(R.id.dbmap2);
        mBaiduMap = mMapView.getMap();
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }
}
