package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
<<<<<<< HEAD
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
=======
>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import kr.swmaestro.recipe.RecycleAdapter;
import kr.swmaestro.recipe.model.Recipe;
<<<<<<< HEAD
import kr.swmaestro.recipe.util.JsonRequestToken;
=======
import kr.swmaestro.recipe.util.JsonArrayRequest;
import kr.swmaestro.recipe.util.SwipeDismissListViewTouchListener;

>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeLayout;
    private DrawerLayout drawer;
    private List<Recipe> list = new ArrayList<>();
    private RecycleAdapter mAdapter = new RecycleAdapter(list);
    private ArrayList<Recipe> mBlackList = new ArrayList<Recipe>();
    private ProgressDialog progressDialog;
<<<<<<< HEAD
    private String Email;
    private String Nickname;
    private ArrayList<View> mMenuItems = new ArrayList<>(6);

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    FloatingActionButton fabBtn;
    TextView mEmailTv;
    TextView mNickTv;
    NavigationView mNavigationView;
    Menu mMenu;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
=======
>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavtigationView();
        initListView();
<<<<<<< HEAD
       // initSwipeRefreshLayout();


=======
        initSwipeRefreshLayout();
>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf

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

<<<<<<< HEAD
=======
        final ListView listView = (ListView) findViewById(R.id.activity_main_listview);

        listView.setAdapter(mAdapter);

>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로딩중....");
        progressDialog.show();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token
<<<<<<< HEAD
        JsonRequestToken recipeRequest = new JsonRequestToken(Request.Method.GET,"http://recipe-main.herokuapp.com/recipes?limit=30"
                ,token, new Response.Listener<JSONArray>() {
=======
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, "http://recipe-main.herokuapp.com/recipes?limit=3"
                , token, new Response.Listener<JSONArray>() {
>>>>>>> develop
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

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        //mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());

<<<<<<< HEAD
=======
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),mAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });
>>>>>>> c5dee830c8363b681489df322c77da26f49eb8cf

//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "아이템 클릭", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

//    private void initSwipeRefreshLayout() {
//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_container);
//        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.pulltorefresh_color1),
//                getResources().getColor(R.color.pulltorefresh_color2),
//                getResources().getColor(R.color.pulltorefresh_color3));
//    }

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