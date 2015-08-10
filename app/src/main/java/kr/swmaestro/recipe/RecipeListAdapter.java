package kr.swmaestro.recipe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import kr.swmaestro.recipe.model.Recipe;

/**
 * Created by lk on 2015. 8. 2..
 */
public class RecipeListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Recipe> list;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public RecipeListAdapter(Activity activity, List<Recipe> item) {
        this.activity = activity;
        this.list = item;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_main_row, null);
        if(imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView niThumbNail = (NetworkImageView) convertView.findViewById(R.id.ni_listrow_thumbnail);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_listrow_title);

        Recipe recipe = list.get(position);

        niThumbNail.setImageUrl(recipe.getImageurl(), imageLoader);

        tvTitle.setText(recipe.getTitle());

        return convertView;
    }

    public void remove(Object item) {
        list.remove(item);

    }
}
