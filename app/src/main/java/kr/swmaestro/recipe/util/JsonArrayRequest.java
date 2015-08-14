package kr.swmaestro.recipe.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lk on 2015. 8. 2..
 */
public class JsonArrayRequest extends Request<JSONArray>{

    private String token;
    private Response.Listener<JSONArray> listener;

    private JsonArrayRequest(int model, String url, String token, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        super(model, url, errorListener);
        this.token = token;
        listener = successListener;
    }

    public static JsonArrayRequest createJsonRequestToken(int model, String url, String token, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        return new JsonArrayRequest(model, url, token, successListener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONArray(jsonString),
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
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

    public Map getHeaders() throws AuthFailureError {
        Map params = new HashMap();
        params.put("Authorization", "Bearer " + token);
        return params;
    }
}
