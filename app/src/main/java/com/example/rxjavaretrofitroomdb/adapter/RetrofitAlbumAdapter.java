package com.example.rxjavaretrofitroomdb.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rxjavaretrofitroomdb.R;
import com.example.rxjavaretrofitroomdb.data.RetrofitAlbum;

import java.util.ArrayList;
import java.util.List;

public class RetrofitAlbumAdapter extends RecyclerView.Adapter<RetrofitAlbumAdapter.ViewHolder> {

    private static final String TAG = RetrofitAlbumAdapter.class.getSimpleName();
    List<RetrofitAlbum> albumList = new ArrayList();

    public RetrofitAlbumAdapter(List<RetrofitAlbum> list){
        albumList.addAll(list);
    }

    public void loadAlbumList(List<RetrofitAlbum> list){
        Log.d(TAG,"loadAlbumList list size: "+list.size());
        albumList.clear();
        albumList.addAll(list);
        Log.d(TAG,"loadAlbumList albumList size: "+albumList.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.retorfit_album,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.albumTitle.setText(albumList.get(position).title);
        holder.albumIcon.setImageDrawable(holder.albumIcon.getContext().getResources().getDrawable(R.drawable.ic_launcher_background,null));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView albumTitle;
        ImageView albumIcon;
        public ViewHolder(View v){
            super(v);
            albumIcon = (ImageView)v.findViewById(R.id.album_icon);
            albumTitle = (TextView)v.findViewById(R.id.album_title);
        }
    }
}
