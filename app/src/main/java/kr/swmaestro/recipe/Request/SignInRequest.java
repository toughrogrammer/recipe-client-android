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
 * Created by lk on 2015. 7. 31..
 */
public class SignInRequest extends Request<String> {


    private Map<String, String> mParams;
    private Response.Listener<String> listener;

    public SignInRequest(String email, String password, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://recipe-main.herokuapp.com/auth/getAccessToken", errorListener);

        mParams = new HashMap<String, String>();
        mParams.put("identifier", email);
        mParams.put("password", password);
        mParams.put("device","android");
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
