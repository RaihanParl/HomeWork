package com.bidjidevelops.hd.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidjidevelops.hd.Comment;
import com.bidjidevelops.hd.Gson.GsonComment;
import com.bidjidevelops.hd.Gson.GsonTL;
import com.bidjidevelops.hd.Gson.GsonUser;
import com.bidjidevelops.hd.Helper;
import com.bidjidevelops.hd.MainActivity;
import com.bidjidevelops.hd.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by You on 20/04/2017.
 */

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    Context context;
    public List<GsonComment.Commentar> DataComment;

    public AdapterComment(Comment listener, List<GsonComment.Commentar> DataComment) {

        context = listener;
        this.DataComment = DataComment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterComment.ViewHolder holder, int position) {
        holder.txtcoment_coment.setText(DataComment.get(position).commentarnya);
        holder.txtsekolah_comment.setText(DataComment.get(position).school);
        holder.txtusername_comment.setText(DataComment.get(position).username); Glide.with(context)
                .load(Helper.BASE_IMGUS+DataComment.get(position).image)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imguser_comment);
    }

    @Override
    public int getItemCount() {
      return DataComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtusername_comment, txtsekolah_comment, txtcoment_coment;
        ImageView imguser_comment, ivComment, imguser;

        public ViewHolder(View itemView) {
            super(itemView);
            txtcoment_coment = (TextView) itemView.findViewById(R.id.txtcoment_coment);
            txtsekolah_comment = (TextView) itemView.findViewById(R.id.txtsekolah_comment);
            txtusername_comment = (TextView) itemView.findViewById(R.id.txtusername_comment);
            imguser_comment = (ImageView) itemView.findViewById(R.id.imguser_comment);
        }
    }
}
