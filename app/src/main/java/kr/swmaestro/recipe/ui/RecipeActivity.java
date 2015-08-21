package kr.swmaestro.recipe.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.util.util;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecipeActivity extends AppCompatActivity{
    //식감  feelings []
    //조리방법 methods []
    //조리과정이미지 methodThumbs {thumbnail {reference} }
    //카테고리 category { label }
    //썸네일(레시피 완성본) thumbnail { reference }
    //쿡타임 cooktime (int)
    //인분 amount
    //칼로리 (1인분기준)  calorie
    //보관온도 temperature
    //보관일 expire (int)
    //title
    //id
    private TextView tvMethods;
    private String id;
    private String token;
    private LinearLayout layout;
    private String title;
    private TextView Tv_title;
    HorizontalScrollView scrollView;
    Context context;
    private ImageButton like;
    private  ImageButton reviewlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);
        context = this;
        init();
        loadrecipe();
        loadmethods();
        loadThumnail();
    }

    private void init() {
        Intent intent = getIntent();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        scrollView = (HorizontalScrollView) findViewById(R.id.activity_receipe_scroll);
        scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        like = (ImageButton) findViewById(R.id.bt_recycle_like);
        reviewlist = (ImageButton) findViewById(R.id.activity_receipe_review);

        reviewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        id = intent.getStringExtra("id")+"";
        token = pref.getString("token", "NON");  // get Token
        tvMethods = (TextView) findViewById(R.id.tv_recipe_methods);
        layout = (LinearLayout) findViewById(R.id.ll_recipe_methodThumnail2);

    }

    private void loadrecipe() {

        tvMethods.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothic.ttf"));

        Tv_title = (TextView) findViewById(R.id.activity_receipe_title);


        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token

        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.GET+"");
        request.put("url", util.recipeUrl + "/" + id);
        request.put("token", token);


        JsonObjectRequest recipeRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("test", response.toString());
                try {
                    title = response.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Tv_title.setText(title);
                Tv_title.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(recipeRequest);
    }

    private void loadmethods() {
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, util.recipeUrl + "/" + id+"/methods"
                , token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("test",response.toString());
                String recipe = "";
                for(int i=0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(jsonObject.has("content")){
                            recipe += jsonObject.get("content")+"\n";

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("recipe",recipe);
                tvMethods.setText(recipe);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(recipeRequest);
    }

    private void loadThumnail(){
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, util.recipeUrl + "/" + id+"/methodThumbs"
                , token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("test",response.toString());
                String imgurl = "";
                //이곳에 맨 처음 시작부분 layout.addView
                for(int i=0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(jsonObject.has("reference")){
                            imgurl = jsonObject.get("reference").toString();
                            ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
                            NetworkImageView mImage;
                            mImage = new NetworkImageView(getApplicationContext());
                            mImage.setImageUrl(imgurl, mImageLoader);

                            layout.addView(mImage, 1100, 900);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


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
