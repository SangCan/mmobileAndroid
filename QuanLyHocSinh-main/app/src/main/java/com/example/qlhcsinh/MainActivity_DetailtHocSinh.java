package com.example.qlhcsinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlhcsinh.Object.HocSinh;
import com.example.qlhcsinh.Object.User;
import com.example.qlhcsinh.Retrofit.DataClient;
import com.example.qlhcsinh.Retrofit.UtilsAPI;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_DetailtHocSinh extends AppCompatActivity {
    ImageView Detl_Img;
    ImageButton Detl_FB;
    TextView Detl_Info;
    EditText Detl_GhiChu;
    DataClient dataClient = UtilsAPI.getData();
    HocSinh mHocSinh = null;
    int REQUEST_CODE_CALL = 26;
    int REQUEST_CODE_SMS = 27;
    User userLogin = MainActivity_Info_HocSinh.userLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detailt_hoc_sinh);
        Anhxa();
        Intent intent = getIntent();
        int Key_ID = intent.getIntExtra("Key_ID", 0);
        //TODO lấy hs từ sever
        GetDetailtHS(Key_ID);

        //TODO sự kiện longClick set lại Link FB
        Detl_FB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (userLogin.ismGV_PH()){
                    DiaLinkFB(mHocSinh);
                }
                return false;
            }
        });
    }
    private void Anhxa(){
        Detl_Img = findViewById(R.id.Detl_Img);
        Detl_Info = findViewById(R.id.Detl_Info);
        Detl_GhiChu = findViewById(R.id.Detl_GhiChu);
        Detl_FB = findViewById(R.id.Detl_FB);
    }

    public void OnClickDetailt(View view){
        switch (view.getId()){
            case R.id.Detl_FB:
                if (mHocSinh.getmLink().equals("chuaco")){
                    if (userLogin.ismGV_PH()){
                        DiaLinkFB(mHocSinh);
                    }else Toast.makeText(this, "GV Chưa thiếc lập!", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Intent intent1 =new Intent();
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse(mHocSinh.getmLink()));
                        startActivity(intent1);
                    }catch (Exception e){
                        if (userLogin.ismGV_PH()){
                            Toast.makeText(MainActivity_DetailtHocSinh.this, "Link fb không hợp lệ!\nThiếc lập lại.", Toast.LENGTH_SHORT).show();
                            DiaLinkFB(mHocSinh);
                        }else Toast.makeText(this, "GV thiếc lập sai Link fb!\nKhông thể tải!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.Detl_TraCuu:
                //TODO KIỂM TRA Bảng điểm hs
                KtraBanDiem();
                break;
            case R.id.Detl_Call:
                ActivityCompat.requestPermissions(MainActivity_DetailtHocSinh.this, new String[]{Manifest.permission.CALL_PHONE,}, REQUEST_CODE_CALL);
                break;
            case R.id.Detl_SMS:
                ActivityCompat.requestPermissions(MainActivity_DetailtHocSinh.this, new String[]{Manifest.permission.SEND_SMS,}, REQUEST_CODE_SMS);
                break;
            case R.id.Detl_Luu:
                String GhiChu = Detl_GhiChu.getText().toString().trim();
                if (GhiChu.length() > 0){
                    if (userLogin.ismGV_PH()){
                        UpGhiChu(mHocSinh.getmID(), GhiChu);
                    }else Toast.makeText(this, "P/H không thể thiếc lập!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void GetDetailtHS(int ID){
        Call<HocSinh> callBack = dataClient.GetDetailtHS(ID, 2);
        callBack.enqueue(new Callback<HocSinh>() {
            @Override
            public void onResponse(Call<HocSinh> call, Response<HocSinh> response) {
                if (response != null){
                    mHocSinh = response.body();
                    setDetailt(mHocSinh);
                }
            }
            @Override
            public void onFailure(Call<HocSinh> call, Throwable t) {
                Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDetailt(HocSinh hocSinh){
        String GT = "";
        if (hocSinh.getmGioiTinh()){
            GT = "Nam";
            Picasso.get().load(hocSinh.getmLinkPhoto()).error(R.drawable.nam).into(Detl_Img);
        }else{
            GT = "Nữ";
            Picasso.get().load(hocSinh.getmLinkPhoto()).error(R.drawable.nu).into(Detl_Img);
        }
        Detl_Info.setText("Họ Tên: " + hocSinh.getmHoTen() +
                            "\nMSHS: " + hocSinh.getmMSHS() +
                            "\nNăm Sinh: " + hocSinh.getmNamSinh() +
                            "\nGiới Tính: " + GT + "    Dân Tộc: " + hocSinh.getmDanToc() +
                            "\nNơi Sinh: " + hocSinh.getmNoiSinh() +
                            "\nChức Vụ: " + hocSinh.getmChucVu() +
                            "\nSDT P/H: " + hocSinh.getmSdtPh());
        Detl_GhiChu.setText(hocSinh.getmGhiChu());
    }

    //TODO Dialog set Link FB
    private void DiaLinkFB(HocSinh hocSinh){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_set_string);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(windowLayoutParams);

        EditText edtValue = (EditText) dialog.findViewById(R.id.edtValue);
        TextView OKSetString = (TextView) dialog.findViewById(R.id.OKSetString);
        TextView txTitle = (TextView) dialog.findViewById(R.id.txTitle);
        txTitle.setText("Thiếc lập Link fb cho SinhViên: " + hocSinh.getmHoTen());

        edtValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtValue.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });

        OKSetString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mLinkFB = edtValue.getText().toString().trim();
                if (mLinkFB.length() > 0 ){
                    setLinkFB(hocSinh.getmID(), mLinkFB, dialog);
                }else  Toast.makeText(MainActivity_DetailtHocSinh.this, "Vui lòng nhập LinkFB!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    //TODO kiểm tra câu tl của người dùng khi xin quyền call/sms
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // kiểm xem:requestCode có đúng hay ko ( ss với RQC), kiểm tra xem ng dùng có tl hay không grantResults.length, câu tl có thỏa điều kiện hay không.
        if (REQUEST_CODE_CALL == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent1 =new Intent();
            intent1.setAction(Intent.ACTION_CALL);
            intent1.setData(Uri.parse("tel:" + mHocSinh.getmSdtPh()));
            startActivity(intent1);
        }else if (REQUEST_CODE_SMS == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent1 =new Intent();
            intent1.setAction(Intent.ACTION_SENDTO);
            intent1.putExtra("sms_body", "Sinh Viên: " + mHocSinh.getmHoTen() + "\n");
            intent1.setData(Uri.parse("sms:" + mHocSinh.getmSdtPh()));
            startActivity(intent1);
        }else Toast.makeText(MainActivity_DetailtHocSinh.this, "Bạn không cho phép mỡ call/sms phone!", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //TODO set Link fb
    private void setLinkFB(int ID, String LinkFB, Dialog dialog){
        Call<String> callBack = dataClient.UpLinkFB(ID, LinkFB, 1);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        mHocSinh.setmLink(LinkFB);
                        dialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Up Ghi Chú
    private void UpGhiChu(int ID, String Value){
        Call<String> callBack = dataClient.UpLinkFB(ID, Value, 2);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Detl_GhiChu.setText(Value);
                        mHocSinh.setmGhiChu(Value);
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Đã lưu!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Ktra bản điểm hs có chưa
    private void KtraBanDiem(){
        Call<String> callBack = dataClient.KtraKey_ID(mHocSinh.getmID());
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Intent intent = new Intent(MainActivity_DetailtHocSinh.this, MainActivity_DiemHocTap.class);
                        intent.putExtra("Key_ID", mHocSinh.getmID());
                        intent.putExtra("Key_Ten", mHocSinh.getmHoTen());
                        intent.putExtra("Key_MSHS", mHocSinh.getmMSHS());
                        intent.putExtra("Key_ChucVu", mHocSinh.getmChucVu());
                        startActivity(intent);
                    }else if (response.body().equals("NotBanDiem")){
                        if(MainActivity_Info_HocSinh.userLogin.ismGV_PH()){
                            DialogTaoBanDiem();
                        }else
                            Toast.makeText(MainActivity_DetailtHocSinh.this, "Bản điểm sinh viên này chưa có!!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi " + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Dialog hỏi Tạo bản điểm khi chưa có
    private void DialogTaoBanDiem(){
        AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setTitle("Thông báo!");
        Dialog.setMessage("Bản điểm của HS " + mHocSinh.getmHoTen() + " chưa có\nXác nhận tạo!");
        Dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaoBanDiem();
            }
        });
        Dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        Dialog.show();
    }

    //TODO Tạo mới 1 bản điểm cho hs
    private void TaoBanDiem(){
        Call<String> callBack = dataClient.TaoBangDiem(mHocSinh.getmID());
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Thành công!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MainActivity_DetailtHocSinh.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_DetailtHocSinh.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}