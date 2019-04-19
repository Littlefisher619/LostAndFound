package com.example.henry.marathon.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henry.marathon.R;
import com.example.henry.marathon.javabean.FoundObj;
import com.example.henry.marathon.javabean.HttpState;
import com.example.henry.marathon.javabean.Obj;
import com.example.henry.marathon.utils.HttpUtil;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class form extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private int date = 0;
    private double latitude = 0;
    private double longitude = 0;
    private static final String TAG = "SENDIMAGE";
    private View name_write;
    private Gson gson = new Gson();
    private View describe_write;
    private View location_write;
    private View date_write;
    private View person_name_write;
    private View person_tel_write;
    private View location_describe;
    private TextView cancel;
    private TextView makesure;
    private TextView nameWriteIn;
    private TextView describeWriteIn;
    private TextView locationWriteIn;
    private TextView LocationDescribeIn;
    private TextView dateWriteIn;
    private TextView personNameWriteIn;
    private TextView personTelWriteIn;
    private String name = "";
    private String describe = "";
    private String personName = "";
    private String personTel = "";
    private String locationdescribe_string = "";
    private DatePickerDialog dpd;
    private String do_what;
    private File outputImage;
    private Uri imageUri;
    private static final int GET_NAME = 1;
    private static final int GET_DESCRIBE =2;
    private static final int GET_PERSON_NAME = 3;
    private static final int GET_TEL =4;
    private static final int GET_LOCATION = 5;
    private static final int GET_LOCATION_DESCRIBE =6;
    private static final int TAKE_PHOTO = 7;
    private static final int CHOOSE_PHOTO = 8;
    private Button choosePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Intent intent_dowhat = getIntent();
        do_what = intent_dowhat.getStringExtra("class");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        initView();

    }
    /** 时间选择器 **/
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month;
        String day;
        String dateTemp;
        if (monthOfYear<9){
            month = "0"+(++monthOfYear);
        }else {
            month = String.valueOf(monthOfYear);
        }
        if (dayOfMonth<10){
            day = "0" +dayOfMonth;
        }else {
            day = String.valueOf(dayOfMonth);
        }
        dateTemp = String.valueOf(year)+month+day;
        date = Integer.valueOf(dateTemp);
        Log.d("输出的时间", "onDateSet: "+date);
        dateWriteIn.setText(String.valueOf(date));
    }
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    private void openCapture(){
        Log.d(TAG, "openCapture: "+"打开相机");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    private void initView(){
        choosePhoto = findViewById(R.id.school_card);
        makesure = findViewById(R.id.make_sure);
        cancel = findViewById(R.id.cancel);
        name_write = findViewById(R.id.name_write);
        describe_write = findViewById(R.id.describe_write);
        location_write = findViewById(R.id.location_write);
        location_describe = findViewById(R.id.location_describe);
        date_write = findViewById(R.id.date_write);
        person_name_write = findViewById(R.id.person_name_write);
        LocationDescribeIn = findViewById(R.id.location_describeIn);
        person_tel_write = findViewById(R.id.person_tel_write);
        nameWriteIn = findViewById(R.id.name_writeIn);
        describeWriteIn = findViewById(R.id.describe_writeIn);
        locationWriteIn = findViewById(R.id.location_writeIn);
        dateWriteIn = findViewById(R.id.date_writeIn);
        personNameWriteIn = findViewById(R.id.person_name_writeIn);
        personTelWriteIn = findViewById(R.id.person_tel_writeIn);
        location_describe.setOnClickListener(this);
        makesure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        describe_write.setOnClickListener(this);
        name_write.setOnClickListener(this);
        location_write.setOnClickListener(this);
        date_write.setOnClickListener(this);
        person_name_write.setOnClickListener(this);
        person_tel_write.setOnClickListener(this);


        /* todo   there is  a  bug here
        choosePhoto.setOnClickListener(this);
        */


    }
    @Override
    public void onClick(View v) {
        Intent intentforedit;
        switch (v.getId()){
            case R.id.name_write:
                intentforedit = new Intent(form.this,EditActivity.class);
                intentforedit.putExtra("extra_data","请输入物品的名字：");
                intentforedit.putExtra("content",name);
                startActivityForResult(intentforedit,GET_NAME);
                break;
            case R.id.describe_write:
                intentforedit = new Intent(form.this,EditActivity.class);
                intentforedit.putExtra("extra_data","请输入物品描述：");
                intentforedit.putExtra("content",describe);
                startActivityForResult(intentforedit,GET_DESCRIBE);
                break;
            case R.id.location_write:
                intentforedit = new Intent(form.this,MapActivity.class);
                startActivityForResult(intentforedit,GET_LOCATION);
                break;
            case R.id.date_write:
                Calendar now = Calendar.getInstance();
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            form.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            form.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.setMaxDate(now);
                dpd.setOkColor(Color.parseColor("#FFFFFF"));
                dpd.show(getFragmentManager(),"Datepickerdialog");
                break;
            case R.id.person_name_write:
                intentforedit = new Intent(form.this,EditActivity.class);
                intentforedit.putExtra("extra_data","请输入您的姓名：");
                intentforedit.putExtra("content",personName);
                startActivityForResult(intentforedit,GET_PERSON_NAME);
                break;
            case R.id.person_tel_write:
                intentforedit = new Intent(form.this,EditActivity.class);
                intentforedit.putExtra("extra_data","请输入您的手机号码：");
                intentforedit.putExtra("content",personTel);
                startActivityForResult(intentforedit,GET_TEL);
                break;
            case R.id.location_describe:
                intentforedit = new Intent(form.this,EditActivity.class);
                intentforedit.putExtra("extra_data","请输入地点描述：");
                intentforedit.putExtra("content",locationdescribe_string);
                startActivityForResult(intentforedit,GET_LOCATION_DESCRIBE);
                break;
            case R.id.school_card:
                outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                        Log.d(TAG, "onClick: DELETE");
                    }
                    outputImage.createNewFile();
                    Log.d(TAG, "onClick: NEWFILE");
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(form.this,"com.example.henry.marathon.fileprovider",outputImage);
                    Log.d(TAG, "onClick: FileProvider.getUriForFile:"+imageUri);
                }else {
                    imageUri = Uri.fromFile(outputImage);
                    Log.d(TAG, "onClick: Uri.fromFile:"+imageUri);
                }
                checkPermission();
                upImage();
                break;
            case R.id.make_sure:
                if (name.equals("")){
                    Toast.makeText(form.this,"请输入物品名",Toast.LENGTH_SHORT).show();
                }else if (describe.equals("")){
                    Toast.makeText(form.this,"请输入物品描述",Toast.LENGTH_SHORT).show();
                }else if (locationWriteIn.getText().toString().equals("请输入")){
                    Toast.makeText(form.this,"请选择地点",Toast.LENGTH_SHORT).show();
                }else if (date == 0){
                    Toast.makeText(form.this,"请选择时间",Toast.LENGTH_SHORT).show();
                }else if (personName.equals("")){
                    Toast.makeText(form.this,"请输入姓名",Toast.LENGTH_SHORT).show();
                }else if (personTel.equals("")){
                    Toast.makeText(form.this,"请输入电话号码",Toast.LENGTH_SHORT).show();
                }else {
                    Obj obj = new Obj();
                    obj.setDate(BigInteger.valueOf(date));
                    obj.setName(name);
                    obj.setDescribe(describe);
                    obj.setLocationdesc(locationdescribe_string);
                    obj.setLatlngx(latitude);
                    obj.setLatlngy(longitude);
                    obj.setPerson_name(personName);
                    obj.setPerson_tel(personTel);
                    String json = gson.toJson(obj);
                    Log.d("POSTJSON", "onClick: "+json);
                    HttpUtil httpUtil = new HttpUtil();
                    String address = "http://47.107.171.219:5000/"+do_what+"/publish";
                    Log.d("ADDRESSIS_", "onClick: "+address);
                    httpUtil.HttpPost(address,json, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(form.this,"连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String data = response.body().string();
                            HttpState httpState = gson.fromJson(data,HttpState.class);
                            final String status = httpState.getStatus();
                            Log.d("HTTPSYATUS", "onResponse: "+response.code());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(form.this,status,Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        }
                    });
                    Intent intentback = new Intent();
                    intentback.putExtra("obj_name",name);
                    setResult(RESULT_OK,intentback);
                    finish();
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
        private void checkPermission() {
            if (ContextCompat.checkSelfPermission(form.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 进入这儿表示没有权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(form.this, Manifest.permission.CAMERA)) {
                    // 提示已经禁止
                    Toast.makeText(form.this,"没有权限",Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(form.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
            } else {
                openCapture();
            }
        }

    private void upImage() {
        Log.d(TAG, "upImage: ");
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(getExternalCacheDir(),"output_image.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "output_image.jpg",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url("http://47.107.171.219:5000/ocr")
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"上传图片失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(form.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "成功"+response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(form.this, "成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (data!=null){
                String returnedData = data.getStringExtra("data_return");
                switch (requestCode){
                    case GET_NAME:
                        nameWriteIn.setText(returnedData);
                        name = returnedData;
                        break;
                    case GET_DESCRIBE:
                        describeWriteIn.setText(returnedData);
                        describe = returnedData;
                        break;
                    case GET_PERSON_NAME:
                        personNameWriteIn.setText(returnedData);
                        personName = returnedData;
                        break;
                    case GET_TEL:
                        personTelWriteIn.setText(returnedData);
                        personTel = returnedData;
                        break;
                    case GET_LOCATION:
                        latitude=data.getDoubleExtra("latitude",0.000000000000000);
                        longitude=data.getDoubleExtra("longitude",0.000000000000000);
                        locationWriteIn.setText("已选地点");
                        break;
                    case GET_LOCATION_DESCRIBE:
                        LocationDescribeIn.setText(returnedData);
                        locationdescribe_string = returnedData;
                        break;
                    case TAKE_PHOTO:
                        Log.d(TAG, "onActivityResult: "+"回调");

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
