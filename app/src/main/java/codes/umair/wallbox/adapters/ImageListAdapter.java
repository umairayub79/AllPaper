package codes.umair.wallbox.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import codes.umair.wallbox.R;
import codes.umair.wallbox.models.Post;


/*
 Created by Umair Ayub on 17 Sept 2019.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.PostViewHolder> {
    private Context context;
    private List<Post> hits;

    public ImageListAdapter(Context context, List<Post> hits) {
        this.context = context;
        this.hits = hits;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hit_item, parent, false);
        ;
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.drawable.ic_launcher_background);
        Post hit = hits.get(position);
        Glide.with(context).load(hit.getPreviewURL()).into(holder.image);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return hits.size();
    }

    public void addItem(Post hit) {
        hits.add(hit);
        notifyDataSetChanged();
    }

    public void addItems(List<Post> hits) {
        hits.addAll(hits);
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public ImageView image;

        public PostViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}