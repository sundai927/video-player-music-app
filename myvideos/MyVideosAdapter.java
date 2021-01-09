package edu.ds4jb.videoplayer.myvideos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.videos.VideoItemsViewHolder;

//import edu.ds4jb.videoplayer.models.User;

public class MyVideosAdapter extends RecyclerView.Adapter<MyVideoItemsViewHolder> {

    private final List<Video> videos = new ArrayList<>();


    @NonNull
    @Override
    public MyVideoItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myvideo, parent, false);
        return new MyVideoItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoItemsViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }



    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void updateVideos(List<Video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }
}
