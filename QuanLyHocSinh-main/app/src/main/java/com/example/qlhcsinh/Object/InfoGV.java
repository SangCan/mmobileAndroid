package com.example.qlhcsinh.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoGV {
    @SerializedName("MSL")
    @Expose
    private int mMSL;

    @SerializedName("Photo1")
    @Expose
    private String mPhoto1;

    @SerializedName("Name")
    @Expose
    private String mName;

    @SerializedName("Gmail")
    @Expose
    private String mGmail;

    @SerializedName("Photo2")
    @Expose
    private String mPhoto2;

    @SerializedName("SDT")
    @Expose
    private String mSDT;

    @SerializedName("TenLop")
    @Expose
    private String mTenLop;

    public String getmTenLop() {
        return mTenLop;
    }

    public void setmTenLop(String mTenLop) {
        this.mTenLop = mTenLop;
    }

    public int getmMSL() {
        return mMSL;
    }

    public void setmMSL(int mMSL) {
        this.mMSL = mMSL;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmGmail() {
        return mGmail;
    }

    public void setmGmail(String mGmail) {
        this.mGmail = mGmail;
    }

    public String getmPhoto2() {
        return mPhoto2;
    }

    public void setmPhoto2(String mPhoto2) {
        this.mPhoto2 = mPhoto2;
    }

    public String getmSDT() {
        return mSDT;
    }

    public void setmSDT(String mSDT) {
        this.mSDT = mSDT;
    }

    public String getmPhoto1() {
        return mPhoto1;
    }

    public void setmPhoto1(String mPhoto1) {
        this.mPhoto1 = mPhoto1;
    }

    public InfoGV(int mMSL, String mPhoto1, String mName, String mGmail, String mPhoto2, String mSDT) {
        this.mMSL = mMSL;
        this.mPhoto1 = mPhoto1;
        this.mName = mName;
        this.mGmail = mGmail;
        this.mPhoto2 = mPhoto2;
        this.mSDT = mSDT;
    }
}
