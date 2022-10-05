package com.example.qlhcsinh.Retrofit;

import com.example.qlhcsinh.Object.DiemHS;
import com.example.qlhcsinh.Object.HocSinh;
import com.example.qlhcsinh.Object.InfoGV;
import com.example.qlhcsinh.Object.TKB;
import com.example.qlhcsinh.Object.ThongBao;
import com.example.qlhcsinh.Object.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    //phương thức login
    //gửi dữ liệu dạng chuổi lên sever
    @FormUrlEncoded
    @POST("Login.php")
    Call<User> Login(@Field("pTaiKhoan") String User, @Field("pMatKhau") int Pass);

    //phương thức InsertDefault
    @FormUrlEncoded
    @POST("InsertDefault.php")
    Call<String> InsertDefault(@Field("pMSL") int MSL, @Field("pPhoto1") String Photo1, @Field("pName") String Name, @Field("pMail") String Mail, @Field("pPhoto2") String Photo2, @Field("Info") int Info, @Field("pSDT") String SDT, @Field("pLop") String Lop);

    //phương thức up ảnh lên sever
    //tạo phương thức gửi ảnh với @Multipart: có thể up file lên sever
    @Multipart
    @POST("UpPhoto.php")
    Call<String> UpPhoto(@Part MultipartBody.Part photo);

    //up chuổi
    @FormUrlEncoded
    @POST("UpChuoi.php")
    Call<String> UpValue(@Field("pMSL") int MSL, @Field("pValue") String Chuoi, @Field("pIn") String Value);

    //Get Info GV
    @FormUrlEncoded
    @POST("GetInfoGV.php")
    Call<InfoGV> GetInfoGV(@Field("pMSL") int MSL);

    //Get HS
    @FormUrlEncoded
    @POST("GetHS.php")
    Call<List<HocSinh>> GetHS(@Field("pMSL") int MSL);

    //Up new HS
    @FormUrlEncoded
    @POST("UpNewHS.php")
    Call<String> UpNewHS(@Field("pMSL") int MSL,
                         @Field("pHoTen") String HoTen,
                         @Field("pMSHS") int MSHS,
                         @Field("pNamSinh") String NamSinh,
                         @Field("pGioiTinh") int GioiTinh,
                         @Field("pDanToc") String DanToc,
                         @Field("pNoiSinh") String NoiSinh,
                         @Field("pChucVu") String ChucVu,
                         @Field("pSdtPh") String SdtPh,
                         @Field("pLinkPhoto") String LinkPhoto,
                         @Field("pCheckID") int ID);

    //Get Detailt HS
    @FormUrlEncoded
    @POST("GetDetailtHS.php")
    Call<HocSinh> GetDetailtHS(@Field("pID") int ID, @Field("pCheck") int Check);

    //Up Link FB
    @FormUrlEncoded
    @POST("UpLinkFB.php")
    Call<String> UpLinkFB(@Field("pID") int ID, @Field("pValue") String LinkFB, @Field("pCheck") int Check);

    //Up/Sua TKB
    @FormUrlEncoded
    @POST("TKB.php")
    Call<String> UpSetTKB(@Field("pMSL") int MSL,
                         @Field("pThu") int Thu,
                         @Field("pSang1") String Sang1,
                         @Field("pSang2") String Sang2,
                         @Field("pSang3") String Sang3,
                         @Field("pSang4") String Sang4,
                         @Field("pSang5") String Sang5,
                          @Field("pChieu1") String Chieu1,
                          @Field("pChieu2") String Chieu2,
                          @Field("pChieu3") String Chieu3,
                          @Field("pChieu4") String Chieu4,
                          @Field("pChieu5") String Chieu5,
                          @Field("pUpSet") int UpSet);

    //Get TKB
    @FormUrlEncoded
    @POST("GetTKB.php")
    Call<List<TKB>> GetTKB(@Field("pMSL") int MSL);

    //Ktra Bảng Điểm
    @FormUrlEncoded
    @POST("KtraKey_ID.php")
    Call<String> KtraKey_ID(@Field("pKey_ID") int Key_ID);

    //Tạo Bảng Điểm
    @FormUrlEncoded
    @POST("TaoBangDiem.php")
    Call<String> TaoBangDiem(@Field("pKey_ID") int Key_ID);

    //Lấy Bảng điểm ra cho HS
    @FormUrlEncoded
    @POST("GetBangDiem.php")
    Call<List<DiemHS>> GetBangDiem(@Field("pKey_ID") int Key_ID);

    //Set diem theo Key_ID
    @FormUrlEncoded
    @POST("SetDiem.php")
    Call<String> SetDiem(@Field("pKey_ID") int Key_ID, @Field("pD_Mieng") double D_Mieng, @Field("pD_15p") double D_15p, @Field("pD_1Tiet") double D_1Tiet, @Field("pD_HocKy") double D_HocKy);

    //Add Thông báo
    @FormUrlEncoded
    @POST("AddThongBao.php")
    Call<String> AddThongBao(@Field("pMSL") int MSL,
                             @Field("pGV_Truong") String GV_Truong,
                             @Field("pTenTB") String TenTB,
                             @Field("pNoiDungTB") String NoiDungTB,
                             @Field("pNgay") String Ngay,
                             @Field("pCheck") int Check);

    //Lấy Thông Báo
    @FormUrlEncoded
    @POST("GetTB.php")
    Call<List<ThongBao>> GetTB(@Field("pMSL") int MSL);

    //Lấy Thông Báo
    @FormUrlEncoded
    @POST("XoaTB.php")
    Call<String> XoaTB(@Field("pKey_ID") int ID);
}
