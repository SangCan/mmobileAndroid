package com.example.qlhcsinh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlhcsinh.Object.User;
import com.example.qlhcsinh.Retrofit.DataClient;
import com.example.qlhcsinh.Retrofit.UtilsAPI;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity_Tao_TK extends AppCompatActivity {
    boolean GVPH;
    TextView MaLop, titleTaoTK;
    EditText TKTTK, MKTTK, MLTTK, MTKTTK;
    String urlAddData = "http://172.16.1.66/QLHS/TaoTaiKhoan.php";
    DataClient dataClient = UtilsAPI.getData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__tao__t_k);
        Anhxa();

        Intent intent = getIntent();
        GVPH = intent.getBooleanExtra("GVPH", false);

        if (GVPH){
            titleTaoTK.setText("Tạo tài khoản cho \n Giáo Viên");
            MaLop.setText("Tạo 1 mã lớp *");
        }else {
            titleTaoTK.setText("Tạo tài khoản cho \n Phụ Huynh");
            MaLop.setText("Nhập mã lớp *");
        }
    }

    private void Anhxa(){
        MaLop = findViewById(R.id.MaLop);
        titleTaoTK = findViewById(R.id.titleTaoTK);
        TKTTK = findViewById(R.id.TKTTK);
        MTKTTK = findViewById(R.id.MTKTTK);
        MKTTK = findViewById(R.id.MKTTK);
        MLTTK = findViewById(R.id.MLTTK);
    }

    private void HoTro(String sTitle, String sMessage){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setTitle("Hỗ trợ về " + sTitle);
        dialog.setMessage(sMessage);
        dialog.show();
    }

    //bắt sự kiện cho item menu
    public void onClickTTK(View view){
        switch (view.getId()){
                //hổ trợ về mã tài khoản
            case R.id.hotrMTK:
                HoTro("Mã tài khoản", "- Mã tài khoản là mã do bạn tự đặt có thể 1 hoặc nhiều số nhằm cung cấp để lấy lại khi quên mật khẩu.");
                break;

                //hổ trợ về mã lớp
            case R.id.hotrML:
                String gv = "- Mã lớp này là 1 mã được đặt riêng cho 1 lớp do 1 giáo viên tạo lúc tạo tài khoản.\n- Mã lớp được dùng để truy vấn thông tin của lớp do GV đó quản lý.\n- Nếu tài khoản bạn tạo là phía phụ huynh thì hay nhập mã này do GV cấp để có thể truy vấn đến thông tin của lớp mình.";
                HoTro("Mã lớp", gv);
                break;

                //hủy Tạo TK
            case R.id.huyTTK:
                finish();
                break;

                //tạo tài khoản mới
            case R.id.okTTK:
                String TaiKhoan = TKTTK.getText().toString().trim();
                String MatKhau = MKTTK.getText().toString().trim();
                String MaTK = MTKTTK.getText().toString().trim();
                String MaLop = MLTTK.getText().toString().trim();
                if (!TaiKhoan.isEmpty() && !MatKhau.isEmpty() && !MaTK.isEmpty() && !MaLop.isEmpty()){
                    int iMatKhau = Integer.parseInt(MatKhau);
                    int iMaTK = Integer.parseInt(MaTK);
                    int iMaLop = Integer.parseInt(MaLop);
                    User user = new User(TaiKhoan, iMatKhau, iMaLop, GVPH, iMaTK);
                    setUrlAddData(user);
                }else Toast.makeText(MainActivity_Tao_TK.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void setUrlAddData(User user){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Thành Công")){
                            TKTTK.setText("");
                            MKTTK.setText("");
                            MTKTTK.setText("");
                            MLTTK.setText("");
                            TKMK(user);
                            //nếu tk phía GV đăng nhập thì insert giá trị mặt định vào trk
                            if (user.ismGV_PH()){
                                InsertDefault(user);
                            }
                        }else if (response.trim().equals("Tài khoản đã tồn tại!")){
                            Toast.makeText(MainActivity_Tao_TK.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }else if (response.trim().equals("Đã tồn tại MSL!")){
                            Toast.makeText(MainActivity_Tao_TK.this, "Mã lớp đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(MainActivity_Tao_TK.this, "Thất Bại!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity_Tao_TK.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("pTaiKhoan", user.getmTaiKhoan());
                        param.put("pMatKhau", user.getmMatKhau() + "");
                        param.put("pMSL", user.getmMSL() + "");
                        param.put("pQMK", user.getmQ_MK() + "");
                        if (user.ismGV_PH()){
                            param.put("pGVPH", "1");
                        }else param.put("pGVPH", "0");
                        return param;
                    }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(stringRequest);
    }
    private  void TKMK(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tạo thành công tài khoản của bạn là:");
        builder.setMessage("Tài khoản: " + user.getmTaiKhoan() + "\nMật khẩu: " + user.getmMatKhau() + "\nMã tài khoản: " +user.getmQ_MK());
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //khong cho ng dùng click thoát khi bấm ở bên ngoài
        builder.setCancelable(false);
        builder.show();
    }

    //InsertDefault
    private void InsertDefault(User user){
        Call<String> callBack = dataClient.InsertDefault(user.getmMSL(), "matdinh", "Name GV", "MailGV@gmail.com", "matdinh", 1, "matdinh", "");
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_Tao_TK.this, "Lỗi tạo TK chưa hoàn tất thông tin: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
