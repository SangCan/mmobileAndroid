package com.example.qlhcsinh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.Object.HocSinh;
import com.example.qlhcsinh.Object.OnClickItemHS;
import com.example.qlhcsinh.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterHocSinh extends RecyclerView.Adapter<AdapterHocSinh.ViewHolderUser> {
    private List<HocSinh> mHocSinhs;
    ProgressBar progressBar;

    //TODO onClick item HS
    private OnClickItemHS onClickItemHS;
    public void setOnClickItemHS(OnClickItemHS onClickItemHS) {
        this.onClickItemHS = onClickItemHS;
    }

    public void setmHocSinhs(List<HocSinh> mHocSinhs, ProgressBar progressBar) {
        this.mHocSinhs = mHocSinhs;
        this.progressBar = progressBar;
    }

    public AdapterHocSinh() {
    }

    public AdapterHocSinh(List<HocSinh> mHocSinhs, ProgressBar progressBar) {
        this.mHocSinhs = mHocSinhs;
        notifyDataSetChanged();
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hs, parent, false);
        return new ViewHolderUser(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser holder, int position) {
        HocSinh hocSinh = mHocSinhs.get(position);
        if (hocSinh == null){
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        holder.txNoiO.setText(hocSinh.getmChucVu());
        holder.txTen.setText(hocSinh.getmHoTen());
        if (hocSinh.getmLinkPhoto().equals("matdinhnam")){
            holder.civHinh.setImageResource(R.drawable.nam);
        }else if (hocSinh.getmLinkPhoto().equals("matdinhnu")){
            holder.civHinh.setImageResource(R.drawable.nu);
        }else {
            if (hocSinh.getmGioiTinh()){
                Picasso.get().load(hocSinh.getmLinkPhoto()).error(R.drawable.nam).into(holder.civHinh);
            }else Picasso.get().load(hocSinh.getmLinkPhoto()).error(R.drawable.nu).into(holder.civHinh);
        }
        progressBar.setVisibility(View.INVISIBLE);

        //TODO Bắt sự kiện onClick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemHS.onClick(hocSinh);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickItemHS.onLongClick(hocSinh);
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        if (mHocSinhs != null){
            return mHocSinhs.size();
        }
        progressBar.setVisibility(View.INVISIBLE);
        return 0;
    }
    public class ViewHolderUser extends RecyclerView.ViewHolder {
        CircleImageView civHinh;
        TextView txTen, txNoiO;
        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            civHinh = (CircleImageView) itemView.findViewById(R.id.itHinh);
            txTen = (TextView) itemView.findViewById(R.id.itTen);
            txNoiO = (TextView) itemView.findViewById(R.id.itChucVu);
        }
    }
}
