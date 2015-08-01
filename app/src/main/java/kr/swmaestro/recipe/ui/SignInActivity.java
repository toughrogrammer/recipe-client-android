package kr.swmaestro.recipe.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import kr.swmaestro.recipe.util.SignUpRequest;

/**
 * Created by lk on 2015. 7. 31..
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private String mEmail;
    private String mPassword;

    EditText emailEt;
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();

    }

    private void initView() {
        emailEt = (EditText) findViewById(R.id.et_signin_email);
        passwordEt = (EditText) findViewById(R.id.et_signin_password);
        Button signinBt = (Button) findViewById(R.id.bt_signin_signin);
        Button signupBt = (Button) findViewById(R.id.bt_signin_signup);
        signinBt.setOnClickListener(this);
        signupBt.setOnClickListener(this);
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

        mEmail = emailEt.getText().toString();
        mPassword = passwordEt.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Request stringRequest = new SignInRequest(mEmail, mPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String,String> map = new ErrorMap();

                TextView mTextView = (TextView)findViewById(R.id.tv_signin_result);

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.has("accessToken")) {
                        mTextView.setText((json.get("accessToken").toString()));
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", json.get("accessToken").toString());
                        editor.commit();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        mTextView.setText("정상가입");
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
