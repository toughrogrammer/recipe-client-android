package kr.swmaestro.recipe.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.model.ReviewListData;

/**
 * Created by Kahye on 15. 8. 20..
 */

public class ReviewListAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ReviewListData> reviewArraylist;

    public ReviewListAdapter(Context context, int layoutResourceId, ArrayList<ReviewListData> listData){
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.reviewArraylist = listData;
        Log.i("listsize", reviewArraylist.size()+"");
    }

    @Override
    public int getCount() {
        return reviewArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        TextView mUserNameTv = (TextView) row.findViewById(R.id.review_list_userName);
        TextView mCommentTv = (TextView) row.findViewById(R.id.review_list_comment);

        mUserNameTv.setText(reviewArraylist.get(position).getUsername());
        mCommentTv.setText(reviewArraylist.get(position).getComment());
        Log.i("ss", "ss");

        ImageView imageView = (ImageView) row.findViewById(R.id.review_list_Img);
        try{
            InputStream is = context.getAssets().open(reviewArraylist.get(position).getProfileImgName());
            Drawable d;
            d = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(d);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return row;
    }
}