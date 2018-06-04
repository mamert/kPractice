package bloody.hell.kpractice.things.dropbox;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import bloody.hell.kpractice.things.dropbox.requests.DropboxRequest;

/**
 * Created by Mamert on 14.07.2017.
 */

public class DropboxStreamStuff {

    public static final String SCHEME_DROPBOX = "dropbox://";
    private static final String MARKER_FULLFILE = "f";
    private static final String MARKER_THUMBNAIL = "t";
    private static final String URI_DELIMETER = "|";
    private static final String URI_DELIMETER_QUOTED = Pattern.quote(URI_DELIMETER);


    private static String getResponseBody(HttpResponse resp) throws IOException {
        return getResponseBody(resp.getEntity().getContent());
    }
    private static String getResponseBody(InputStream stream) throws IOException {
        return getResponseBody(stream, true);
    }
    private static String getResponseBody(InputStream stream, boolean closeStream) throws IOException {
//        try(java.util.Scanner s = new java.util.Scanner(is)) { return s.useDelimiter("\\A").hasNext() ? s.next() : ""; }
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String explanation = s.hasNext() ? s.next() : "";
        while(s.hasNext())
            explanation += s.next();
        if(closeStream)
            stream.close();
        return explanation;
    }




    public InputStream getStreamForPath(String path, boolean isThumb) throws DropboxRequest.BadInputError, AuthFailureError, IOException, DropboxRequest.EndpointSpecificError, DropboxRequest.RateLimitingError {

        String urlString = "https://content.dropboxapi.com/2/files/" +(isThumb ? "get_thumbnail" : "download");
        Map<String, String> headers = createArgHeaders(path, isThumb);
        headers = Dropbox.getInstance(null).createAuthHeaders(headers);

        HttpURLConnection conn = (HttpURLConnection)(new URL(urlString)).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(14000);
        for(String key : headers.keySet()) {
            conn.setRequestProperty(key, headers.get(key));
        }
        conn.setRequestProperty("Content-Type", ""); // else sets to application/x-www-form-urlencoded
        conn.connect();
        InputStream is = conn.getInputStream();
//        switch(conn.getResponseCode()){
//            case 400: // bad input. Plaintext response body.
//                throw new DropboxRequest.BadInputError(getResponseBody(is));
//            case 401: // bad/expired/revoked token. Do a re-auth.
//                throw new AuthFailureError(getResponseBody(is));
//            case 409: // Contains keys: error (value), error_summary (readable, not meant for user), user_message (optional)
//                throw DropboxRequest.EndpointSpecificError.fromString(getResponseBody(is));
//            case 429: // Rate-limiting. Wait for Retry-After seconds. Response is plaintext or json with reason key.
//                int retryAfter = Integer.valueOf(conn.getHeaderField("Retry-After"));
//                throw DropboxRequest.RateLimitingError.fromString(getResponseBody(is), retryAfter);
//        }
        return new BufferedInputStream(is);
    }





    public static enum ThumbSize {
        w32h32, w64h64, w128h128, w640h480, w1024h768;
    }
    private static Map<String, String> createArgHeaders(String path, boolean isThumb){
        Map<String, String> ret = new HashMap<>();
        ret.put("Dropbox-API-Arg", new Gson().toJson(createParams(path, isThumb ? ThumbSize.w128h128 : null)));
        return ret;
    }

    private static Params createParams(String path, ThumbSize thumbSize) {
        Params p = new Params();
        p.path = path;
        if(thumbSize != null) {
            p.format = "jpeg";
            p.size = thumbSize.name();
        }
        return p;
    }

    public static class Params {
        String path;
        String format;
        String size;
    }



}
