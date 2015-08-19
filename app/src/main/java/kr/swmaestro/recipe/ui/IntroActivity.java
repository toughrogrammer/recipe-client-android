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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.util.AppSetting;

/**
 * Created by lk on 2015. 7. 31..
 */


public class IntroActivity extends AppCompatActivity{

    TextView myTv;
    TextView foodTv;
    Typeface tf;

    final String TAG = "IntroActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initView();
        tokenCheck();
    }

    private void initView() {
        tf = Typeface.createFromAsset(getAssets(), AppSetting.logoFont);
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
                final SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                final String token = pref.getString("token", "NON");                            // Get token when it is saved

                HashMap<String, String> request = new HashMap<>();
                request.put("model", Request.Method.POST+"");
                request.put("url", AppSetting.tokenUrl);
                request.put("token", token);

                JsonObjectRequest tokenRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {                               // Accpet Request is pass token check
                        SharedPreferences.Editor editor = pref.edit();
                        try {
                            editor.putString("nickname", response.get("nickname").toString());
                            editor.putString("email",    response.get("email").toString());
                            editor.putString("id",       response.get("id").toString());
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Success");
                        startActivity(new Intent(IntroActivity.this, MainActivity.class));      // Start Main Activity
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {                            // When Server status 401,
                        Log.e("volley", error.toString());
                        startActivity(new Intent(IntroActivity.this, SignInActivity.class));    // Start Login Activity
                        finish();
                    }
                });
                AppController.getInstance().addToRequestQueue(tokenRequest);
            }
        }, 2000);                                                                               //Timer 2000ms
    }
}
