package kr.swmaestro.recipe.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import kr.swmaestro.recipe.util.AppSetting;

public class ReviewActivity extends ActionBarActivity {
    private ListView mListView;
    private EditText mCommentEt;
    private String Comment;
    private String Username;
    private Button mRegisterBT;
    private String token;
    private String userid;
    private String recipeId;

    private ProgressDialog progressDialog;

    private int count = 0;                                                  // Recipe number for more loading
    private int recipeRecallCount = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getPreferenceData();
        init();
    }

    private void init() {

        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeid");

        mListView = (ListView) findViewById(R.id.activity_review_lv);
        mCommentEt = (EditText) findViewById(R.id.activity_review_commentET);
        mRegisterBT = (Button) findViewById(R.id.activity_review_registerBt);
        mRegisterBT.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));
        mCommentEt.setTypeface(Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf"));

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
        this.userid = pref.getString("id", "NON");      // get userId
    }

    private void loadRecipeList() {
        JsonArrayRequest reviewRequest = JsonArrayRequest.createJsonRequestToken(Request.Method.GET, AppSetting.recipeUrl + "?limit=" + recipeRecallCount + "&skip=" + count, token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideprograssDialog();
                ArrayList<HashMap<String, String>> ReviewList = new ArrayList<HashMap<String, String>>(2);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        HashMap<String,String> reviewmap = new HashMap<String,String>();

                        Username = jsonObject.getString("username");
                        Comment = jsonObject.getString("comment");

                        reviewmap.put(Username,i+"");
                        reviewmap.put(Comment,i+"");

                        ReviewList.add(reviewmap);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                count += 15;
                String [] from = {"name","comment"};
                int[] to = {android.R.id.text1, android.R.id.text2};

                SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), ReviewList,android.R.layout.simple_expandable_list_item_2,from,to);
                mListView.setAdapter(simpleAdapter);

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
                Log.e("Volley", "Reiveiw Request : " + error.networkResponse);
            }
        });

        queue.add(jsonObjectRequest);
    }
//    private void loadUsername() {
//
//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        String token = pref.getString("token", "NON");  // get Token
//
//        HashMap<String, String> request = new HashMap<>();
//
//        JsonObjectRequest reviewRequest = new JsonObjectRequest(request, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.i("test", response.toString());
//                try {
//                    Username = response.getString("username");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("volley", error.toString());
//            }
//        });
//        AppController.getInstance().addToRequestQueue(reviewRequest);
//    }

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
    private void hideprograssDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
