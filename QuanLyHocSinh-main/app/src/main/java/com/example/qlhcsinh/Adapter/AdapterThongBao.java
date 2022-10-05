package com.example.qlhcsinh.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.Fragment.FragmentThongBao;
import com.example.qlhcsinh.MainActivity_AddThongBao;
import com.example.qlhcsinh.MainActivity_Info_HocSinh;
import com.example.qlhcsinh.Object.ThongBao;
import com.example.qlhcsinh.R;
import com.ramotion.foldingcell.FoldingCell;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterThongBao extends RecyclerView.Adapter<AdapterThongBao.ViewHolderTB> {
    List<ThongBao> mThongBaos;
    boolean User;
    Context mContext;

    public AdapterThongBao(boolean user, Context context) {
        User = user;
        mContext = context;
    }

    public void setmThongBaos(List<ThongBao> mThongBaos) {
        this.mThongBaos = mThongBaos;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderTB onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thongbao, parent, false);
        return new ViewHolderTB(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterThongBao.ViewHolderTB holder, int position) {
        ThongBao thongBao = mThongBaos.get(position);
        if (thongBao == null){
            return;
        }
        holder.it_Title_Ten.setText(thongBao.getmTen());
        holder.it_Title_Ngay.setText(thongBao.getmNgayDang());
        holder.it_Title_Nguon.setText(thongBao.getmGV_NhaTruong());
        holder.it_Mes_Ten.setText(thongBao.getmTen());
        holder.it_Mes_Ngay.setText(thongBao.getmNgayDang());
        holder.it_Mes_Nguon.setText(thongBao.getmGV_NhaTruong());
        holder.it_Mes_NoiDung.setText(thongBao.getmNoiDung());
        holder.mFoldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mFoldingCell.toggle(false);
            }
        });

        if (!User){
            holder.it_Title_Info.setVisibility(View.INVISIBLE);
        }else {
            holder.it_Title_Info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogXoaSuaTB(thongBao);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mThongBaos != null){
            return mThongBaos.size();
        }
        return 0;
    }

    public class ViewHolderTB extends RecyclerView.ViewHolder {
        TextView it_Mes_Nguon, it_Mes_Ngay, it_Mes_Ten, it_Mes_NoiDung, it_Title_Nguon, it_Title_Ten, it_Title_Ngay;
        FoldingCell mFoldingCell;
        ImageButton it_Title_Info;
        public ViewHolderTB(@NonNull @NotNull View itemView) {
            super(itemView);
            it_Mes_Nguon = itemView.findViewById(R.id.it_Mes_Nguon);
            it_Mes_Ngay = itemView.findViewById(R.id.it_Mes_Ngay);
            it_Mes_Ten = itemView.findViewById(R.id.it_Mes_Ten);
            it_Mes_NoiDung = itemView.findViewById(R.id.it_Mes_NoiDung);
            it_Title_Nguon = itemView.findViewById(R.id.it_Title_Nguon);
            it_Title_Ten = itemView.findViewById(R.id.it_Title_Ten);
            it_Title_Ngay = itemView.findViewById(R.id.it_Title_Ngay);
            mFoldingCell = itemView.findViewById(R.id.mFoldingCell);
            it_Title_Info = itemView.findViewById(R.id.it_Title_Info);
        }
    }

    private void DialogXoaSuaTB(ThongBao thongBao) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("Xin chào!");
        dialog.setMessage("Bạn muốn thực hiện: \n-Thông báo: " + thongBao.getmTen());
        dialog.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XoaTB(thongBao.getmID());
            }
        });
        dialog.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, MainActivity_AddThongBao.class);
                intent.putExtra("Key_ThongBao", thongBao);
                mContext.startActivity(intent);
            }
        });
        dialog.show();
    }

    //TODO Xóa TB
    private void XoaTB(int ID){
        Call<String> callBack = MainActivity_Info_HocSinh.dataClient.XoaTB(ID);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null){
                    Toast.makeText(mContext, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    FragmentThongBao.GetTB(MainActivity_Info_HocSinh.userLogin.getmMSL());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(mContext, "Lỗi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
