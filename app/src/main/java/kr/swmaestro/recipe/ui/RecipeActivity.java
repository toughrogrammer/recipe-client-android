package kr.swmaestro.recipe.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.model.Recipe;

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
    FrameLayout preview;
    Context context;
    private NetworkImageView imageView;

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

        id = intent.getStringExtra("id")+"";
        token = pref.getString("token", "NON");  // get Token
        tvMethods = (TextView) findViewById(R.id.tv_recipe_methods);
        layout = (LinearLayout) findViewById(R.id.ll_recipe_methodThumnail2);
        //layout.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void loadrecipe() {

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token
        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes/" + id
                , token, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("test",response.toString());
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
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes/" + id+"/methods"
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
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes/" + id+"/methodThumbs"
                , token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("test",response.toString());
                String imgurl = "";
                for(int i=0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if(jsonObject.has("reference")){
                            imgurl = jsonObject.get("reference").toString();
                            ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
                            NetworkImageView mImage;
                            mImage = new NetworkImageView(getApplicationContext());
                            mImage.setImageUrl(imgurl,mImageLoader);
                            layout.addView(mImage);
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
