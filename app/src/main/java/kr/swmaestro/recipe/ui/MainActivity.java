package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.util.EndlessRecyclerOnScrollListener;
import kr.swmaestro.recipe.util.AppSetting;


public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView mNavigationView;

    private List<Recipe> list = new ArrayList<>();                      // Recipe List
    private RecyclerView mRecyclerView;                                 // Recipe Recycle View
    private RecycleAdapter mAdapter = new RecycleAdapter(list, this);   // Recycle View Adapter(Recipe list, Content)
    private LinearLayoutManager mLinearLayoutManager;

    private Menu mMenu;
    private ArrayList<View> mMenuItems = new ArrayList<>(6);

    private String Email;                                               // Email in Preference
    private String Nickname;                                            // Nickname in Preference
    private String token;
    private TextView mEmailTv;                                          // TextView in drawer to show the Email
    private TextView mNickTv;                                           // TextView in drawer to show the Nickname

    private ProgressDialog progressDialog;

    private int count = 0;                                                  // Recipe number for more loading
    private int recipeRecallCount = 15;                                       // Recipe number recall a time

    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getPreferenceData();
        this.initToolbar();
        this.initNavtigationView();
        this.initListView();
    }

    public void getPreferenceData() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Email = pref.getString("email","test@gmail.com");   // get Email
        Nickname = pref.getString("nickname","Test");       // get Nickname
        token = pref.getString("token", "NON");             // get Token
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

        mEmailTv = (TextView) findViewById(R.id.activity_main_emailTv);
        mEmailTv.setText(Email);
        mEmailTv.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));

        mNickTv = (TextView) findViewById(R.id.activity_main_nicknameTv);
        mNickTv.setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFontBold));
        mNickTv.setText(Nickname);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawer, R.string.hello_world, R.string.hello_world);

        mNavigationView = (NavigationView) findViewById(R.id.activity_main_navigation_view);
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
                    ((TextView) menuItem).setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFont), Typeface.BOLD);
                }
            }
        });

        mMenu = mNavigationView.getMenu();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_main_collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("추천요리");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = (NavigationView) findViewById(R.id.activity_main_navigation_view);
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isCheckable()) {
                            menuItem.setChecked(true);
                        }
                        drawer.closeDrawers();
                        return true;
                    }
                });
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

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.i("onLoadMore", current_page + "");
                visibleprogress();
                loadRecipeList();
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



    private void visibleprogress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void loadRecipeList() {
        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, AppSetting.recipeUrl + "?limit=" + recipeRecallCount +"&skip="+count, token,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideprograssDialog();                                                      // Hide PrograssDialog at the end of the recipe loaded

                String imgUrl = "";
                String wasLiked = "";
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (jsonObject.has("thumbnail")) {                                // Set Thumbnail url
                            JSONObject imginfo = jsonObject.getJSONObject("thumbnail");
                            imgUrl = imginfo.getString("reference");
                        }
                        if (jsonObject.has("wasLiked"))                                   // Set Like id
                            wasLiked = jsonObject.getString("wasLiked");
                        Recipe recipe = new Recipe(jsonObject.getString("title"), jsonObject.getString("id"), imgUrl, wasLiked);
                        // Recipe Title, Recipe id, Recipe Thumbnail img URL, Like id
                        list.add(recipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                count += 15;
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(recipeRequest);
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
            case android.R.id.home:                     //ActionBar Home button
                drawer.openDrawer(GravityCompat.START); //Open Drawer
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideprograssDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}