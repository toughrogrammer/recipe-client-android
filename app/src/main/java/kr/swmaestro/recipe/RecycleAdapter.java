package kr.swmaestro.recipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import kr.swmaestro.recipe.model.Recipe;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    private static final int COUNT = 100;
    private ImageLoader mImageLoader;
    private List<Recipe> list;

    public RecycleAdapter(List<Recipe> item) {
        mImageLoader = AppController.getInstance().getImageLoader();
        this.list = item;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        setItem(holder, position);
    }



    private void setItem(RecyclerHolder holder, int position) {
        Recipe recipe = list.get(position);
        holder.mRecycleHolder.mTitle.setText(recipe.getTitle());

        holder.mRecycleHolder.mImage.setImageUrl(recipe.getImageurl(), mImageLoader);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_row, parent, false);
        return new RecyclerHolder(view);
    }

}
