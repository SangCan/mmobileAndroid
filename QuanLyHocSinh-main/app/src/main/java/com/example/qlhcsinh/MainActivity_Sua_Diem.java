package com.example.qlhcsinh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_Sua_Diem extends AppCompatActivity {
    int Key_ID = 0;
    EditText SuaD_Mieng, SuaD_15p, SuaD_1Tiet, SuaD_HocKy;
    TextView SuaD_Ten, SuaD_Mon;
    String Key_Ten = "", Key_Mon = "";
    double D_Mieng = 0, D_15p = 0, D_1Tiet = 0, D_HocKy = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sua_diem);
        Anhxa();
        //set các giá trị vào nhận đc từ intent trk vào layout
        GetIntent();

    }

    private void Anhxa() {
        SuaD_Mieng = findViewById(R.id.SuaD_Mieng);
        SuaD_15p = findViewById(R.id.SuaD_15p);
        SuaD_1Tiet = findViewById(R.id.SuaD_1Tiet);
        SuaD_HocKy = findViewById(R.id.SuaD_HocKy);
        SuaD_Ten = findViewById(R.id.SuaD_Ten);
        SuaD_Mon = findViewById(R.id.SuaD_Mon);
    }

    //TODO getIntent
    private void GetIntent(){
        Intent intent = getIntent();
        //set Text
        Key_ID = intent.getIntExtra("Key_ID", 0);
        Key_Ten = intent.getStringExtra("Key_Ten");
        Key_Mon = intent.getStringExtra("Key_Mon");
        SuaD_Ten.setText(Key_Ten);
        SuaD_Mon.setText(Key_Mon);
        //set điểm
        D_Mieng = intent.getDoubleExtra("Key_DMieng", 0);
        D_15p = intent.getDoubleExtra("Key_D15p", 0);
        D_1Tiet = intent.getDoubleExtra("Key_D1Tiet", 0);
        D_HocKy = intent.getDoubleExtra("Key_DHocKy", 0);
        SuaD_Mieng.setText(DinhDang(D_Mieng));
        SuaD_15p.setText(DinhDang(D_15p));
        SuaD_1Tiet.setText(DinhDang(D_1Tiet));
        SuaD_HocKy.setText(DinhDang(D_HocKy));
    }

    public void onClickSetDiem (View view){
        switch (view.getId()){
            case R.id.SuaD_Huy:
                finish();
                break;
            case R.id.SuaD_OK:
                SetDiem();
                break;
        }
    }

    //TODO Set điểm
    private void SetDiem(){
        double DiemMieng = SetChuoiNhapVao(SuaD_Mieng);
        double Diem15p = SetChuoiNhapVao(SuaD_15p);
        double Diem1Tiet = SetChuoiNhapVao(SuaD_1Tiet);
        double DiemHocKy = SetChuoiNhapVao(SuaD_HocKy);
        Call<String> callBack = MainActivity_Info_HocSinh.dataClient.SetDiem(Key_ID, DiemMieng, Diem15p, Diem1Tiet, DiemHocKy);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        finish();
                        MainActivity_DiemHocTap.getBangDiem(MainActivity_DiemHocTap.Key_ID);
                    }else
                        Toast.makeText(MainActivity_Sua_Diem.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_Sua_Diem.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //định dạng lại chuổi nhập vào thành double
    private double SetChuoiNhapVao(TextView textView){
        double result = 0;
        String s = textView.getText().toString().trim();
        if (s.length() > 0){
            result = Double.parseDouble(s);
        }
        return result;
    }

    //TODO định dạng text gán vào EDT
    private String DinhDang(double Diem){
        String s = "";
        if (Diem > 0){
            s = String.valueOf(Diem);
        }
        return s;
    }
}