package io.github.oskuda.readit;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsArticleRecyclerViewAdapter extends RecyclerView.Adapter<NewsArticleRecyclerViewAdapter.NewsArticleViewHolder>{
    private static final String TAG = "NewsArticleRecyclerView";
    private List<NewsArticle> mNewsArticleList;

    private Context mContext;


    public NewsArticleRecyclerViewAdapter(Context mContext,List<NewsArticle> newsArticleList) {
        this.mContext = mContext;
        mNewsArticleList = newsArticleList;
    }

    @NonNull
    @Override
    public NewsArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        // Called by layout manager when it needs new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_items,parent,false);
        return new NewsArticleViewHolder(mContext,view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsArticleViewHolder holder, int position) {
        //called by recycler view when it wants new data to store on viewHolder
        //called by layout manager  when it wants new data in an existing row

        NewsArticle newsArticle = mNewsArticleList.get(position);
        Log.d(TAG, "onBindViewHolder: "+newsArticle.getHeadline()+" ==> "+position);

        holder.headline.setText(newsArticle.getHeadline());

        Picasso.get()
                .load(newsArticle.getImageUri())
                .resize(400,300)
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mNewsArticleList!=null)&&(mNewsArticleList.size()!=0)?mNewsArticleList.size():0);
    }
    public void loadNewData(ArrayList<NewsArticle> newsArticleList){
        mNewsArticleList = newsArticleList;
        notifyDataSetChanged();
    }

    static public class NewsArticleViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "NewsArticleViewHolder";

        ImageView thumbnail;
        TextView headline;

        public NewsArticleViewHolder(Context mContext,@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "NewsArticleViewHolder: starts");


            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.headline = itemView.findViewById(R.id.headline);

            Typeface MMedium = Typeface.createFromAsset(mContext.getAssets(),"fonts/Montserrat-Medium.ttf");

            headline.setTypeface(MMedium);




            Log.d(TAG, "NewsArticleViewHolder: ends");
        }
    }
}
