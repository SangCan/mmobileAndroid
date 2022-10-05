package com.example.qlhcsinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlhcsinh.Adapter.AdapterHocSinh;
import com.example.qlhcsinh.Fragment.FragmentThongBao;
import com.example.qlhcsinh.Fragment.FragmentTKB;
import com.example.qlhcsinh.Object.HocSinh;
import com.example.qlhcsinh.Object.InfoGV;
import com.example.qlhcsinh.Object.OnClickItemHS;
import com.example.qlhcsinh.Object.User;
import com.example.qlhcsinh.Retrofit.DataClient;
import com.example.qlhcsinh.Retrofit.UtilsAPI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_Info_HocSinh extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    //AppBarLayout: bố cục thanh ứng dụng
    AppBarLayout mAppBarLayout;
    //CollapsingToolbarLayout: thu gọn bố cục thanh công cụ
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    //của tk androidx,Toolbar: thanh công cụ
    Toolbar mToolbar;
    //là nút add, FloatingActionButton: nút hành động nỗi
    FloatingActionButton mFloatingActionButton;
    public static RecyclerView mRecyclerView;
    public static AdapterHocSinh adapter;
    Menu mMenu;
    public static List<HocSinh> mHocSinhs;
    boolean isExpanded = true;//trạng thái của FloatingActionButton

    private static final int FRAGMENT_TKB = 2;
    private static final int FRAGMENT_HOCTAP = 3;
    private static int CURENT_FRAGMENT = 1;

    int REQUEST_CODE_IMAGE = 123, REQUEST_CODE_CALL = 12, REQUEST_CODE_SMS = 21;
    String realPath = "";
    String In = "";

    TextView txtName_GV, txtGmail_GV;
    ImageView img_GV1, SetImg, Img_GV2;
    EditText edtValue;

    public static User userLogin;
    public static DataClient dataClient = UtilsAPI.getData();

    Dialog dialogSetImg, dialogSetString;

    int Check = 0;

    public static ProgressBar mProgressBar_Info;

    public static String Key_Lop = "";
    InfoGV infoGV = null;

    long backPressTinme;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info_hoc_sinh);
        Anhxa();
        MainActivity.mProgressBar.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        userLogin = (User) intent.getSerializableExtra("Info_TKMK");
        ActionToolBar();

        initToolbar();
        initToolbarAnimations();
        onClickBtnAdd();
//        mFloatingActionButton.setImageResource(R.drawable.ic_info);
        //sự kiện click item menu Navigation
        mNavigationView.setNavigationItemSelectedListener(this);

        //TODO Set Info GV lên layout
        setInfoGV();
        if (!userLogin.ismGV_PH()){
            mFloatingActionButton.setImageResource(R.drawable.ic_setting_user);
        }


        //TODO Set item HS cho RecyclerView
        //set layout hiển thị cho RecyclerView (có 3 dạng layout hiển thị)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//dạng list
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mHocSinhs = getDataHS();
        adapter = new AdapterHocSinh();

        //TODO sự kiện onClick Item HS
        adapter.setOnClickItemHS(new OnClickItemHS() {
            @Override
            public void onClick(HocSinh hocSinh) {
                if (hocSinh.getmID() > 0){
                    Intent intent1 = new Intent(MainActivity_Info_HocSinh.this, MainActivity_DetailtHocSinh.class);
                    intent1.putExtra("Key_ID", hocSinh.getmID());
                    startActivity(intent1);
                }
            }

            @Override
            public void onLongClick(HocSinh hocSinh) {
                if (userLogin.ismGV_PH()){
                    dialogXoaSua(hocSinh);
                }
            }
        });
    }
    private void Anhxa(){
        mToolbar = findViewById(R.id.mToolbar);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        mNavigationView = findViewById(R.id.mNavigationView);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.mAppBarLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.mCollapsingToolbarLayout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.mFloatingActionButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvUser);
        Img_GV2 = findViewById(R.id.Img_GV2);

        //ánh xạ headerLayout của NavigationView
        View headerLayout  = mNavigationView.getHeaderView(0);
        txtGmail_GV = headerLayout .findViewById(R.id.txtGmail_GV);
        txtName_GV = headerLayout .findViewById(R.id.txtName_GV);
        img_GV1 = headerLayout .findViewById(R.id.img_GV1);
        mProgressBar_Info = findViewById(R.id.mProgressBar_Info);
    }

    //TODO get Data HS
    public static List<HocSinh> getDataHS(){
        List<HocSinh> list = new ArrayList<>();
        Call<List<HocSinh>> callBack = dataClient.GetHS(userLogin.getmMSL());
        callBack.enqueue(new Callback<List<HocSinh>>() {
            @Override
            public void onResponse(Call<List<HocSinh>> call, Response<List<HocSinh>> response) {
                if (response != null){
                    for (HocSinh x:response.body()){
                        list.add(x);
                    }
                    Collections.sort(mHocSinhs, HocSinh.AZ_HocSinh);
                    adapter.setmHocSinhs(mHocSinhs, mProgressBar_Info);
                    mRecyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<HocSinh>> call, Throwable t) {

            }
        });
        return list;
    }

    //TODO ActionToolBar
    private void ActionToolBar(){
        //gọi đến hàm hỗ trợ tool bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //gán icon
        mToolbar.setNavigationIcon(R.drawable.ic_menu);

        //sự kiện onClick hiện menu
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //TODO Nút Toolbar
    private void initToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//để hiện thị nút add
        }
    }

    //TODO initToolbarAnimations
    private void initToolbarAnimations(){
        mCollapsingToolbarLayout.setTitle("Lớp: ");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_bia_hs);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
