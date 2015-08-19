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
 * Created by lk on 2015. 7. 27..
 */
public class SignUpRequest extends Request<String> {

    private Map<String, String> mParams;
    private Response.Listener<String> listener;


    public SignUpRequest(String username, String email, String password, String nickname, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://recipe-main.herokuapp.com/auth/local/register", errorListener);

        mParams = new HashMap<String, String>();
        mParams.put("username", username);
        mParams.put("email", email);
        mParams.put("password", password);
        mParams.put("nickname", nickname);
        mParams.put("device","android");
        //mParams.put("gender", gender);

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
