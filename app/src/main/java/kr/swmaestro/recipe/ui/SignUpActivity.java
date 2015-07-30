package kr.swmaestro.recipe.ui;


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

import kr.swmaestro.recipe.model.ErrorMap;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.util.SignUpRequest;

/**
 * Created by lk on 2015. 7. 27..
 */
public class SignUpActivity extends AppCompatActivity {

    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
    }

    private void initView() {

        final EditText usernameEt = (EditText) findViewById(R.id.et_signup_username);
        final EditText emailEt = (EditText) findViewById(R.id.et_signup_email);
        final EditText passwordEt = (EditText) findViewById(R.id.et_signup_password);
        final EditText genderEt = (EditText) findViewById(R.id.et_signup_gender);
        final Button sendBt = (Button) findViewById(R.id.bt_signup_send);

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = usernameEt.getText().toString();
                mEmail = emailEt.getText().toString();
                mPassword = passwordEt.getText().toString();
                //mGender = genderEt.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                Request stringRequest = new SignUpRequest(mUsername, mEmail, mPassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String,String> map = new ErrorMap();

                        TextView mTextView = (TextView)findViewById(R.id.textView);

                        try {
                            JSONObject json = new JSONObject(response);
                            if(json.has("error"))
                                mTextView.setText(map.get(json.get("error").toString()));
                            else
                                mTextView.setText("정상가입");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","SignUp Request : "+error.networkResponse);
                    }
                });
                queue.add(stringRequest);
            }
        });
    }

}
