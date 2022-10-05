package com.example.qlhcsinh.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

public class HocSinh implements Serializable {
    @SerializedName("ID")
    @Expose
    private int mID;

    public int getmMSL() {
        return mMSL;
    }

    public void setmMSL(int mMSL) {
        this.mMSL = mMSL;
    }

    @SerializedName("MSL")
    @Expose
    private int mMSL;

    @SerializedName("HoTen")
    @Expose
    private String mHoTen;

    @SerializedName("MSHS")
    @Expose
    private int mMSHS;

    @SerializedName("NamSinh")
    @Expose
    private String mNamSinh;

    @SerializedName("GioiTinh")
    @Expose
    private Boolean mGioiTinh;

    @SerializedName("DanToc")
    @Expose
    private String mDanToc;

    @SerializedName("NoiSinh")
    @Expose
    private String mNoiSinh;

    @SerializedName("ChucVu")
    @Expose
    private String mChucVu;

    @SerializedName("SdtPh")
    @Expose
    private String mSdtPh;

    @SerializedName("LinkPhoto")
    @Expose
    private String mLinkPhoto;

    @SerializedName("Linkfb")
    @Expose
    private String mLink;

    @SerializedName("GhiChu")
    @Expose
    private String mGhiChu;

    public HocSinh() {
    }
    //Contructo TODO Up HS (not Linkfb, LinkPhoto, GhiChu, ID)
    public HocSinh(String mHoTen, int mMSHS, String mNamSinh, Boolean mGioiTinh, String mDanToc, String mNoiSinh, String mChucVu, String mSdtPh) {
        this.mHoTen = mHoTen;
        this.mMSHS = mMSHS;
        this.mNamSinh = mNamSinh;
        this.mGioiTinh = mGioiTinh;
        this.mDanToc = mDanToc;
        this.mNoiSinh = mNoiSinh;
        this.mChucVu = mChucVu;
        this.mSdtPh = mSdtPh;
    }

    public String getmLinkPhoto() {
        return mLinkPhoto;
    }

    public void setmLinkPhoto(String mLinkPhoto) {
        this.mLinkPhoto = mLinkPhoto;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmHoTen() {
        return mHoTen;
    }

    public void setmHoTen(String mHoTen) {
        this.mHoTen = mHoTen;
    }

    public int getmMSHS() {
        return mMSHS;
    }

    public void setmMSHS(int mMSHS) {
        this.mMSHS = mMSHS;
    }

    public String getmNamSinh() {
        return mNamSinh;
    }

    public void setmNamSinh(String mNamSinh) {
        this.mNamSinh = mNamSinh;
    }

    public Boolean getmGioiTinh() {
        return mGioiTinh;
    }

    public void setmGioiTinh(Boolean mGioiTinh) {
        this.mGioiTinh = mGioiTinh;
    }

    public String getmDanToc() {
        return mDanToc;
    }

    public void setmDanToc(String mDanToc) {
        this.mDanToc = mDanToc;
    }

    public String getmNoiSinh() {
        return mNoiSinh;
    }

    public void setmNoiSinh(String mNoiSinh) {
        this.mNoiSinh = mNoiSinh;
    }

    public String getmChucVu() {
        return mChucVu;
    }

    public void setmChucVu(String mChucVu) {
        this.mChucVu = mChucVu;
    }

    public String getmSdtPh() {
        return mSdtPh;
    }

    public void setmSdtPh(String mSdtPh) {
        this.mSdtPh = mSdtPh;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmGhiChu() {
        return mGhiChu;
    }

    public void setmGhiChu(String mGhiChu) {
        this.mGhiChu = mGhiChu;
    }

    //TODO Sắp xếp
    public static Comparator<HocSinh> AZ_HocSinh = new Comparator<HocSinh>() {
        @Override
        public int compare(HocSinh o1, HocSinh o2) {
            return o1.getmHoTen().compareTo(o2.getmHoTen());
        }
    };
}
