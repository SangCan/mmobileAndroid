package com.example.qlhcsinh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlhcsinh.Fragment.FragmentTKB;
import com.example.qlhcsinh.Object.TKB;
import com.example.qlhcsinh.Retrofit.DataClient;
import com.example.qlhcsinh.Retrofit.UtilsAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_setTKB extends AppCompatActivity {
    TextView SetTKB_Thu;
    EditText Set_TKB_S1, Set_TKB_S2, Set_TKB_S3, Set_TKB_S4, Set_TKB_S5, Set_TKB_C1, Set_TKB_C2, Set_TKB_C3, Set_TKB_C4, Set_TKB_C5;
    int MSL = MainActivity_Info_HocSinh.userLogin.getmMSL();
    TKB Key_TKB = null;
    DataClient dataClient = UtilsAPI.getData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_set_tkb);
        Anhxa();
        Intent intent = getIntent();
        Key_TKB = (TKB) intent.getSerializableExtra("Key_TKB");
        if (Key_TKB != null){
            GetTKBCu(Key_TKB);
        }
    }
    private void Anhxa(){
        SetTKB_Thu = findViewById(R.id.SetTKB_Thu);
        Set_TKB_S1 = findViewById(R.id.Set_TKB_S1);
        Set_TKB_S2 = findViewById(R.id.Set_TKB_S2);
        Set_TKB_S3 = findViewById(R.id.Set_TKB_S3);
        Set_TKB_S4 = findViewById(R.id.Set_TKB_S4);
        Set_TKB_S5 = findViewById(R.id.Set_TKB_S5);
        Set_TKB_C1 = findViewById(R.id.Set_TKB_C1);
        Set_TKB_C2 = findViewById(R.id.Set_TKB_C2);
        Set_TKB_C3 = findViewById(R.id.Set_TKB_C3);
        Set_TKB_C4 = findViewById(R.id.Set_TKB_C4);
        Set_TKB_C5 = findViewById(R.id.Set_TKB_C5);
    }
    //TODO Lấy TKB Nhập Từ Layout
    private TKB getLayoutTKB(){
        String Sang1 = Set_TKB_S1.getText().toString().trim();
        String Sang2 = Set_TKB_S2.getText().toString().trim();
        String Sang3 = Set_TKB_S3.getText().toString().trim();
        String Sang4 = Set_TKB_S4.getText().toString().trim();
        String Sang5 = Set_TKB_S5.getText().toString().trim();
        String Chieu1 = Set_TKB_C1.getText().toString().trim();
        String Chieu2 = Set_TKB_C2.getText().toString().trim();
        String Chieu3 = Set_TKB_C3.getText().toString().trim();
        String Chieu4 = Set_TKB_C4.getText().toString().trim();
        String Chieu5 = Set_TKB_C5.getText().toString().trim();
        TKB tkb = new TKB(MSL, Key_TKB.getmThu(), Sang1, Sang2, Sang3, Sang4, Sang5, Chieu1, Chieu2, Chieu3, Chieu4, Chieu5);
        return tkb;
    }

    //TODO Gán TKB củ vào layout
    private void GetTKBCu(TKB tkb){
        SetTKB_Thu.setText("Thứ " + tkb.getmThu());
        Set_TKB_S1.setText(tkb.getmSang1());
        Set_TKB_S2.setText(tkb.getmSang2());
        Set_TKB_S3.setText(tkb.getmSang3());
        Set_TKB_S4.setText(tkb.getmSang4());
        Set_TKB_S5.setText(tkb.getmSang5());
        Set_TKB_C1.setText(tkb.getmChieu1());
        Set_TKB_C2.setText(tkb.getmChieu2());
        Set_TKB_C3.setText(tkb.getmChieu3());
        Set_TKB_C4.setText(tkb.getmChieu4());
        Set_TKB_C5.setText(tkb.getmChieu5());
    }

    //TODO Bắt sự kiện ok
    public void Set_TKB_OK(View view){
        if (view.getId() == R.id.Set_TKB_OK){
            TKB tkb = getLayoutTKB();
            UpSetTKB(tkb);
        }
    }

    //TODO Up/Sửa TKB Lên Sever
    private void UpSetTKB(TKB tkb){
        Call<String> callBack = dataClient.UpSetTKB(MSL, Key_TKB.getmThu(), tkb.getmSang1(), tkb.getmSang2(), tkb.getmSang3(), tkb.getmSang4(), tkb.getmSang5(), tkb.getmChieu1(), tkb.getmChieu2(), tkb.getmChieu3(), tkb.getmChieu4(), tkb.getmChieu5(), Key_TKB.getmID());
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Toast.makeText(MainActivity_setTKB.this, "Đã Lưu!", Toast.LENGTH_SHORT).show();
                        FragmentTKB.GetTKB();
                        finish();
                    }else Log.d("BBB-Check-UpSetTKB", response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_setTKB.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}