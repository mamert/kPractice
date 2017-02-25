package bloody.hell.kpractice.things.dropbox.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.things.dropbox.Dropbox;
import bloody.hell.kpractice.things.volley.GsonPostRequestWithGsonBody;

/**
 * Created by Mamert on 25.02.2017.
 */

public abstract class DropboxRequest<Target, Params> extends GsonPostRequestWithGsonBody<Target, Params> {
    private static final String DROPBOX_API_URL = "https://api.dropboxapi.com/";

    public DropboxRequest(String endpoint,
                          Class<Target> clazz,
                          Params body,
                          Response.Listener<Target> listener,
                          Response.ErrorListener errorListener) {
        super(DROPBOX_API_URL + endpoint,
                clazz,
                Dropbox.getInstance(null).createAuthHeaders(null),
                body,
                listener,
                errorListener);
    }


// parseNetworkResponse only returns 200-299 range, so we must take it from onErrorResponse
    public static VolleyError translateError(VolleyError e){
        NetworkResponse response = e.networkResponse;
        switch(response.statusCode) {
            case 400: // bad input. Plaintext response body.
                return new BadInputError(response);
            case 401: // bad/expired/revoked token. Do a re-auth.
                return new AuthFailureError(response);
            case 409: // Contains keys: error (value), error_summary (readable, not meant for user), user_message (optional)
                return EndpointSpecificError.fromResponse(response);
            case 429: // Rate-limiting. Wait for Retry-After seconds. Response is plaintext or json with reason key.
                return RateLimitingError.fromResponse(response);
        }
        return e;
    }


    public static class BadInputError extends VolleyError {
        private String body;
        public BadInputError(NetworkResponse response){
            try {
                body = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                body = new String(response.data);
            }
        }

        @Override
        public String getMessage(){
            return body;
        }
    }

    public static class EndpointSpecificError extends VolleyError {
        private String error; // value
        private String error_summary; // readable, not meant for user
        private String user_message; // optional

        public static EndpointSpecificError fromResponse(NetworkResponse response){
            String body;
            try {
                body = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                body = new String(response.data);
            }
            return new Gson().fromJson(body, EndpointSpecificError.class);
        }

        @Override
        public String getMessage(){
            return user_message;
        }

        public String getSummary(){
            return error_summary;
        }
    }

    public static class RateLimitingError extends VolleyError {
        private int retry_after;
        private String reason;

        public static RateLimitingError fromResponse(NetworkResponse response){
            String body;
            try {
                body = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                body = new String(response.data);
            }
            RateLimitingError error;
            if(body.startsWith("{")){ // json with "reason" key
                error = new Gson().fromJson(body, RateLimitingError.class);
            } else { // plaintext
                error = new RateLimitingError();
                error.reason = body;
            }
            error.retry_after = Integer.parseInt(response.headers.get("Retry-After"));
            return error;
        }

        @Override
        public String getMessage(){
            return reason;
        }
    }

}
