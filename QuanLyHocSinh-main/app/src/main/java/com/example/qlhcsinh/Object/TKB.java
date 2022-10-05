package com.example.qlhcsinh.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

public class TKB implements Serializable {
    @SerializedName("ID")
    @Expose
    private int mID;

    @SerializedName("MSL")
    @Expose
    private int mMSL;

    @SerializedName("Thu")
    @Expose
    private int mThu;

    @SerializedName("Sang1")
    @Expose
    private String mSang1;

    @SerializedName("Sang2")
    @Expose
    private String mSang2;

    @SerializedName("Sang3")
    @Expose
    private String mSang3;

    @SerializedName("Sang4")
    @Expose
    private String mSang4;

    @SerializedName("Sang5")
    @Expose
    private String mSang5;

    @SerializedName("Chieu1")
    @Expose
    private String mChieu1;

    @SerializedName("Chieu2")
    @Expose
    private String mChieu2;

    @SerializedName("Chieu3")
    @Expose
    private String mChieu3;

    @SerializedName("Chieu4")
    @Expose
    private String mChieu4;

    @SerializedName("Chieu5")
    @Expose
    private String mChieu5;

    public TKB(int mMSL, int mThu, String mSang1, String mSang2, String mSang3, String mSang4, String mSang5, String mChieu1, String mChieu2, String mChieu3, String mChieu4, String mChieu5) {
        this.mMSL = mMSL;
        this.mThu = mThu;
        this.mSang1 = mSang1;
        this.mSang2 = mSang2;
        this.mSang3 = mSang3;
        this.mSang4 = mSang4;
        this.mSang5 = mSang5;
        this.mChieu1 = mChieu1;
        this.mChieu2 = mChieu2;
        this.mChieu3 = mChieu3;
        this.mChieu4 = mChieu4;
        this.mChieu5 = mChieu5;
    }

    public int getmMSL() {
        return mMSL;
    }

    public void setmMSL(int mMSL) {
        this.mMSL = mMSL;
    }

    public int getmThu() {
        return mThu;
    }

    public void setmThu(int mThu) {
        this.mThu = mThu;
    }

    public String getmSang1() {
        return mSang1;
    }

    public void setmSang1(String mSang1) {
        this.mSang1 = mSang1;
    }

    public String getmSang2() {
        return mSang2;
    }

    public void setmSang2(String mSang2) {
        this.mSang2 = mSang2;
    }

    public String getmSang3() {
        return mSang3;
    }

    public void setmSang3(String mSang3) {
        this.mSang3 = mSang3;
    }

    public String getmSang4() {
        return mSang4;
    }

    public void setmSang4(String mSang4) {
        this.mSang4 = mSang4;
    }

    public String getmSang5() {
        return mSang5;
    }

    public void setmSang5(String mSang5) {
        this.mSang5 = mSang5;
    }

    public String getmChieu1() {
        return mChieu1;
    }

    public void setmChieu1(String mChieu1) {
        this.mChieu1 = mChieu1;
    }

    public String getmChieu2() {
        return mChieu2;
    }

    public void setmChieu2(String mChieu2) {
        this.mChieu2 = mChieu2;
    }

    public String getmChieu3() {
        return mChieu3;
    }

    public void setmChieu3(String mChieu3) {
        this.mChieu3 = mChieu3;
    }

    public String getmChieu4() {
        return mChieu4;
    }

    public void setmChieu4(String mChieu4) {
        this.mChieu4 = mChieu4;
    }

    public String getmChieu5() {
        return mChieu5;
    }

    public void setmChieu5(String mChieu5) {
        this.mChieu5 = mChieu5;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    //TODO Sắp xếp
    public static Comparator<TKB> AZ_TKB = new Comparator<TKB>() {
        @Override
        public int compare(TKB o1, TKB o2) {
            return o1.getmThu() - o2.getmThu();
        }
    };
}
