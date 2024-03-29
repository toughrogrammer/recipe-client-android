package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.swmaestro.recipe.AppController;
import kr.swmaestro.recipe.R;
import kr.swmaestro.recipe.Request.JsonArrayRequest;
import kr.swmaestro.recipe.Request.JsonObjectRequest;
import kr.swmaestro.recipe.model.ReviewListData;
import kr.swmaestro.recipe.util.AppSetting;
import kr.swmaestro.recipe.util.ReviewListAdapter;

public class ReviewActivity extends ActionBarActivity {
    private ListView mListView;
    private EditText mCommentEt;
    private String Comment;
    private String Username;
    private Button mRegisterBT;
    private String token;
    public ArrayList<ReviewListData> reviewListData;
    private String userid;
    private String recipeId;

    private ProgressDialog progressDialog;

    private ReviewListAdapter reviewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getPreferenceData();
        init();
        loadRecipeList();
    }

    private void init() {
        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeid");

        mListView = (ListView) findViewById(R.id.activity_review_lv);
        mCommentEt = (EditText) findViewById(R.id.activity_review_commentET);
        mRegisterBT = (Button) findViewById(R.id.activity_review_registerBt);
        mRegisterBT.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));
        mCommentEt.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));

        ListView listView = (ListView) findViewById(R.id.activity_review_lv);
        reviewListData = new ArrayList<>();
        reviewListAdapter = new ReviewListAdapter(this, R.layout.review_custom_list,reviewListData);
        listView.setAdapter(reviewListAdapter);
        reviewListAdapter.notifyDataSetChanged();
        mRegisterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerComment(mCommentEt.getText().toString());
            }
        });
    }

    public void getPreferenceData() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Username = pref.getString("useranme","testname");   // get Email
        Comment = pref.getString("comment","hellow");       // get Comment
        token = pref.getString("token", "NON");             // get Token
        this.userid = pref.getString("id", "NON");          // get userId
    }


    private void loadRecipeList() {
        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.GET+"");
        request.put("url", AppSetting.recipeUrl  + "/" + recipeId + "/reviews");
        request.put("token", token);

        JsonArrayRequest reviewRequest = JsonArrayRequest.createJsonRequestToken(request, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideprograssDialog();
                ArrayList<HashMap<String, String>> ReviewList = new ArrayList<HashMap<String, String>>(2);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        JSONObject author = jsonObject.getJSONObject("author");
                        Log.i("username" , author.getString("nickname"));

                        Log.i("conent", jsonObject.getString("content"));

                        ReviewListData data = new ReviewListData(author.getString("nickname"),jsonObject.getString("content"),"imgurl");

                        reviewListData.add(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reviewListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(reviewRequest);
    }


    private void registerComment(String content) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        HashMap<String, String> request = new HashMap<>();
        request.put("model", Request.Method.POST+"");
        request.put("url", AppSetting.reviewUrl);
        request.put("token", token);
        request.put("author", userid);
        request.put("recipe", recipeId);
        request.put("content", content);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Reiveiw Request : " + error.networkResponse.data.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
