package kr.swmaestro.recipe.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import kr.swmaestro.recipe.R;

public class ReviewActivity extends ActionBarActivity {
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.activity_review_lv);
        ArrayList<HashMap<String,String>> ReviewList = new ArrayList<HashMap<String,String>>(2);

        for(int i = 0;i<5;i++){
            HashMap<String,String> reviewmap = new HashMap<String,String>();
            reviewmap.put("name",i+"");
            reviewmap.put("comment",i+"");
            ReviewList.add(reviewmap);
        }

        String [] from = {"name","comment"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ReviewList,android.R.layout.simple_expandable_list_item_2,from,to);
        mListView.setAdapter(simpleAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
