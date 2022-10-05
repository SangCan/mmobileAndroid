package com.example.qlhcsinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.MainActivity_Sua_Diem;
import com.example.qlhcsinh.Object.DiemHS;
import com.example.qlhcsinh.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterDiemHS extends RecyclerView.Adapter<AdapterDiemHS.ViewHolderDiemHS> {
    List<DiemHS> mDiemHS;
    boolean CheckGV_PH;
    Context context;
    String TenHS;
    public AdapterDiemHS(boolean CheckGV_PH, Context context, String TenHS) {
        this.CheckGV_PH = CheckGV_PH;
        this.context = context;
        this.TenHS = TenHS;
    }

    public void setmDiemHS(List<DiemHS> mDiemHS) {
        this.mDiemHS = mDiemHS;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDiemHS onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diem_hs, parent, false);
        return new ViewHolderDiemHS(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterDiemHS.ViewHolderDiemHS holder, int position) {
        DiemHS diemHS = mDiemHS.get(position);
        //TODO Check GV/PH để ẩn ImageButton edit
        if (CheckGV_PH){
            holder.it_D_HS_Sua.setVisibility(View.VISIBLE);
        }else holder.it_D_HS_Sua.setVisibility(View.INVISIBLE);

        if (position > 0){
            //TODO set Background
            if (position % 2 == 0){
                holder.itemView.setBackgroundColor(Color.parseColor("#C0E0FA"));
            }else holder.itemView.setBackgroundColor(Color.parseColor("#D7FAF1D8"));

            holder.STT.setText(position + "");
            holder.TenMonHoc.setText(diemHS.getmTenMonHoc());

            //TODO set Diểm lên layout
            holder.D_Mieng.setText(DinhDang(diemHS.getmD_Mieng()));
            holder.D_15p.setText(DinhDang(diemHS.getmD_15p()));
            holder.D_1Tiet.setText(DinhDang(diemHS.getmD_1Tiet()));
            holder.D_HocKy.setText(DinhDang(diemHS.getmD_HocKy()));

            //TODO Tính điểm TB
            if (diemHS.getmD_Mieng() > 0 && diemHS.getmD_15p() > 0 && diemHS.getmD_1Tiet() > 0 && diemHS.getmD_HocKy() >0){
                double D_Mieng = diemHS.getmD_Mieng();
                double D_15p = diemHS.getmD_15p();
                double D_1Tiet = diemHS.getmD_1Tiet();
                double D_HocKy = diemHS.getmD_HocKy();
                double D_TB = (D_Mieng + D_15p + (D_1Tiet * 2) + (D_HocKy * 3)) / 7;
                D_TB = Math.ceil(D_TB * 10) / 10;
                holder.D_TKBM.setText(D_TB + "");
            }
        }else{
            //TODO set Background
            holder.itemView.setBackgroundColor(Color.parseColor("#60B5FA"));

            //TODO set cứng item giá trị bằng 0
            holder.it_D_HS_Sua.setVisibility(View.INVISIBLE);
            holder.STT.setText("STT");
            holder.TenMonHoc.setText("Tên Môn Học");
            holder.D_Mieng.setText("Điểm Miệng");
            holder.D_15p.setText("Điểm 15p");
            holder.D_1Tiet.setText("Điểm 1 Tiết");
            holder.D_HocKy.setText("Điểm Học Kỳ");
            holder.D_TKBM.setText("Điểm TB");
        }

        //todo Chuyển Activity Sửa
        holder.it_D_HS_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity_Sua_Diem.class);
                intent.putExtra("Key_ID", diemHS.getmID());
                intent.putExtra("Key_Ten", TenHS);
                intent.putExtra("Key_Mon", holder.TenMonHoc.getText().toString().trim());

                intent.putExtra("Key_DMieng", diemHS.getmD_Mieng());
                intent.putExtra("Key_D15p", diemHS.getmD_15p());
                intent.putExtra("Key_D1Tiet", diemHS.getmD_1Tiet());
                intent.putExtra("Key_DHocKy", diemHS.getmD_HocKy());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDiemHS != null){
            return mDiemHS.size();
        }
        return 0;
    }

    public class ViewHolderDiemHS extends RecyclerView.ViewHolder {
        TextView STT, TenMonHoc, D_Mieng, D_15p, D_1Tiet, D_HocKy, D_TKBM;
        ImageButton it_D_HS_Sua;
        public ViewHolderDiemHS(@NonNull @NotNull View itemView) {
            super(itemView);
            STT = itemView.findViewById(R.id.it_D_HS_STT);
            TenMonHoc = itemView.findViewById(R.id.it_D_HS_TenMonHoc);
            D_Mieng = itemView.findViewById(R.id.it_D_HS_D_Mieng);
            D_15p = itemView.findViewById(R.id.it_D_HS_D_15p);
            D_1Tiet = itemView.findViewById(R.id.it_D_HS_D_1Tiet);
            D_HocKy = itemView.findViewById(R.id.it_D_HS_D_HocKy);
            D_TKBM = itemView.findViewById(R.id.it_D_HS_D_TBMon);
            it_D_HS_Sua = itemView.findViewById(R.id.it_D_HS_Sua);
        }
    }

    private String DinhDang(double Diem){
        String s = "";
        if (Diem > 0){
            s = String.valueOf(Diem);
        }
        return s;
    }
}
