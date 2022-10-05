package com.example.qlhcsinh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlhcsinh.Adapter.AdapterDiemHS;
import com.example.qlhcsinh.Object.DiemHS;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_DiemHocTap extends AppCompatActivity {
    public static RecyclerView Recly_BangDiem;
    TextView BD_HoTen, BD_MSHS, BD_ChucVu;
    public static List<DiemHS> mDiemHS;
    public static AdapterDiemHS adapter;
    public static int Key_ID = 0;
    int Key_MSHS = 0;
    String Key_Ten = "", Key_ChucVu = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_diem_hoc_tap);
        Anhxa();
        Intent intent = getIntent();
        Key_ID = intent.getIntExtra("Key_ID", 0);
        Key_MSHS = intent.getIntExtra("Key_MSHS", 0);
        Key_Ten = intent.getStringExtra("Key_Ten");
        Key_ChucVu = intent.getStringExtra("Key_ChucVu");
        SetInfo(Key_Ten, Key_MSHS, Key_ChucVu);
        adapter = new AdapterDiemHS(MainActivity_Info_HocSinh.userLogin.ismGV_PH(), this, Key_Ten);

        //Set RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//dạng list
        Recly_BangDiem.setLayoutManager(layoutManager);
        Recly_BangDiem.setHasFixedSize(true);
        Recly_BangDiem.setItemViewCacheSize(14);
        //Get bảng điểm
        getBangDiem(Key_ID);
    }
    private void Anhxa(){
        Recly_BangDiem = findViewById(R.id.Recly_BangDiem);
        BD_HoTen = findViewById(R.id.BD_HoTen);
        BD_MSHS = findViewById(R.id.BD_MSHS);
        BD_ChucVu = findViewById(R.id.BD_ChucVu);
    }

    private void SetInfo(String Ten, int MSHS, String ChucVu){
        BD_HoTen.setText("Họ Tên: " + Ten);
        BD_MSHS.setText("MSHS: " + MSHS);
        BD_ChucVu.setText("Chức Vụ: " + ChucVu);
    }

    //TODO Get Bảng điểm
    public static void getBangDiem(int ID){
        Call<List<DiemHS>> callBack = MainActivity_Info_HocSinh.dataClient.GetBangDiem(ID);
        callBack.enqueue(new Callback<List<DiemHS>>() {
            @Override
            public void onResponse(Call<List<DiemHS>> call, Response<List<DiemHS>> response) {
                if (response != null){
                    mDiemHS = response.body();
                    mDiemHS.add(0, new DiemHS(0, "Tên Môn Học", 0, 0, 0, 0));
                    adapter.setmDiemHS(mDiemHS);
                    Collections.sort(mDiemHS, DiemHS.AZ_DiemHS);
                    Recly_BangDiem.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<DiemHS>> call, Throwable t) {
            }
        });
    }
}