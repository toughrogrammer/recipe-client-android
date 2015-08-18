package kr.swmaestro.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;

import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.Request.LikeRequest;
import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.ui.MainActivity;
import kr.swmaestro.recipe.ui.RecipeActivity;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerHolder>{

    private ImageLoader mImageLoader;
    private List<Recipe> list;
    private Context context;

    public RecycleAdapter(List<Recipe> item, Context context) {
        mImageLoader = AppController.getInstance().getImageLoader();
        this.list = item;
        this.context = context;
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

    private void setItem(final RecyclerHolder holder, int position) {
        final Recipe recipe = list.get(position);
        holder.mRecycleHolder.mTitle.setText(recipe.getTitle());
        holder.mRecycleHolder.mImage.setImageUrl(recipe.getImageurl(), mImageLoader);
        if(!recipe.getWasLike().equals(""))
            holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLUE);
        holder.mRecycleHolder.mlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = recipe.getItemId();
                if (recipe.getWasLike().equals("")) {     //TODO 중복 라이크 취소 기능 구현 예정
                    SharedPreferences pref = context.getSharedPreferences("pref", 0);
                    String token = pref.getString("token", "NON");  // get Token
                    String userid = pref.getString("id", "NON");  // get Token
                    LikeRequest recipeRequest = new LikeRequest(id, userid, token, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("like", "Success");
                            try {
                                holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLUE);
                                Log.i("like", response.getString("id"));
                                recipe.setWasLike(response.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("volley", error.toString());
                        }
                    });

                    AppController.getInstance().addToRequestQueue(recipeRequest);
                }else{
                    SharedPreferences pref = context.getSharedPreferences("pref", 0);
                    String token = pref.getString("token", "NON");  // get Token
                    String userid = pref.getString("id", "NON");  // get Token
                    JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.DELETE, "http://recipe-main.herokuapp.com/likes/" +recipe.getWasLike(), token, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Cancle like", "Success");
                            holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLACK);
                            recipe.setWasLike("");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("volley", error.toString());
                        }
                    });
                    AppController.getInstance().addToRequestQueue(recipeRequest);
                }
            }
        });

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
