package kr.swmaestro.recipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import kr.swmaestro.recipe.R;

/**
 * Created by lk on 2015. 7. 31..
 */
public class IntroActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 2000);
    }
}
