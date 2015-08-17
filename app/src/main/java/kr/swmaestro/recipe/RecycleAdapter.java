package kr.swmaestro.recipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.ui.MainActivity;
import kr.swmaestro.recipe.ui.RecipeActivity;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerHolder> {

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
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        setItem(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), RecipeActivity.class);
                intent.putExtra("id",list.get(position).getItemId()+"");
                Log.i("id", list.get(position).getItemId()+"");
                view.getContext().startActivity(intent);
            }
        });
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
