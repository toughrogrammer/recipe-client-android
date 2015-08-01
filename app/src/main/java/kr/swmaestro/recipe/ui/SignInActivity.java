package kr.swmaestro.recipe.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.helper.MakeBlurHelper;

/**
 * Created by lk on 2015. 7. 31..
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();

    }

    private void initView() {
        ImageView bgImageView = (ImageView) findViewById(R.id.activity_singnin_background);
        Bitmap blurImage = MakeBlurHelper.makeBlur(getApplicationContext(), getBitmapFromDrawable(), 5);
        bgImageView.setImageBitmap(blurImage);
        final EditText emailEt = (EditText) findViewById(R.id.et_signin_email);
        final EditText passwordEt = (EditText) findViewById(R.id.et_signin_password);
        final Button signinBt = (Button) findViewById(R.id.bt_signin_signin);
        final Button signupBt = (Button) findViewById(R.id.bt_signin_signup);
        signinBt.setOnClickListener(this);
        signupBt.setOnClickListener(this);


    }

    private Bitmap getBitmapFromDrawable() {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.test);
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

                break;
            case R.id.bt_signin_signup:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
