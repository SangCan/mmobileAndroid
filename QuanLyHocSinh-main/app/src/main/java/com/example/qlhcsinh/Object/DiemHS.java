package com.example.qlhcsinh.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class DiemHS {
    @SerializedName("ID")
    @Expose
    private int mID;

    @SerializedName("KeyID")
    @Expose
    private int mKeyID;

    @SerializedName("TenMonHoc")
    @Expose
    private String mTenMonHoc;

    @SerializedName("D_Mieng")
    @Expose
    private double mD_Mieng;

    @SerializedName("D_15p")
    @Expose
    private double mD_15p;

    @SerializedName("D_1Tiet")
    @Expose
    private double mD_1Tiet;

    @SerializedName("D_HocKy")
    @Expose
    private double mD_HocKy;

    public DiemHS(int mKeyID, String mTenMonHoc, int mD_Mieng, int mD_15p, int mD_1Tiet, int mD_HocKy) {
        this.mKeyID = mKeyID;
        this.mTenMonHoc = mTenMonHoc;
        this.mD_Mieng = mD_Mieng;
        this.mD_15p = mD_15p;
        this.mD_1Tiet = mD_1Tiet;
        this.mD_HocKy = mD_HocKy;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getmKeyID() {
        return mKeyID;
    }

    public void setmKeyID(int mKeyID) {
        this.mKeyID = mKeyID;
    }

    public String getmTenMonHoc() {
        return mTenMonHoc;
    }

    public void setmTenMonHoc(String mTenMonHoc) {
        this.mTenMonHoc = mTenMonHoc;
    }

    public double getmD_Mieng() {
        return mD_Mieng;
    }

    public void setmD_Mieng(double mD_Mieng) {
        this.mD_Mieng = mD_Mieng;
    }

    public double getmD_15p() {
        return mD_15p;
    }

    public void setmD_15p(double mD_15p) {
        this.mD_15p = mD_15p;
    }

    public double getmD_1Tiet() {
        return mD_1Tiet;
    }

    public void setmD_1Tiet(double mD_1Tiet) {
        this.mD_1Tiet = mD_1Tiet;
    }

    public double getmD_HocKy() {
        return mD_HocKy;
    }

    public void setmD_HocKy(double mD_HocKy) {
        this.mD_HocKy = mD_HocKy;
    }

    //TODO Sắp xếp theo thứ tự
    public static Comparator<DiemHS> AZ_DiemHS = new Comparator<DiemHS>() {
        @Override
        public int compare(DiemHS o1, DiemHS o2) {
            return o1.getmID() - o2.getmID();
        }
    };
}
