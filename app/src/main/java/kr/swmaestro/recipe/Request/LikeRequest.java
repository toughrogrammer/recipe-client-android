package kr.swmaestro.recipe.Request;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lk on 2015. 8. 17..
 */
public class LikeRequest  extends Request<JSONObject> {


    private Map<String, String> mParams;
    private Response.Listener<JSONObject> listener;
    private String token;

    public LikeRequest(int id, String userid, String token, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://recipe-main.herokuapp.com/likes", errorListener);

        mParams = new HashMap<String, String>();
        mParams.put("recipe", id + "");
        mParams.put("user",userid +"");
        this.token = token;
        listener = successListener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(parsed),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    public Map getHeaders() throws AuthFailureError {
        Map params = new HashMap();
        params.put("Authorization", "Bearer " + token);
        return params;
    }
}
