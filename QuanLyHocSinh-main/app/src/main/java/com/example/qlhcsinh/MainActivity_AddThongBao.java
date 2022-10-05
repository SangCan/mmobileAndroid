package com.example.qlhcsinh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlhcsinh.Fragment.FragmentThongBao;
import com.example.qlhcsinh.Object.ThongBao;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_AddThongBao extends AppCompatActivity {
    TextView Ngay_TB, Nguon_TB;
    EditText Ten_TB, NoiDung_TB;
    String Key_SetTB = "", NgayDang = "";
    int Key_MSL = 0;

    ThongBao mThongBao = null;
    //Check đây là id của thông báo nếu sửa
    int Check = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_thong_bao);
        Anhxa();

        //TODO getIntent
        Intent intent = getIntent();
        Key_MSL = intent.getIntExtra("Key_MSL", 0);
        Key_SetTB = intent.getStringExtra("Key_SetTB");
        Nguon_TB.setText("Thông báo từ " + Key_SetTB);
        //get thông báo
        mThongBao = (ThongBao) intent.getSerializableExtra("Key_ThongBao");

        if (mThongBao != null){
            Ngay_TB.setText(mThongBao.getmNgayDang());
            Nguon_TB.setText(mThongBao.getmGV_NhaTruong());
            Ten_TB.setText(mThongBao.getmTen());
            NoiDung_TB.setText(mThongBao.getmNoiDung());
            Check = mThongBao.getmID();
        }else {
            //set ngày lên layout
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("E, hh:mm, dd/MM/yyyy");
            NgayDang = formatter.format(date);
            Ngay_TB.setText(NgayDang);
        }


    }
    private void Anhxa(){
        Ngay_TB = findViewById(R.id.Ngay_TB);
        Ten_TB = findViewById(R.id.Ten_TB);
        NoiDung_TB = findViewById(R.id.NoiDung_TB);
        Nguon_TB = findViewById(R.id.Nguon_TB);
    }

    //TODO onclick btn
    public void onClickAddTB(View view){
        switch (view.getId()){
            case R.id.Huy_TB:
                finish();
                break;
            case R.id.OK_TB:
                String TenTB = Ten_TB.getText().toString().trim();
                String NoiDungTB = NoiDung_TB.getText().toString().trim();
                if (TenTB.length() > 0 && NoiDungTB.length() > 0){
                    if (mThongBao == null){
                        AddThongBao(Key_MSL, Key_SetTB, TenTB, NoiDungTB, NgayDang, Check);
                    }else {
                        AddThongBao(-1, "", TenTB, NoiDungTB, "", Check);
                    }
                }
                break;
        }
    }

    //TODO add thông báo
    private void AddThongBao(int MSL, String NguonTB, String TenTB, String NoiDungTB, String NgayDang, int ID){
        Call<String> callBack = MainActivity_Info_HocSinh.dataClient.AddThongBao(MSL,
                                                                                    NguonTB,
                                                                                    TenTB,
                                                                                    NoiDungTB,
                                                                                    NgayDang,
                                                                                    ID);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Toast.makeText(MainActivity_AddThongBao.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                        if (Check > 0){
                            FragmentThongBao.GetTB(MainActivity_Info_HocSinh.userLogin.getmMSL());
                        }

                    }else
                        Toast.makeText(MainActivity_AddThongBao.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_AddThongBao.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}