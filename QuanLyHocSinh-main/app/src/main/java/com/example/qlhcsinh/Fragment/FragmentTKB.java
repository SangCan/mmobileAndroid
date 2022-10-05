package com.example.qlhcsinh.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.Adapter.AdapterHocSinh;
import com.example.qlhcsinh.Adapter.AdapterTKB;
import com.example.qlhcsinh.MainActivity;
import com.example.qlhcsinh.MainActivity_Info_HocSinh;
import com.example.qlhcsinh.MainActivity_setTKB;
import com.example.qlhcsinh.Object.OnClickItemTKB;
import com.example.qlhcsinh.Object.TKB;
import com.example.qlhcsinh.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTKB extends Fragment {
    public static RecyclerView Recly_TKB;
    public static AdapterTKB adapter;
    public static List<TKB> mTKBs;
    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tkb, container, false);
        Anhxa();

        //TODO Set item HS cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());//dạng list
        Recly_TKB.setLayoutManager(layoutManager);
        Recly_TKB.setHasFixedSize(true);
        Recly_TKB.setItemViewCacheSize(7);
        //TODO lấy tkb từ sever vsf set vào RecyclerView
        GetTKB();

        //TODO bắt sự kiện onClick ic_Edit
        adapter.setOnClickItemTKB(new OnClickItemTKB() {
            @Override
            public void onClick(TKB tkb) {
                if (MainActivity_Info_HocSinh.userLogin.ismGV_PH()){
                    Intent intent = new Intent(getContext(), MainActivity_setTKB.class);
                    intent.putExtra("Key_TKB", tkb);
                    startActivity(intent);
                }else Toast.makeText(getContext(), "P/H không thể sử dụng!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void Anhxa(){
        Recly_TKB = view.findViewById(R.id.Recly_TKB);
        mTKBs = new ArrayList<>();
        adapter = new AdapterTKB();
    }

    //TODO GetTKB
    public static void GetTKB(){
        Call<List<TKB>> callBack = MainActivity_Info_HocSinh.dataClient.GetTKB(MainActivity_Info_HocSinh.userLogin.getmMSL());
        callBack.enqueue(new Callback<List<TKB>>() {
            @Override
            public void onResponse(Call<List<TKB>> call, Response<List<TKB>> response) {
                if (response != null){
                    mTKBs = response.body();
                    Collections.sort(mTKBs, TKB.AZ_TKB);
                    adapter.setmTKB(mTKBs);
                    Recly_TKB.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TKB>> call, Throwable t) {

            }
        });
    }
}
