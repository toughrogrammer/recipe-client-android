package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.util.AuthUserRquest;
import kr.swmaestro.recipe.util.JsonRequestToken;

/**
 * Created by lk on 2015. 7. 31..
 */


public class IntroActivity extends AppCompatActivity{

    TextView myTv;
    TextView foodTv;
    Typeface tf;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        initView();


    }

    private void initView() {

        tf = Typeface.createFromAsset(getAssets(),"Nanumbut.ttf");

        myTv = (TextView) findViewById(R.id.activity_signin_my_tx);
        myTv.setTypeface(tf, Typeface.BOLD);
        foodTv = (TextView) findViewById(R.id.activity_signin_Food_tx);
        foodTv.setTypeface(tf, Typeface.BOLD);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                String token = pref.getString("token", "NON");


                AuthUserRquest recipeRequest = new AuthUserRquest(Request.Method.POST, "http://recipe-main.herokuapp.com/auth/me"
                        , token, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Test", response.length() + "");
                        try {
                            String imgurl = "";
                            Log.i("test",response.get("nickname").toString());
                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", error.toString());
                        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                AppController.getInstance().addToRequestQueue(recipeRequest);

            }
        }, 2000);
    }
}
