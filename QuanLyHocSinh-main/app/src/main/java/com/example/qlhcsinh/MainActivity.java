package com.example.qlhcsinh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class MainActivity extends AppCompatActivity {
    String urlDoiMK = "http://172.16.1.66/QLHS/DoiMatKhau.php";
    Dialog pDialog;
    EditText edtTK;
    EditText edtMK;
    SharedPreferences preferences;
    public static ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);edtTK = findViewById(R.id.edt_TK_Main);
        edtMK = findViewById(R.id.edt_MK_Main);
        mProgressBar = findViewById(R.id.mProgressBar);

        //lưu tài khoản mật khẩu
        preferences = getSharedPreferences("DataLogin", MODE_PRIVATE);
        edtTK.setText(preferences.getString("TK", ""));
        edtMK.setText(preferences.getString("MK", ""));

        //set bo tròn cho image
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_hoa);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
        ImageView circularImageView = (ImageView) findViewById(R.id.imageView);
        circularImageView.setImageBitmap(circularBitmap);

    }
    //bắt sự kiện on click
    public void onClickMain(View view){
        switch (view.getId()){
            //tạo tài khoản
            case R.id.btnTaoTK:
                dialogGV_PH();
                break;
            case R.id.QMK_Main:
                DialogQMK();
                break;
            case R.id.DangNhapMain:
                String TK = edtTK.getText().toString().trim();
                String MK = edtMK.getText().toString().trim();
                if (!TK.isEmpty() && !MK.isEmpty()){
                    mProgressBar.setVisibility(View.VISIBLE);
                    int intMK = Integer.parseInt(MK);
                    DataClient DataClient = UtilsAPI.getData();
                    Call<User> callBack = DataClient.Login(TK, intMK);
                    callBack.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            if (response != null){
                                User user = response.body();
                                if (user.getmTaiKhoan().equals("ERROR TKMK")){
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Sai thông tin", Toast.LENGTH_SHORT).show();
                                }else if (user.getmTaiKhoan().equals("ERROR")){
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                }else {
                                    //đăng nhập thành công lưu tk
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("TK", user.getmTaiKhoan());
                                    editor.putString("MK", user.getmMatKhau() + "");
                                    editor.commit();
                                    //chuyển qua màn hình info
                                    Intent intent = new Intent(MainActivity.this, MainActivity_Info_HocSinh.class);
                                    intent.putExtra("Info_TKMK", user);
                                    startActivity(intent);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("BBB-Check-ERROR", t.getMessage());
                        }
                    });
                }
                break;
        }
    }

    //dialog hỏi tạo tài khoản cho GV hay PH
    private void dialogGV_PH(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông báo");
        dialog.setMessage("Bạn muốn tạo tài khoản cho: ");

        dialog.setPositiveButton("Giáo Viên", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, MainActivity_Tao_TK.class);
                intent.putExtra("GVPH", true);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Phụ Huynh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, MainActivity_Tao_TK.class);
                intent.putExtra("GVPH", false);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    //Dialog Quên MK
    private void DialogQMK(){
        //Khởi tạo dialog
        pDialog = new Dialog(this);
        // lệnh bỏ đi title mặt định của dialog (1 số đt có thể k hổ trợ và bị lỗi)
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.dialog_quen_mk);

        Window window = pDialog.getWindow();
        if (window == null){
            return;
        }

        //Bo tròn và set vị trí hiển thị dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hiển thị ở giữa
        window.setAttributes(windowLayoutParams);

        //click bên ngoài có đóng dialog mặt định là có (ở đây mình để không)
        pDialog.setCanceledOnTouchOutside(false);
        //ánh xạ các view trong dialog
        TextView btnHuy = pDialog.findViewById(R.id.huyQMK);
        TextView btnXacNhan = pDialog.findViewById(R.id.okQMK);

        EditText edtTK = pDialog.findViewById(R.id.edt_QMK);
        EditText edtMTK = pDialog.findViewById(R.id.edt_MTK_QMK);
        EditText edtMKM1 = pDialog.findViewById(R.id.edt_MKM1_QMK);
        EditText edtMKM2 = pDialog.findViewById(R.id.edt_MKM2_QMK);

        //bắt sự kiện
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.cancel();    //lệnh đóng dialog
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TK = edtTK.getText().toString().trim();
                String MTK = edtMTK.getText().toString().trim();
                String MK1 = edtMKM1.getText().toString().trim();
                String MK2 = edtMKM2.getText().toString().trim();
                if (!TK.isEmpty() && !MTK.isEmpty() && !MK1.isEmpty() && !MK2.isEmpty()){
                    if (MK1.equals(MK2)){
                        int iMTK = Integer.parseInt(MTK);
                        int iMK = Integer.parseInt(MK1);
                        User user = new User(TK, iMK, iMTK);
                        DMK_DN(user, urlDoiMK);
                    }else Toast.makeText(MainActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        pDialog.show();  //lệnh show dialog
    }
    //tham số int DMK_DN để xác định đổi mk hay đăng nhập
    private void DMK_DN(User user, String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Thành Công")){
                            pDialog.cancel();
                            Toast.makeText(MainActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        }else if (response.trim().equals("Sai thong tin")){
                            Toast.makeText(MainActivity.this, "Tài khoản hoặc mã tài khoản không khớp!", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(MainActivity.this, "Thất Bại!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối!: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("pTaiKhoan", user.getmTaiKhoan());
                param.put("pMatKhau1", user.getmMatKhau() + "");
                param.put("pQMK", user.getmQ_MK() + "");
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


}
