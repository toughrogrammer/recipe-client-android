package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import kr.swmaestro.recipe.util.SignInRequest;
import android.widget.ImageView;
import android.widget.Toast;

import kr.swmaestro.recipe.helper.MakeBlurHelper;

/**
 * Created by lk on 2015. 7. 31..
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private String mEmail;
    private String mPassword;

    EditText emailEt;
    EditText passwordEt;
    TextView myTv;
    TextView foodTv;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();

    }

    private void initView() {

        ImageView bgImageView = (ImageView) findViewById(R.id.activity_singnin_background);
        Bitmap blurImage = MakeBlurHelper.makeBlur(getApplicationContext(), getBitmapFromDrawable(), 1);
        bgImageView.setImageBitmap(blurImage);

        tf = Typeface.createFromAsset(getAssets(),"Nanumbut.ttf");
        myTv = (TextView) findViewById(R.id.activity_signin_my_tx);
        myTv.setTypeface(tf,Typeface.BOLD);

        foodTv = (TextView) findViewById(R.id.activity_signin_Food_tx);
        foodTv.setTypeface(tf,Typeface.BOLD);

        emailEt = (EditText) findViewById(R.id.et_signin_email);
        emailEt.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));

        passwordEt = (EditText) findViewById(R.id.et_signin_password);
        passwordEt.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));

        Button signinBt = (Button) findViewById(R.id.bt_signin_signin);
        Button signupBt = (Button) findViewById(R.id.bt_signin_signup);

        signupBt.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));
        signinBt.setOnClickListener(this);
        signupBt.setOnClickListener(this);


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
            case R.id.bt_signin_signin:
                Signin();
                break;
            case R.id.bt_signin_signup:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void Signin() {
        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        mEmail = emailEt.getText().toString();
        mPassword = passwordEt.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Request stringRequest = new SignInRequest(mEmail, mPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String,String> map = new ErrorMap();

                //TextView mTextView = (TextView)findViewById(R.id.tv_signin_result);

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.has("accessToken")) {
                        Toast.makeText(getApplication(),"accessToken",Toast.LENGTH_LONG).show();
                        //mTextView.setText((json.get("accessToken").toString()));
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", json.get("accessToken").toString());
                        editor.commit();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Snackbar
                                .make(coordinatorLayoutView, "아이디 또는 비밀번호가 잘못되었습니다.", Snackbar.LENGTH_LONG)
                                .show();
                        //mTextView.setText("정상가입");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "SignUp Request : " + error.networkResponse);
            }
        });
        queue.add(stringRequest);
    }
}
