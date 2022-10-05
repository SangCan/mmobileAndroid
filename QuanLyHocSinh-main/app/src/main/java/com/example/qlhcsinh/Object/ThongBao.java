package com.example.qlhcsinh.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

public class ThongBao implements Serializable {
    @SerializedName("ID")
    @Expose
    private int mID;

    @SerializedName("GV_NhaTruong")
    @Expose
    private String mGV_NhaTruong;

    @SerializedName("Ten")
    @Expose
    private String mTen;

    @SerializedName("NoiDung")
    @Expose
    private String mNoiDung;

    @SerializedName("NgayDang")
    @Expose
    private String mNgayDang;

    public ThongBao(int mID, String mGV_NhaTruong, String mTen, String mNoiDung, String mNgayDang) {
        this.mID = mID;
        this.mGV_NhaTruong = mGV_NhaTruong;
        this.mTen = mTen;
        this.mNoiDung = mNoiDung;
        this.mNgayDang = mNgayDang;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmGV_NhaTruong() {
        return mGV_NhaTruong;
    }

    public void setmGV_NhaTruong(String mGV_NhaTruong) {
        this.mGV_NhaTruong = mGV_NhaTruong;
    }

    public String getmTen() {
        return mTen;
    }

    public void setmTen(String mTen) {
        this.mTen = mTen;
    }

    public String getmNoiDung() {
        return mNoiDung;
    }

    public void setmNoiDung(String mNoiDung) {
        this.mNoiDung = mNoiDung;
    }

    public String getmNgayDang() {
        return mNgayDang;
    }

    public void setmNgayDang(String mNgayDang) {
        this.mNgayDang = mNgayDang;
    }
    //TODO Sắp xếp theo thứ tự
    public static Comparator<ThongBao> AZ_ThongBao = new Comparator<ThongBao>() {
        @Override
        public int compare(ThongBao o1, ThongBao o2) {
            return o2.mID - o1.mID;
        }
    };
}
