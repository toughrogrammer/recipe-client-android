package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.util.JsonRequestToken;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loadrecipe();


    }

    private void loadrecipe() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token
        JsonRequestToken recipeRequest = JsonRequestToken.createJsonRequestToken(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes/" + id
                , token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Test", response.length() + "");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String imgurl = "";
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (jsonObject.has("thumbnail")) {
                            JSONObject imginfo = jsonObject.getJSONObject("thumbnail");
                            imgurl = imginfo.getString("reference");
                        }
                        Log.i("recipeActivity", response.toString());
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
