package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.RecycleAdapter;
import kr.swmaestro.recipe.model.Recipe;
import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.util.EndlessRecyclerOnScrollListener;


public class MainActivity extends AppCompatActivity{

    private final String TAG = "MainActivity";

    private DrawerLayout drawer;
    private List<Recipe> list = new ArrayList<>();
    private RecycleAdapter mAdapter = new RecycleAdapter(list);
    private ProgressDialog progressDialog;
    private String Email;
    private String Nickname;
    private ArrayList<View> mMenuItems = new ArrayList<>(6);
    private int count;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ActionBarDrawerToggle drawerToggle;
    TextView mEmailTv;
    TextView mNickTv;
    NavigationView mNavigationView;
    Menu mMenu;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = 0;
        initToolbar();
        initNavtigationView();
        initListView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initNavtigationView() {

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Email = pref.getString("email","test@gmail.com");  // get Email
        Nickname = pref.getString("nickname","Test");  // get Nickname


        mEmailTv = (TextView) findViewById(R.id.activity_main_emailTv);
        mNickTv = (TextView) findViewById(R.id.activity_main_nicknameTv);
        mNavigationView = (NavigationView) findViewById(R.id.activity_main_navigation_view);
        mMenu = mNavigationView.getMenu();

        mEmailTv.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));
        mNickTv.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));

        mEmailTv.setText(Email);
        mNickTv.setText(Nickname);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawer, R.string.hello_world, R.string.hello_world);
        drawer.setDrawerListener(drawerToggle);

        // Install an OnGlobalLayoutListener and wait for the NavigationMenu to fully initialize
        mNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                for (int i = 0, length = 6; i < length; i++) {
                    final String id = "menuItem" + (i + 1);
                    final MenuItem item = mMenu.findItem(getResources().getIdentifier(id, "id", getPackageName()));
                    mNavigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                }
                for (final View menuItem : mMenuItems) {
                    ((TextView) menuItem).setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothic.ttf"), Typeface.BOLD);
                }
            }
        });


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_main_collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("추천요리");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        makeCollapsingToolbarLayoutLooksGood(collapsingToolbarLayout);
//        SpannableString s = new SpannableString("My Title");
//        TypefaceSpan robotoTFS = new TypefaceSpan("NanumBarunGothicBold.ttf");
//        s.setSpan(robotoTFS, 0, s.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        getSupportActionBar().setTitle(s);

        NavigationView nv = (NavigationView) findViewById(R.id.activity_main_navigation_view);
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void initListView() {

        visibleprogress();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        loadRecipeList();

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.i("onLoadMore", current_page + "");
                visibleprogress();
                loadRecipeList();
            }
        });


    }

    private void visibleprogress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로딩중....");
        progressDialog.show();
    }

    private void loadRecipeList() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String token = pref.getString("token", "NON");  // get Token
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET,"http://recipe-main.herokuapp.com/recipes?limit=15&skip="+count, token,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideprograssDialog();

                Log.i("Test", response.length() + "");
                for(int i=0; i< response.length(); i++){
                    try {
                        String imgurl = "";
                        JSONObject jsonObject = response.getJSONObject(i);
                        if(jsonObject.has("thumbnail")){
                            JSONObject imginfo = jsonObject.getJSONObject("thumbnail");
                            imgurl = imginfo.getString("reference");
                        }
                        Recipe recipe = new Recipe(jsonObject.getString("title"), jsonObject.getString("id"), imgurl);

                        list.add(recipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                count+=15;
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


//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "아이템 클릭", Toast.LENGTH_SHORT).show();
//            }
//        });

        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

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


    ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // callback for drag-n-drop, false to skip this feature
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // callback for swipe to dismiss, removing item from data and adapter
            list.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    });


//    @Override
//    public void onRefresh() {
//        Toast.makeText(getApplicationContext(), "BoardFragmentRefresh", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(false);
//            }
//        }, 3000);
//    }

    private void hideprograssDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        try {
            final Field field = collapsingToolbarLayout.getClass().getDeclaredField("mCollapsingTextHelper");
            field.setAccessible(true);

            final Object object = field.get(collapsingToolbarLayout);
            final Field tpf = object.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            ((TextPaint) tpf.get(object)).setTypeface(Typeface.createFromAsset(getAssets(), "Yoon.ttf"));
        } catch (Exception ignored) {
        }
    }
}
