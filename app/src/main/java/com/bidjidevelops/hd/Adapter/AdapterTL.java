package com.bidjidevelops.hd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidjidevelops.hd.Comment;
import com.bidjidevelops.hd.Gson.GsonTL;
import com.bidjidevelops.hd.Helper;
import com.bidjidevelops.hd.MainActivity;
import com.bidjidevelops.hd.R;
import com.bidjidevelops.hd.TimelineMain;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by You on 19/04/2017.
 */

public class AdapterTL extends RecyclerView.Adapter<AdapterTL.ViewHolder> {
    Context context;
    public List<GsonTL.Soal> dataSoal;

    public AdapterTL(FragmentActivity activity, List<GsonTL.Soal> dataSoal) {

        context = activity;
        this.dataSoal = dataSoal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timeline, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterTL.ViewHolder holder, final int position) {
        holder.txtFeed.setText(dataSoal.get(position).pertanyaan);
        holder.txtTingkat.setText(dataSoal.get(position).school);
        holder.txtUser.setText(dataSoal.get(position).username);
        holder.txtTanggal.setText(dataSoal.get(position).waktuSoal);
        Glide.with(context)
                .load(Helper.BASE_IMGUS+dataSoal.get(position).image)
                .crossFade()
                .placeholder(R.drawable.student)
                .into(holder.imguser);
        Glide.with(context)
                .load(Helper.BASE_IMGUS+dataSoal.get(position).gbr_pertanyaan)
                .crossFade()
                .into(holder.ivContent);
        holder.ivContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(view.getContext(),Comment.class);
                a.putExtra("id_pertanyaan",dataSoal.get(position).idpertanyaan);
                a.putExtra("username",dataSoal.get(position).username);
                a.putExtra("sekolah",dataSoal.get(position).school);
                a.putExtra("pertanyaan",dataSoal.get(position).pertanyaan);
                a.putExtra("image_user",dataSoal.get(position).image);
                a.putExtra("gbr_pertanyaan",dataSoal.get(position).gbr_pertanyaan);
                a.putExtra("waktuSoal",dataSoal.get(position).waktuSoal);
                a.putExtra("id_user",dataSoal.get(position).idUser);
                a.putExtra("idpertanyaan",dataSoal.get(position).idpertanyaan);
                view.getContext().startActivity(a);
            }
        });
        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(view.getContext(),Comment.class);
                a.putExtra("id_pertanyaan",dataSoal.get(position).idpertanyaan);
                a.putExtra("username",dataSoal.get(position).username);
                a.putExtra("sekolah",dataSoal.get(position).school);
                a.putExtra("pertanyaan",dataSoal.get(position).pertanyaan);
                a.putExtra("image_user",dataSoal.get(position).image);
                a.putExtra("gbr_pertanyaan",dataSoal.get(position).gbr_pertanyaan);
                a.putExtra("waktuSoal",dataSoal.get(position).waktuSoal);
                a.putExtra("id_user",dataSoal.get(position).idUser);
                a.putExtra("idpertanyaan",dataSoal.get(position).idpertanyaan);
                view.getContext().startActivity(a);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSoal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUser, txtTingkat, txtFeed,txtTanggal;
        ImageView ivContent, ivComment,imguser;
        public ViewHolder(View itemView) {
            super(itemView);
            txtFeed = (TextView) itemView.findViewById(R.id.txt_feed);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggal);
            txtTingkat = (TextView) itemView.findViewById(R.id.txtTingkat);
            txtUser = (TextView) itemView.findViewById(R.id.txtUser);
            ivContent = (ImageView) itemView.findViewById(R.id.img_content);
            ivComment = (ImageView) itemView.findViewById(R.id.imgComment);
            imguser = (ImageView) itemView.findViewById(R.id.imguser);
        }
    }
}
