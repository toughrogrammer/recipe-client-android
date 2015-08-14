package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.RecipeListAdapter;
import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.util.JsonRequestToken;
import kr.swmaestro.recipe.util.SwipeDismissListViewTouchListener;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeLayout;
    private DrawerLayout drawer;
    private List<Recipe> list = new ArrayList<>();
    private RecipeListAdapter mAdapter = new RecipeListAdapter(this, list);
    private ArrayList<Recipe> mBlackList = new ArrayList<Recipe>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavtigationView();
        initListView();
        initSwipeRefreshLayout();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initNavtigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView nv = (NavigationView) findViewById(R.id.navigation_view);
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isCheckable()) {
                            menuItem.setChecked(true);
                        }
                        Toast.makeText(getApplicationContext(),
                                menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    private void initListView() {

        final ListView listView = (ListView) findViewById(R.id.activity_main_listview);

        listView.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로딩중....");
        progressDialog.show();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token
        JsonRequestToken recipeRequest = JsonRequestToken.createJsonRequestToken(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes?limit=30"
                , token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideprograssDialog();

                Log.i("Test", response.length() + "");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String imgurl = "";
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (jsonObject.has("thumbnail")) {
                            JSONObject imginfo = jsonObject.getJSONObject("thumbnail");
                            imgurl = imginfo.getString("reference");
                        }
                        Recipe recipe = new Recipe(jsonObject.getString("title"), jsonObject.getString("id"), imgurl);

                        list.add(recipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(recipeRequest);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mBlackList.add((Recipe) mAdapter.getItem(position));
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                                //Toast.makeText(getApplicationContext(),mBlackList.toString(),Toast.LENGTH_SHORT).show();
                                Log.d(TAG, mBlackList.toString());
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),mAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });

    }

    private void initSwipeRefreshLayout() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.pulltorefresh_color1),
                getResources().getColor(R.color.pulltorefresh_color2),
                getResources().getColor(R.color.pulltorefresh_color3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home: //ActionBar Home button
                drawer.openDrawer(GravityCompat.START); //Open Drawer
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "BoardFragmentRefresh", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private void hideprograssDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
