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

import org.json.JSONObject;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.util.AuthUserRquest;
import kr.swmaestro.recipe.util.JsonRequestToken;
import kr.swmaestro.recipe.util.util;

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
        tokenCheck();
    }

    private void initView() {
        tf = Typeface.createFromAsset(getAssets(),"Nanumbut.ttf");
        myTv = (TextView) findViewById(R.id.activity_signin_my_tx);
        myTv.setTypeface(tf, Typeface.BOLD);
        foodTv = (TextView) findViewById(R.id.activity_signin_Food_tx);
        foodTv.setTypeface(tf, Typeface.BOLD);
    }

    private void tokenCheck() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                String token = pref.getString("token", "NON");          //Get token when it is saved
                AuthUserRquest recipeRequest = new AuthUserRquest(token, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {       //Accpet Request is pass token check
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {    //When Server status 401, Start Login Activity
                        Log.e("volley", error.toString());
                        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AppController.getInstance().addToRequestQueue(recipeRequest);
            }
        }, 2000);                                                       //Timer 2000ms
    }
}
