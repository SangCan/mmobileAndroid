package com.example.qlhcsinh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlhcsinh.Object.OnClickItemTKB;
import com.example.qlhcsinh.Object.TKB;
import com.example.qlhcsinh.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AdapterTKB extends RecyclerView.Adapter<AdapterTKB.ViewHolderTKB>{
    List<TKB> mTKB;

    public void setmTKB(List<TKB> mTKB) {
        this.mTKB = mTKB;
        notifyDataSetChanged();
    }

    public AdapterTKB() {
    }

    //TODO onClick ic sửa
    private OnClickItemTKB onClickItemTKB;
    public void setOnClickItemTKB(OnClickItemTKB onClickItemTKB) {
        this.onClickItemTKB = onClickItemTKB;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderTKB onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tkb, parent, false);
        return new ViewHolderTKB(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterTKB.ViewHolderTKB holder, int position) {
        TKB tkb = mTKB.get(position);
        if (tkb == null){
            return;
        }
        if (tkb.getmThu() == 8){
            holder.itTKB_Thu.setText("Chủ Nhật");
        }else holder.itTKB_Thu.setText("Thứ " + tkb.getmThu());
        holder.itTKB_S1.setText(tkb.getmSang1());
        holder.itTKB_S2.setText(tkb.getmSang2());
        holder.itTKB_S3.setText(tkb.getmSang3());
        holder.itTKB_S4.setText(tkb.getmSang4());
        holder.itTKB_S5.setText(tkb.getmSang5());
        holder.itTKB_C1.setText(tkb.getmChieu1());
        holder.itTKB_C2.setText(tkb.getmChieu2());
        holder.itTKB_C3.setText(tkb.getmChieu3());
        holder.itTKB_C4.setText(tkb.getmChieu4());
        holder.itTKB_C5.setText(tkb.getmChieu5());

        //TODO bắt sự kiện onClick
        holder.itTKB_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemTKB.onClick(tkb);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTKB != null){
            return mTKB.size();
        }
        return 0;
    }

    public class ViewHolderTKB extends RecyclerView.ViewHolder {
        TextView itTKB_Thu, itTKB_S1, itTKB_S2, itTKB_S3, itTKB_S4, itTKB_S5, itTKB_C1, itTKB_C2, itTKB_C3, itTKB_C4, itTKB_C5;
        ImageButton itTKB_Edit;
        public ViewHolderTKB(@NonNull @NotNull View itemView) {
            super(itemView);
            itTKB_Thu = itemView.findViewById(R.id.itTKB_Thu);
            itTKB_S1 = itemView.findViewById(R.id.itTKB_S1);
            itTKB_S2 = itemView.findViewById(R.id.itTKB_S2);
            itTKB_S3 = itemView.findViewById(R.id.itTKB_S3);
            itTKB_S4 = itemView.findViewById(R.id.itTKB_S4);
            itTKB_S5 = itemView.findViewById(R.id.itTKB_S5);
            itTKB_C1 = itemView.findViewById(R.id.itTKB_C1);
            itTKB_C2 = itemView.findViewById(R.id.itTKB_C2);
            itTKB_C3 = itemView.findViewById(R.id.itTKB_C3);
            itTKB_C4 = itemView.findViewById(R.id.itTKB_C4);
            itTKB_C5 = itemView.findViewById(R.id.itTKB_C5);
            itTKB_Edit = itemView.findViewById(R.id.itTKB_Edit);
        }
    }
}
