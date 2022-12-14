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

    //AppBarLayout: b??? c???c thanh ???ng d???ng
    AppBarLayout mAppBarLayout;
    //CollapsingToolbarLayout: thu g???n b??? c???c thanh c??ng c???
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    //c???a tk androidx,Toolbar: thanh c??ng c???
    Toolbar mToolbar;
    //l?? n??t add, FloatingActionButton: n??t h??nh ?????ng n???i
    FloatingActionButton mFloatingActionButton;
    public static RecyclerView mRecyclerView;
    public static AdapterHocSinh adapter;
    Menu mMenu;
    public static List<HocSinh> mHocSinhs;
    boolean isExpanded = true;//tr???ng th??i c???a FloatingActionButton

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
        //s??? ki???n click item menu Navigation
        mNavigationView.setNavigationItemSelectedListener(this);

        //TODO Set Info GV l??n layout
        setInfoGV();
        if (!userLogin.ismGV_PH()){
            mFloatingActionButton.setImageResource(R.drawable.ic_setting_user);
        }


        //TODO Set item HS cho RecyclerView
        //set layout hi???n th??? cho RecyclerView (c?? 3 d???ng layout hi???n th???)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//d???ng list
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mHocSinhs = getDataHS();
        adapter = new AdapterHocSinh();

        //TODO s??? ki???n onClick Item HS
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

        //??nh x??? headerLayout c???a NavigationView
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
        //g???i ?????n h??m h??? tr??? tool bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //g??n icon
        mToolbar.setNavigationIcon(R.drawable.ic_menu);

        //s??? ki???n onClick hi???n menu
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //TODO N??t Toolbar
    private void initToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//????? hi???n th??? n??t add
        }
    }

    //TODO initToolbarAnimations
    private void initToolbarAnimations(){
        mCollapsingToolbarLayout.setTitle("L???p: ");
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

    //TODO s??? ki???n onclick n??t add
    private void onClickBtnAdd(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override//h??m b???t s??? ki???n Button add
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
        //b???t s??? ki???n n??t add (FloatingActionButton) khi ??c g??n v??o menu
        if (item.getTitle() == "Add"){
            if (userLogin.ismGV_PH()){
                SetTB();
            }else {
                AllInfoGV();
            }
        }
        switch (item.getItemId()){
            //s??? ki???n oncick hi???n menu drawable
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;//g??n menu ??? ph???n khai b??o b???ng v???i menu ta t???o trong resoucre
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenu != null && (!isExpanded || mMenu.size() != 1)){
            //TODO g??n n??t add (FloatingActionButton) v??o menu
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

    //TODO s??? ki???n onclick menu Navigation
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
        //????ng menu
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    //TODO B???T S??? KI???N ONCLICK_INFO
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

                //s??? ki???n dialog IMG
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
        //Bo tr??n v?? set v??? tr?? hi???n th??? dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hi???n th??? ??? gi???a
        window.setAttributes(windowLayoutParams);
        dialog.show();
    }

    //todo Dialog set IMG
    private void DialogIMG(){
        dialogSetImg = new Dialog(this);
        dialogSetImg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetImg.setContentView(R.layout.dialog_set_image);

        //??nh x??? view
        SetImg = dialogSetImg.findViewById(R.id.SetImg);

        Window window = dialogSetImg.getWindow();
        if (window == null){
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSetImg.show();
    }


    //k???t n???i t???i ???????ng d???n uri v?? duy???t xem b??n trong c?? g?? hay kh??ng n???u c?? th?? get nh???ng gi?? tr??? ???? ra
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

    //TODO xin quy???n foder
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_IMAGE && grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            //Intent.ACTION_PICK: ch???n 1 c??i g?? ????
            Intent intent = new Intent(Intent.ACTION_PICK);
            //ch??? ch???n h??nh ???nh th??i
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        }else if (REQUEST_CODE_CALL == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (infoGV.getmSDT() != null){
                if (infoGV.getmSDT().equals("matdinh")){
                Toast.makeText(this, "GV Ch??a thi???c l???p SDT!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 =new Intent();
                    intent1.setAction(Intent.ACTION_CALL);
                    intent1.setData(Uri.parse("tel:" + infoGV.getmSDT()));
                    startActivity(intent1);
                }
            }else Toast.makeText(this, "GV Ch??a thi???c l???p SDT ho???c M?? TK kh??ng h???p l???", Toast.LENGTH_SHORT).show();
        }else if (REQUEST_CODE_SMS == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (infoGV.getmSDT() != null){
                if (infoGV.getmSDT().equals("matdinh")){
                Toast.makeText(this, "GV Ch??a thi???c l???p SDT!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 =new Intent();
                    intent1.setAction(Intent.ACTION_SENDTO);
                    intent1.putExtra("sms_body", "Gi??o Vi??n: " + infoGV.getmName() + "\n");
                    intent1.setData(Uri.parse("sms:" + infoGV.getmSDT()));
                    startActivity(intent1);
                }
            }else Toast.makeText(this, "GV Ch??a thi???c l???p SDT ho???c M?? TK kh??ng h???p l???", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(MainActivity_Info_HocSinh.this, "B???n ch??a c???p quy???n!", Toast.LENGTH_SHORT).show();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            //uri: ???????ng d???n t???i h??nh ???nh ????
            Uri uri = data.getData();
            //g??n ???????ng d???n v??o realPath
            realPath = getRealPathFromURI(uri);
            try {
                //sau khi c?? ???????ng d???n th?? d???ng InputStream k???t n???i v?? m??? ???????ng d???n ???? ra
                //b???t l??i try catch
                InputStream inputStream = getContentResolver().openInputStream(uri);
                //convert v??? d???ng Bitmap v???i BitmapFactory
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //set h??nh cho image
                SetImg.setImageBitmap(bitmap);
                Check = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //TODO up ???nh l??n sever
    private void UpPhoto(){
        File file = new File(realPath);//l???y ???????ng d???n file
        String file_path = file.getAbsolutePath(); //t??n file

        //ch???n s???a l???i t??n h??nh
        String[] mangtenfile = file_path.split("\\.");
        //s??? d???ng bi???n th???i gian milis ????? g??n v??o t??n
        file_path = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        Log.d("BBB-T??nH??nh", file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //uploaded_file: key g???i l??n, 2 l?? ???????ng d???n, 3 l?? ki???u d??? li???u c???a file
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);

        //T???o k???t n???i v?? g???i gi?? tr??? v???
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
                Toast.makeText(MainActivity_Info_HocSinh.this, "L???i:" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity_Info_HocSinh.this, "Th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        if (In.equals("dPhoto1") || In.equals("dPhoto2")){
                            dialogSetImg.cancel();
                        }
                        setInfoGV();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "L???i: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        //Bo tr??n v?? set v??? tr?? hi???n th??? dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hi???n th??? ??? gi???a
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
                    mCollapsingToolbarLayout.setTitle("L???p: " + infoGV.getmTenLop());
                    Key_Lop = infoGV.getmTenLop();
                }
            }

            @Override
            public void onFailure(Call<InfoGV> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "L???i: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO Dialog x??a hay s???a h???c sinh
    private void dialogXoaSua(HocSinh hocSinh){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Th??ng b??o");
        dialog.setMessage("B???n mu???n th???c hi???n: ");

        dialog.setPositiveButton("S???a", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddHs.class);
                intent.putExtra("Key_Sua", 1);
                intent.putExtra("Key_ID", hocSinh.getmID());
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("X??a", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XoaHS(hocSinh.getmID());
            }
        });
        dialog.show();
    }

    //TODO X??a HS
    private void XoaHS(int ID){
        Call<HocSinh> callBack = dataClient.GetDetailtHS(ID, 1);
        callBack.enqueue(new Callback<HocSinh>() {
            @Override
            public void onResponse(Call<HocSinh> call, Response<HocSinh> response) {
                if(response != null){
                    HocSinh hocSinh = response.body();
                    if (hocSinh.getmHoTen().equals("Success")){
                        Toast.makeText(MainActivity_Info_HocSinh.this, "???? x??a", Toast.LENGTH_SHORT).show();
                        mHocSinhs = getDataHS();
                    }else
                        Toast.makeText(MainActivity_Info_HocSinh.this, "Th???t b???i!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HocSinh> call, Throwable t) {
                Toast.makeText(MainActivity_Info_HocSinh.this, "L???i " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO N??t onBackPress
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.mDrawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            //trong th???i gian 2s nh???n back s??? tho??t application
            if (backPressTinme + 2000 > System.currentTimeMillis()){
                //Khoi tao lai Activity main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Tao su kien ket thuc app
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();//tho??t application
                mToast.cancel();//t???t hi???n th??? lu??n tk Toast
                return;
            }else {
                mToast = Toast.makeText(this, "Nh???n Back 1 l???n n???a ????? Tho??t!", Toast.LENGTH_SHORT);
                mToast.show();
            }
            backPressTinme = System.currentTimeMillis();
        }
    }

    //TODO all info GV
    private void AllInfoGV(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("GVCN");
        dialog.setMessage("L???p: " + fomatGV(infoGV.getmTenLop()) +
                            "\nT??n GVCN: " + fomatGV(infoGV.getmName()) +
                            "\nGmail: " + fomatGV(infoGV.getmGmail()) +
                            "\nSDT: " + fomatGV(infoGV.getmSDT()));
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    //Dinh d???ng
    private String fomatGV(String value){
        String result = "Ch??a c??!";
        if (value != null){
            if (!value.equals("matdinh") && value.length() > 0 && !value.equals("ERROR")){
                result = value;
            }
        }
        return result;
    }

    private void SetTB(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Xin Ch??o!");
        dialog.setMessage("B???n mu???n t???o th??ng b??o t???");
        dialog.setPositiveButton("Nh?? Tr?????ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddThongBao.class);
                intent.putExtra("Key_MSL", userLogin.getmMSL());
                intent.putExtra("Key_SetTB", "Nh?? Tr?????ng");
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Gi??o vi??n", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity_Info_HocSinh.this, MainActivity_AddThongBao.class);
                intent.putExtra("Key_MSL", userLogin.getmMSL());
                intent.putExtra("Key_SetTB", "Gi??o Vi??n");
                startActivity(intent);
            }
        });
        dialog.show();
    }
}