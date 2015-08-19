package kr.swmaestro.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.Request.LikeRequest;
import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.ui.RecipeActivity;
import kr.swmaestro.recipe.util.util;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    private ImageLoader mImageLoader;
    private List<Recipe> list;
    private Context context;

    private String token;
    private String userid;

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
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_row, parent, false);
        getPreferenceData();
        return new RecyclerHolder(view);
    }

    public void getPreferenceData() {
        SharedPreferences pref = context.getSharedPreferences("pref", 0);
        this.token = pref.getString("token", "NON");    // get Token
        this.userid = pref.getString("id", "NON");      // get Token
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        setItem(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeActivity.class);
                intent.putExtra("id",list.get(position).getItemId()+"");
                intent.putExtra("titleThumbnail", list.get(position).getImageUrl());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) { return position; }

    private void setItem(final RecyclerHolder holder, int position) {

        final Recipe recipe = list.get(position);

        holder.mRecycleHolder.mTitle.setText(recipe.getTitle());
        holder.mRecycleHolder.mImage.setImageUrl(recipe.getImageUrl(), mImageLoader);

        if(!recipe.getWasLike().equals(""))                                                 // if wasLike
            holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLUE);                     // set Like Button wasLike
        holder.mRecycleHolder.mlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = recipe.getItemId();
                if (recipe.getWasLike().equals("")) {                                       // was not Like item
                    LikeRequest recipeRequest = new LikeRequest(id, userid, token, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("like", "Success");
                            try {
                                holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLUE);
                                recipe.setWasLike(response.getString("id"));                // save Like id
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
                } else {
                    HashMap<String, String> request = new HashMap<String, String>();
                    request.put("model", Request.Method.DELETE+"");
                    request.put("url", util.likeUrl + "/" + recipe.getWasLike());
                    request.put("token", token);

                    JsonObjectRequest recipeRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Cancel like", "Success");
                            holder.mRecycleHolder.mlikeButton.setTextColor(Color.BLACK);
                            recipe.setWasLike("");                                          // delete Like id
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
}
