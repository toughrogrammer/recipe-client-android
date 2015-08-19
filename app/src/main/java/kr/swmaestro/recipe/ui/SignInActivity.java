package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.model.ErrorMap;
import kr.swmaestro.recipe.Request.SignInRequest;
import android.widget.ImageView;

import kr.swmaestro.recipe.helper.MakeBlurHelper;
import kr.swmaestro.recipe.util.AppSetting;

/**
 * Created by lk on 2015. 7. 31..
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private String mEmail;
    private String mPassword;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText emailEt;
    private EditText passwordEt;
    private TextView myTv;
    private TextView foodTv;
    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.initPreference();
        this.initView();
    }

    private void initPreference() {
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
    }

    private void initView() {
        ImageView bgImageView = (ImageView) findViewById(R.id.activity_singnin_background);
        Bitmap blurImage = MakeBlurHelper.makeBlur(getApplicationContext(), getBitmapFromDrawable(), 1);
        bgImageView.setImageBitmap(blurImage);

        tf = Typeface.createFromAsset(getAssets(), AppSetting.logoFont);
        myTv = (TextView) findViewById(R.id.activity_signin_my_tx);
        myTv.setTypeface(tf,Typeface.BOLD);

        foodTv = (TextView) findViewById(R.id.activity_signin_Food_tx);
        foodTv.setTypeface(tf,Typeface.BOLD);

        emailEt = (EditText) findViewById(R.id.et_signin_email);
        emailEt.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));

        passwordEt = (EditText) findViewById(R.id.et_signin_password);
        passwordEt.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));

        Button signInBt = (Button) findViewById(R.id.bt_signin_signin);
        Button signUpBt = (Button) findViewById(R.id.bt_signin_signup);

        signUpBt.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));
        signInBt.setOnClickListener(this);
        signUpBt.setOnClickListener(this);


    }

    private void Signin() {
        final View coordinatorLayoutView = findViewById(R.id.sb_signin_snackbarposition);
        mEmail = emailEt.getText().toString();
        mPassword = passwordEt.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Request stringRequest = new SignInRequest(mEmail, mPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String,String> map = new ErrorMap();

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.has("accessToken")) {                                               // if Sign_in Success
                        editor.putString("token", json.get("accessToken").toString());          // save accessToken
                        editor.commit();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    }
                    else                                                                        // else show snackBar
                        Snackbar
                                .make(coordinatorLayoutView, "이메일 또는 비밀번호가 잘못되었습니다.", Snackbar.LENGTH_LONG)
                                .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "SignIn Request : " + error.networkResponse);
            }
        });
        queue.add(stringRequest);
    }

    private Bitmap getBitmapFromDrawable() {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.background);
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            return bitmap;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_signin_signin:     // if Click sign_in button
                Signin();                   // send Email & password to Server
                break;
            case R.id.bt_signin_signup:     // if Click sign_up button, start SignUpActivity
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;
        }
    }
}
