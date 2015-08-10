package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import kr.swmaestro.recipe.R;

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
                if(token.equals("NON")){
                    Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);
    }
}
