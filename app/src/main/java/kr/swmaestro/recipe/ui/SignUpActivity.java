package kr.swmaestro.recipe.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

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
                Log.i("test",mUsername + mEmail + mPassword);

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                Request stringRequest = new SignUpRequest(mUsername, mEmail, mPassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.i("test", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                Log.i("test",stringRequest.toString());
                queue.add(stringRequest);
            }
        });
    }

}
