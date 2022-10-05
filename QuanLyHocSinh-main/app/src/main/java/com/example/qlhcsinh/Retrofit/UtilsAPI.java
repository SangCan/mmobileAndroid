package com.example.qlhcsinh.Retrofit;

public class UtilsAPI {
    public static final String BaseUrl = "http://172.16.1.66/QLHS/";
    public static DataClient getData(){
        return RetrofitClient.getClient(BaseUrl).create(DataClient.class);
    }
}
