package kr.swmaestro.recipe.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.RecycleAdapter;
import kr.swmaestro.recipe.model.Category;
import kr.swmaestro.recipe.model.Feelings;
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
    private Button mLikeBtn;
    private ProgressDialog progressDialog;

    private CheckBox[] mfeelCheckBoxs;
    private CheckBox[] mCategoryCheckBoxs;
    private Dialog mFeelDialog;
    private Dialog mCategoryDialog;
    private ArrayList<String> nowfeelings;
    private ArrayList<Integer> nowcategories;
    private Feelings feelings;
    private Category categories;
    private String feel;                                                // Feeling Select Option    ex) ?where{"feelings":[]}
    private String category;                                            // Category Select Option   ex) ?where{"categories":[]}

    private int count = 0;                                              // Recipe number for more loading
    private int recipeRecallCount = 15;                                 // Recipe number recall a time

    private EndlessRecyclerOnScrollListener mScrollListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initData();
        this.initToolbar();
        this.initNavtigationView();
        this.initListView();
    }

    public void initData() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Email = pref.getString("email","test@gmail.com");   // get Email
        Nickname = pref.getString("nickname", "Test");       // get Nickname
        token = pref.getString("token", "NON");             // get Token

        feelings = new Feelings();
        categories = new Category();
        nowfeelings = feelings.getFeels();
        feel = "";
        category = "";

        mFeelDialog = createDialog();
        mCategoryDialog = createDialog2();
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

        //드로어를 여는 버튼에 애니메이션 효과를 적용함
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawer, R.string.hello_world, R.string.hello_world);
        //collapsingToolbarLayout
        makeCollapsingToolbarLayoutLooksGood(collapsingToolbarLayout);
        //드로어에 있는 아이템들의 폰트를 바꿔줌
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
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        makeCollapsingToolbarLayoutLooksGood(collapsingToolbarLayout);

        NavigationView nv = (NavigationView) findViewById(R.id.activity_main_navigation_view);
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isCheckable()) {
                            menuItem.setChecked(true);
                        }
                        if(menuItem.getTitle().equals("식감선택"))
                            mFeelDialog.show();
                        else if(menuItem.getTitle().equals("카테고리선택"))
                            mCategoryDialog.show();
                        else if(menuItem.getTitle().equals("로그아웃")){
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }

                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    private void initListView() {
        visibleprogress();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLikeBtn = (Button) findViewById(R.id.bt_recycle_like);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

        loadRecipeList();
        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.i("onLoadMore", current_page + "");
                visibleprogress();
                loadRecipeList();
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    private void addViewListener() {
        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.i("onLoadMore", current_page + "");
                visibleprogress();
                loadRecipeList();
            }
        };
        mRecyclerView.setOnScrollListener(mScrollListener);
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
        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.GET+"");
        request.put("url", AppSetting.predictionUrl + "?limit=" + recipeRecallCount +"&skip="+count+"&where={"+ feel + category+"}");
        request.put("token", token);

        Log.i("url", request.get("url"));

        JsonArrayRequest recipeRequest = JsonArrayRequest.createJsonRequestToken(request,new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
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
                            wasLiked = "true";
                        else
                            wasLiked = "false";
                        Recipe recipe = new Recipe(jsonObject.getString("title"), jsonObject.getString("id"), imgUrl, wasLiked);
                        // Recipe Title, Recipe id, Recipe Thumbnail img URL, Like id
                        list.add(recipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                count += 15;
                mAdapter.notifyDataSetChanged();
                hideprograssDialog();                                                      // Hide PrograssDialog at the end of the recipe loaded
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
                hideprograssDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(recipeRequest);
    }

    //리사이클 뷰 좌우로 스크롤해서 없애기(dismiss)
    ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            list.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    });

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
    //CollapsingToolbarLayout 폰트설정
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
    private void hideprograssDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private AlertDialog createDialog(){
        final View innerView = getLayoutInflater().inflate(R.layout.selectfeel, null);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("식감설정");
        ab.setView(innerView);
        mfeelCheckBoxs = new CheckBox[]{
                (CheckBox) innerView.findViewById(R.id.cb_check_all),
                (CheckBox) innerView.findViewById(R.id.cb_01),
                (CheckBox) innerView.findViewById(R.id.cb_02),
                (CheckBox) innerView.findViewById(R.id.cb_03),
                (CheckBox) innerView.findViewById(R.id.cb_04),
                (CheckBox) innerView.findViewById(R.id.cb_05),
                (CheckBox) innerView.findViewById(R.id.cb_06),
                (CheckBox) innerView.findViewById(R.id.cb_07),
                (CheckBox) innerView.findViewById(R.id.cb_08),
                (CheckBox) innerView.findViewById(R.id.cb_09),
                (CheckBox) innerView.findViewById(R.id.cb_10),
                (CheckBox) innerView.findViewById(R.id.cb_11)
        };
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                boolean[] ckfeels = new boolean[mfeelCheckBoxs.length];
                for (int i = 1; i < mfeelCheckBoxs.length; i++) {
                    ckfeels[i] = mfeelCheckBoxs[i].isChecked();
                }
                feelings.setCkfeels(ckfeels);
                nowfeelings = feelings.getCkfeels();
                if (mfeelCheckBoxs[0].isChecked())
                    feel = "\"\"";
                else {
                    feel = "\"feelings\":[\"" + nowfeelings.get(0) + "\"";
                    for (int i = 1; i < nowfeelings.size(); i++)
                        feel += ",\"" + nowfeelings.get(i) + "\"";
                    feel += "]";
                }
                list.clear();
                count = 0;
                mAdapter.notifyDataSetChanged();
                hideprograssDialog();
                addViewListener();
                loadRecipeList();
            }
        });
        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mfeelCheckBoxs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox checkBox : mfeelCheckBoxs)
                    checkBox.setChecked(mfeelCheckBoxs[0].isChecked());
            }
        });
        for (int i = 0; i < mfeelCheckBoxs.length; i++) {
            mfeelCheckBoxs[i].setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFont));
        }
        return ab.create();
    }

    private AlertDialog createDialog2(){
        final ScrollView innerView = (ScrollView)getLayoutInflater().inflate(R.layout.selectcategory, null);
        final LinearLayout ll = (LinearLayout) innerView.findViewById(R.id.ll_selectcategoty);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("카테고리 설정");
        ab.setView(innerView);

        mCategoryCheckBoxs = new CheckBox[48];

        for(int i=0; i< 48;i++){
            mCategoryCheckBoxs[i] = new CheckBox(getApplicationContext());
            mCategoryCheckBoxs[i].setText(categories.getTitle(i));
            mCategoryCheckBoxs[i].setTextColor(Color.BLACK);
            ll.addView(mCategoryCheckBoxs[i]);
        }
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                boolean[] ckCategory = new boolean[48];
                for (int i = 0; i < 48; i++) {
                    ckCategory[i] = mCategoryCheckBoxs[i].isChecked();
                }
                nowcategories = categories.setCkCategory(ckCategory);

                if (feel.isEmpty()) {
                    category = "\"categories\":[\"" + nowcategories.get(0) + "\"";
                    for (int i = 1; i < nowcategories.size(); i++)
                        category += ",\"" + nowcategories.get(i) + "\"";
                    category += "]";
                } else {
                    category = ",\"categories\":[\"" + nowcategories.get(0) + "\"";
                    for (int i = 1; i < nowcategories.size(); i++)
                        category += ",\"" + nowcategories.get(i) + "\"";
                    category += "]";
                }
                list.clear();
                count = 0;
                mAdapter.notifyDataSetChanged();
                hideprograssDialog();
                addViewListener();
                loadRecipeList();
            }
        });
        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        for (int i = 0; i < mfeelCheckBoxs.length; i++) {
            mCategoryCheckBoxs[i].setTypeface(Typeface.createFromAsset(getAssets(), AppSetting.appFont));
        }
        return ab.create();
    }
}