//                int myColor = palette.getVibrantColor(getResources().getColor(R.color.purple_700));
                Resources res = getResources();
                int myColor = res.getColor(R.color.color_toolbar);
                mCollapsingToolbarLayout.setContentScrimColor(myColor);
                mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.black_trans));
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200){
                    isExpanded = false;
                }else isExpanded = true;
                invalidateOptionsMenu();
            }
        });
    }

    //TODO sự kiện onclick nút add
    private void onClickBtnAdd(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override//hàm bắt sự kiện Button add
            public void onClick(View v) {
                if (userLogin.ismGV_PH()){
                    Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddHs.class);
                    intent.putExtra("Key_MSL", userLogin.getmMSL());
                    startActivity(intent);
                }else
                    AllInfoGV();
            }
        });
    }

    //TODO Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //bắt sự kiện nút add (FloatingActionButton) khi đc gán vào menu
        if (item.getTitle() == "Add"){
            if (userLogin.ismGV_PH()){
                SetTB();
            }else {
                AllInfoGV();
            }
        }
        switch (item.getItemId()){
            //sự kiện oncick hiện menu drawable
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;//gán menu ở phần khai báo bằng với menu ta tạo trong resoucre
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenu != null && (!isExpanded || mMenu.size() != 1)){
            //TODO gán nút add (FloatingActionButton) vào menu
            if (userLogin.ismGV_PH()){
                mMenu.add("Add").setIcon(R.drawable.ic_notification_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }else mMenu.add("Add").setIcon(R.drawable.ic_setting_user).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(mMenu);
    }

    //TODO set Fragment
    private void ReplaceFragment(Fragment fragment, String s){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Conten_Fragment, fragment, s);
        fragmentTransaction.commit();
    }

    //TODO sự kiện onclick menu Navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_DS_HS:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("tkb");
                Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("thongbao");
                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    CURENT_FRAGMENT = 1;
                }
                if (fragment1 != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
                    CURENT_FRAGMENT = 1;
                }
                break;
            case R.id.nav_TKB:
                if (FRAGMENT_TKB != CURENT_FRAGMENT){
                    ReplaceFragment(new FragmentTKB(), "tkb");
                    CURENT_FRAGMENT = FRAGMENT_TKB;
                }
                break;
            case R.id.nav_ThongBao:
                if (FRAGMENT_HOCTAP != CURENT_FRAGMENT){
                    ReplaceFragment(new FragmentThongBao(), "thongbao");
                    CURENT_FRAGMENT = FRAGMENT_HOCTAP;
                }
                break;
            case R.id.nav_call:
                ActivityCompat.requestPermissions(MainActivity_Info_HocSinh.this, new String[]{Manifest.permission.CALL_PHONE,}, REQUEST_CODE_CALL);
                break;
            case R.id.nav_sms:
                ActivityCompat.requestPermissions(MainActivity_Info_HocSinh.this, new String[]{Manifest.permission.SEND_SMS,}, REQUEST_CODE_SMS);
                break;
            case R.id.nav_DangXuat:
                finish();
                break;
        }
        //đóng menu
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    //TODO BẮT SỰ KIỆN ONCLICK_INFO
    public void OnClick_Info(View view){
        switch (view.getId()){
            case R.id.Infor_GV:
                if (userLogin.ismGV_PH()){
                    DialogDefault();
                }
                break;
            case R.id.txtSetPhoto1:
                DialogIMG();
                In = "dPhoto1";
                break;
            case R.id.txtSetName:
                DialogString();
                In = "dName";
                break;
            case R.id.txtSetGmail:
                In = "dMail";
                DialogString();
                break;
            case R.id.txtSetPhoto2:
                DialogIMG();
                In = "dPhoto2";
                break;
            case R.id.txtSetSDT:
                In = "dSDT";
                DialogString();
                break;
            case R.id.txtSetTenLop:
                In = "dTenLop";
                DialogString();
                break;

            case R.id.Img_GV2:
                if (userLogin.ismGV_PH()){
                    DialogDefault();
                }
                break;

                //sự kiện dialog IMG
            case R.id.SetImg:
                ActivityCompat.requestPermissions(
                        MainActivity_Info_HocSinh.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_IMAGE);
                break;
            case R.id.HuyImg:
                dialogSetImg.cancel();
                if (Check == 1){
                    Check = 0;
                }
                break;
            case R.id.OKImg:
                if (Check == 1){
                    UpPhoto();
                    Check = 0;
                }
                break;

                //dialog set String
            case R.id.OKSetString:
                String value = edtValue.getText().toString().trim();
                if (value.length() > 0){
                    UpChuoi(value, In);
                    dialogSetString.cancel();
                }
                break;
        }
    }



    //TODO Dialog set Default
    private void DialogDefault(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_default);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        //Bo tròn và set vị trí hiển thị dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hiển thị ở giữa
        window.setAttributes(windowLayoutParams);
        dialog.show();
    }

    //todo Dialog set IMG
    private void DialogIMG(){
        dialogSetImg = new Dialog(this);
        dialogSetImg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetImg.setContentView(R.layout.dialog_set_image);

        //ánh xạ view
        SetImg = dialogSetImg.findViewById(R.id.SetImg);

        Window window = dialogSetImg.getWindow();
        if (window == null){
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSetImg.show();
    }


    //kết nối tới đường dẫn uri và duyệt xem bên trong có gì hay không nếu có thì get những giá trị đó ra
    private String getRealPathFromURI(Uri uri){
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()){
            int Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(Column_index);
        }
        cursor.close();
        return path;
    }

    //TODO xin quyền foder
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_IMAGE && grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            //Intent.ACTION_PICK: chọn 1 cái gì đó
            Intent intent = new Intent(Intent.ACTION_PICK);
            //chỉ chọn hình ảnh thôi
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        }else if (REQUEST_CODE_CALL == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (infoGV.getmSDT() != null){
                if (infoGV.getmSDT().equals("matdinh")){
                Toast.makeText(this, "GV Chưa thiếc lập SDT!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 =new Intent();
                    intent1.setAction(Intent.ACTION_CALL);
                    intent1.setData(Uri.parse("tel:" + infoGV.getmSDT()));
                    startActivity(intent1);
                }
            }else Toast.makeText(this, "GV Chưa thiếc lập SDT hoặc Mã TK không hợp lệ", Toast.LENGTH_SHORT).show();
        }else if (REQUEST_CODE_SMS == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (infoGV.getmSDT() != null){
                if (infoGV.getmSDT().equals("matdinh")){
                Toast.makeText(this, "GV Chưa thiếc lập SDT!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 =new Intent();
                    intent1.setAction(Intent.ACTION_SENDTO);
                    intent1.putExtra("sms_body", "Giáo Viên: " + infoGV.getmName() + "\n");
                    intent1.setData(Uri.parse("sms:" + infoGV.getmSDT()));
                    startActivity(intent1);
                }
            }else Toast.makeText(this, "GV Chưa thiếc lập SDT hoặc Mã TK không hợp lệ", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(MainActivity_Info_HocSinh.this, "Bạn chưa cấp quyền!", Toast.LENGTH_SHORT).show();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            //uri: đường dẫn tới hình ảnh đó
            Uri uri = data.getData();
            //gán đường dẫn vào realPath
            realPath = getRealPathFromURI(uri);
            try {
                //sau khi có đường dẫn thì dừng InputStream kết nối và mỡ đường dẫn đó ra
                //bắt lôi try catch
                InputStream inputStream = getContentResolver().openInputStream(uri);
                //convert về dạng Bitmap với BitmapFactory
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //set hình cho image
                SetImg.setImageBitmap(bitmap);
                Check = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //TODO up ảnh lên sever
    private void UpPhoto(){
        File file = new File(realPath);//lấy đường dẫn file
        String file_path = file.getAbsolutePath(); //tên file

        //chỉn sửa lại tên hình
        String[] mangtenfile = file_path.split("\\.");
        //sử dụng biến thời gian milis để gán vào tên
        file_path = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        Log.d("BBB-TênHình", file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //uploaded_file: key gửi lên, 2 là đường dẫn, 3 là kiểu dữ liệu của file
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);

        //Tạo kết nối và gửi giá trị về
        Call<String> callBack = dataClient.UpPhoto(body);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    String Photo = response.body();
                    UpChuoi(Photo, In);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "Lỗi:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpChuoi(String Photo, String In){
        if(In.equals("dPhoto1") || In.equals("dPhoto2")){
            Photo = UtilsAPI.BaseUrl + "image/" + Photo;
        }
        Call<String> callBack = dataClient.UpValue(userLogin.getmMSL(), Photo, In);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    if (response.body().equals("Success")){
                        Toast.makeText(MainActivity_Info_HocSinh.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        if (In.equals("dPhoto1") || In.equals("dPhoto2")){
                            dialogSetImg.cancel();
                        }
                        setInfoGV();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO dialog set String
    private void DialogString(){
        dialogSetString = new Dialog(this);
        dialogSetString.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetString.setContentView(R.layout.dialog_set_string);

        edtValue = dialogSetString.findViewById(R.id.edtValue);
        Window window = dialogSetString.getWindow();
        if (window == null){
            return;
        }
        //Bo tròn và set vị trí hiển thị dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hiển thị ở giữa
        window.setAttributes(windowLayoutParams);

        dialogSetString.show();
    }

    //set Info GV
    private void setInfoGV(){
        Call<InfoGV> callBack = dataClient.GetInfoGV(userLogin.getmMSL());
        callBack.enqueue(new Callback<InfoGV>() {
            @Override
            public void onResponse(Call<InfoGV> call, Response<InfoGV> response) {
                if (response != null){
                    infoGV = response.body();
                    txtName_GV.setText(infoGV.getmName());
                    txtGmail_GV.setText(infoGV.getmGmail());
                    Picasso.get().load(infoGV.getmPhoto1()).placeholder(R.drawable.ic_hoa).error(R.drawable.ic_hoa).into(img_GV1);
                    Picasso.get().load(infoGV.getmPhoto2()).placeholder(R.drawable.ic_hoa).error(R.drawable.img_bia_hs).into(Img_GV2);
                    mCollapsingToolbarLayout.setTitle("Lớp: " + infoGV.getmTenLop());
                    Key_Lop = infoGV.getmTenLop();
                }
            }

            @Override
            public void onFailure(Call<InfoGV> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Dialog xóa hay sửa học sinh
    private void dialogXoaSua(HocSinh hocSinh){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thông báo");
        dialog.setMessage("Bạn muốn thực hiện: ");

        dialog.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddHs.class);
                intent.putExtra("Key_Sua", 1);
                intent.putExtra("Key_ID", hocSinh.getmID());
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XoaHS(hocSinh.getmID());
            }
        });
        dialog.show();
    }

    //TODO Xóa HS
    private void XoaHS(int ID){
        Call<HocSinh> callBack = dataClient.GetDetailtHS(ID, 1);
        callBack.enqueue(new Callback<HocSinh>() {
            @Override
            public void onResponse(Call<HocSinh> call, Response<HocSinh> response) {
                if(response != null){
                    HocSinh hocSinh = response.body();
                    if (hocSinh.getmHoTen().equals("Success")){
                        Toast.makeText(MainActivity_Info_HocSinh.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        mHocSinhs = getDataHS();
                    }else
                        Toast.makeText(MainActivity_Info_HocSinh.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HocSinh> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Nút onBackPress
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.mDrawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            //trong thời gian 2s nhấn back sẽ thoát application
            if (backPressTinme + 2000 > System.currentTimeMillis()){
                //Khoi tao lai Activity main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Tao su kien ket thuc app
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();//thoát application
                mToast.cancel();//tắt hiển thị luôn tk Toast
                return;
            }else {
                mToast = Toast.makeText(this, "Nhấn Back 1 lần nữa để Thoát!", Toast.LENGTH_SHORT);
                mToast.show();
            }
            backPressTinme = System.currentTimeMillis();
        }
    }

    //TODO all info GV
    private void AllInfoGV(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("GVCN");
        dialog.setMessage("Lớp: " + fomatGV(infoGV.getmTenLop()) +
                            "\nTên GVCN: " + fomatGV(infoGV.getmName()) +
                            "\nGmail: " + fomatGV(infoGV.getmGmail()) +
                            "\nSDT: " + fomatGV(infoGV.getmSDT()));
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    //Dinh dạng
    private String fomatGV(String value){
        String result = "Chưa có!";
        if (value != null){
            if (!value.equals("matdinh") && value.length() > 0 && !value.equals("ERROR")){
                result = value;
            }
        }
        return result;
    }

    private void SetTB(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Xin Chào!");
        dialog.setMessage("Bạn muốn tạo thông báo từ");
        dialog.setPositiveButton("Nhà Trường", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddThongBao.class);
                intent.putExtra("Key_MSL", userLogin.getmMSL());
                intent.putExtra("Key_SetTB", "Nhà Trường");
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Giáo viên", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddThongBao.class);
                intent.putExtra("Key_MSL", userLogin.getmMSL());
                intent.putExtra("Key_SetTB", "Giáo Viên");
                startActivity(intent);
            }
        });
        dialog.show();
    }
}