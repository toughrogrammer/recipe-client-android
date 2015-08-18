package kr.swmaestro.recipe;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import kr.swmaestro.recipe.helper.MakeBlurHelper;

/**
 * Created by lk on 2015. 8. 15..
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    public RecycleHolder mRecycleHolder;

    static class RecycleHolder {

        public TextView mTitle;
        public NetworkImageView mImage;
        public Button mlikeButton;
        public ImageView coverImage;
        public Bitmap mBitmap;

    }

    public RecyclerHolder(View itemView) {
        super(itemView);
        setHolder(itemView);

    }

    private void setHolder(View itemView) {
        mRecycleHolder = new RecycleHolder();
        mRecycleHolder.mTitle = (TextView) itemView.findViewById(R.id.tv_tittle);
        mRecycleHolder.mImage = (NetworkImageView) itemView.findViewById(R.id.nv_recycle_image);
        mRecycleHolder.mlikeButton = (Button) itemView.findViewById(R.id.bt_recycle_like);
       // mRecycleHolder.coverImage = (ImageView) itemView.findViewById  (R.id.activity_receipe_coverimage);

    }
}