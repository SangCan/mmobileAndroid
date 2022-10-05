package com.example.qlhcsinh.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.Adapter.AdapterThongBao;
import com.example.qlhcsinh.MainActivity_Info_HocSinh;
import com.example.qlhcsinh.Object.ThongBao;
import com.example.qlhcsinh.R;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentThongBao extends Fragment {
    static RecyclerView Recly_ThongBao;
    static AdapterThongBao adapter;
    static List<ThongBao> mThongBaos;
    View view;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thong_bao, container, false);
        Anhxa();

        //TODO Set item HS cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Recly_ThongBao.setLayoutManager(layoutManager);
        Recly_ThongBao.setHasFixedSize(true);
        Recly_ThongBao.setItemViewCacheSize(20);

        //TODO Get TB từ sever
        GetTB(MainActivity_Info_HocSinh.userLogin.getmMSL());

        return view;
    }
    private void Anhxa(){
        Recly_ThongBao = view.findViewById(R.id.Recly_ThongBao);
        adapter = new AdapterThongBao(MainActivity_Info_HocSinh.userLogin.ismGV_PH(), getContext());
    }

    //TODO Get ThongBao từ Sever
    public static void GetTB(int MSL){
        Call<List<ThongBao>> callBack = MainActivity_Info_HocSinh.dataClient.GetTB(MSL);
        callBack.enqueue(new Callback<List<ThongBao>>() {
            @Override
            public void onResponse(Call<List<ThongBao>> call, Response<List<ThongBao>> response) {
                if (response != null){
                    mThongBaos = response.body();
                    Collections.sort(mThongBaos, ThongBao.AZ_ThongBao);
                    adapter.setmThongBaos(mThongBaos);
                    Recly_ThongBao.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ThongBao>> call, Throwable t) {

            }
        });
    }
}
