package kr.swmaestro.recipe.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lk on 2015. 8. 5..
 */
public class RecipeUploadRequest extends Request<String>{

    private Map<String, String> mParams;
    private Response.Listener<String> listener;
    private String token;


    public RecipeUploadRequest(String title, String method, String thumbnail, String token, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, "http://recipe-main.herokuapp.com/recipes", errorListener);

        mParams = new HashMap<String, String>();
        mParams.put("title", title);
        mParams.put("method", method);
        mParams.put("thumbnail", thumbnail);
        //mParams.put("taste", gender);

        this.token = token;

        listener = successListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;

        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    public Map getHeaders() throws AuthFailureError {
        Map params = new HashMap();
        params.put("Authorization", "Bearer " + token);
        return params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

}
