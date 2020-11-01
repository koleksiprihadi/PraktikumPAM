package com.example.praktikumpam;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BiodataAdapter extends RecyclerView.Adapter<BiodataAdapter.BiodataViewHolder> {


    private ArrayList<Biodata> dataList;

    public BiodataAdapter(ArrayList<Biodata> dataList) {
        this.dataList = dataList;
    }

    @Override
    public BiodataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.biodata, parent, false);
        return new BiodataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BiodataViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtJeniskelamin.setText(dataList.get(position).getJeniskelamin());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class BiodataViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtJeniskelamin;

        public BiodataViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtJeniskelamin = (TextView) itemView.findViewById(R.id.txt_jeniskelamin);

        }
    }
}