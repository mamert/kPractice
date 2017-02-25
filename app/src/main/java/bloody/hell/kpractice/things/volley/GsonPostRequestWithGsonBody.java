package bloody.hell.kpractice.things.volley;

import com.android.volley.Request;
import com.android.volley.Response;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Mamert on 25.02.2017.
 */

public class GsonPostRequestWithGsonBody<T, U> extends GsonRequest<T>{
    private static final String BODY_CONTENT_TYPE = "application/json; charset=utf-8";

    private U body;

    public GsonPostRequestWithGsonBody(
            String url,
            Class<T> clazz,
            Map headers,
            U body,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {
        super(
                url,
                clazz,
                Request.Method.POST,
                headers,
                listener,
                errorListener);
        this.body = body;
    }



    @Override
    public String getBodyContentType() {
        return BODY_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return body == null ? null :
                    gson.toJson(body).getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            return null; // really shouldn't happen
        }
    }

}
