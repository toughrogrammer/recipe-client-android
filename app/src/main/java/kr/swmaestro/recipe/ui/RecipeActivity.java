package kr.swmaestro.recipe.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import kr.swmaestro.recipe.util.AppSetting;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecipeActivity extends AppCompatActivity{

    private TextView tvMethods;
    private String id;              // RecipeID
    private String token;           // UserToken
    private String userid;          // UserID
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
        getPreferenceData();
        loadmethods();
        loadThumnail();
        sendViewEvent();
    }

    private void init() {
        Intent intent = getIntent();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        scrollView = (HorizontalScrollView) findViewById(R.id.activity_receipe_scroll);
        scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        like = (ImageButton) findViewById(R.id.activity_receipe_like);

        reviewlist = (ImageButton) findViewById(R.id.activity_receipe_review);

        reviewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, ReviewActivity.class);
                intent.putExtra("recipeid", id);
                startActivity(intent);
            }
        });

        id = intent.getStringExtra("id")+"";            // get recipeId
        title = intent.getStringExtra("title");         // get recipeName
        tvMethods = (TextView) findViewById(R.id.tv_recipe_methods);
        layout = (LinearLayout) findViewById(R.id.ll_recipe_methodThumnail2);

        tvMethods.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFont));
        Tv_title = (TextView) findViewById(R.id.activity_receipe_title);
        Tv_title.setText(title);
        Tv_title.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));

    }

    public void getPreferenceData() {
        SharedPreferences pref = context.getSharedPreferences("pref", 0);
        this.token = pref.getString("token", "NON");    // get Token
        this.userid = pref.getString("id", "NON");      // get userId
    }


    private void loadmethods() {
        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.GET+"");
        request.put("url", AppSetting.recipeUrl + "/" + id+"/methods");
        request.put("token", token);

        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(request, new Response.Listener<JSONArray>() {
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
        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.GET+"");
        request.put("url", AppSetting.recipeUrl + "/" + id+"/methodThumbs");
        request.put("token", token);

        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(request, new Response.Listener<JSONArray>() {
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

    private void sendViewEvent(){
        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.POST+"");
        request.put("url", AppSetting.recipeUrl + "/" + id + "/views");
        request.put("token", token);

        JsonObjectRequest recipeRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ViewEvent", "Success");
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
