package kr.swmaestro.recipe.helper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.swmaestro.recipe.R;

/**
 * Created by Kahye on 15. 8. 4..
 */
public class StayledFontHelper {
    public class StylizedFontActivity extends Activity {

        private static final String TYPEFACE_NAME = "NanumMyeongjo.otf";
        private Typeface typeface = null;

        private void loadTypeface(){
            if(typeface==null)
                typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
        }

        @Override
        public void setContentView(int viewId) {
            View view = LayoutInflater.from(this).inflate(viewId, null);
            ViewGroup group = (ViewGroup)view;
            int childCnt = group.getChildCount();
            for(int i=0; i<childCnt; i++){
                View v = group.getChildAt(i);
                if(v instanceof TextView){
                    ((TextView)v).setTypeface(typeface);
                }
            }
            super.setContentView(view);
        }

    }
}
