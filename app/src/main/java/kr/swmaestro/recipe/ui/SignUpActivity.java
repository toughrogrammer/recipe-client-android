package kr.swmaestro.recipe.ui;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.swmaestro.recipe.model.ErrorMap;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.Request.SignUpRequest;

/**
 * Created by lk on 2015. 7. 27..
 */
public class SignUpActivity extends AppCompatActivity {

    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mNickname;

    private EditText usernameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText passwordEt2;
    private EditText nickname;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
    }

    private void initView() {
        usernameEt = (EditText) findViewById(R.id.et_signup_username);
        emailEt = (EditText) findViewById(R.id.et_signup_email);
        passwordEt = (EditText) findViewById(R.id.et_signup_password);
        passwordEt2 = (EditText) findViewById(R.id.et_signup_password2);
        nickname = (EditText) findViewById(R.id.et_signup_nickname);
        final Button sendBt = (Button) findViewById(R.id.bt_signup_send);
        final View coordinatorLayoutView = findViewById(R.id.sb_signup_snackbarposition);

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = usernameEt.getText().toString();
                mEmail = emailEt.getText().toString();
                mPassword = passwordEt.getText().toString();
                mNickname = nickname.getText().toString();

                if (errorCk()) {

                    signUp(coordinatorLayoutView);
                }
            }
        });
    }

    /**
     * Error Handler Method
     */
    private boolean errorCk(){
        View coordinatorLayoutView = findViewById(R.id.sb_signup_snackbarposition);
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailEt.getText().toString());
        if(!matcher.matches()) {
            Snackbar
                    .make(coordinatorLayoutView, "이메일 형식이 올바르지 않습니다.", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }else if(usernameEt.getText().toString().length() < 6) {
            Snackbar
                    .make(coordinatorLayoutView, "아이디를 6자리 이상 입력해주세요.", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }else if(!passwordEt.getText().toString().equals(passwordEt2.getText().toString())){
            Snackbar
                    .make(coordinatorLayoutView, "비밀번호 확인이 다릅니다.", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }else if(passwordEt.getText().length() < 8) {
            Snackbar
                    .make(coordinatorLayoutView, "비밀번호를 8자리 이상 입력해주세요.", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }else if(nickname.getText().length() < 6){
            Snackbar
                    .make(coordinatorLayoutView, "닉네임을 6자리 이상 입력해주세요.", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void signUp(final View coordinatorLayoutView) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Request stringRequest = new SignUpRequest(mUsername, mEmail, mPassword, mNickname, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String, String> map = new ErrorMap();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.has("error")) {
                        Log.e("SignupError", json.get("error").toString());
                        Snackbar.make(coordinatorLayoutView, json.get("error").toString(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT);
                        finish();
                    }
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
