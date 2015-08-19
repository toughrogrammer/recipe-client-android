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
 * Created by Kahye on 15. 8. 20..
 */
public class ReviewRequest extends Request<String> {

    private Map<String, String> mParams;
    private Response.Listener<String> listener;

    public ReviewRequest(String username, String comment, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, "http://recipe-main.herokuapp.com/auth/local/register", errorListener);

        mParams = new HashMap<String, String>();
        mParams.put("username", username);
        mParams.put("comment", comment);

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

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

}