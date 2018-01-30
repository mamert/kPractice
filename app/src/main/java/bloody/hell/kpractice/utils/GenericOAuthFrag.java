package bloody.hell.kpractice.utils;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bloody.hell.kpractice.R;


public class GenericOAuthFrag extends DialogFragment{



    public static enum Reason{
        CSRF_MISMATCH
        // , others
    }

    public static final String TAG = "tag_OAuthFrag";
    public static final String KEY_TOKEN = "access_token";
    private static final String KEY_REDIRECT_URI = "redirect_uri";
    private static final String KEY_CLIENT_ID = "client_id";

    private static final String KEY_RETURN_PARAMS = "return_params";
    private static final String KEY_CSRF_STATE = "state";
    private static final String KEY_CALLBACK_RESULT = "callback_result";
    private static final String KEY_AUTH_URL = "auth_url";
    public static final String KEY_SPOOF_BROWSER = "spoofBrowser";

    private String state_string = null; // CSRF protection
    private HashMap<String, String> result = new HashMap<>();
    private String authUrl;
    private String mRedirectUri;
    private String mClientId;
    private boolean mUseCsrfState;
    private String[] mReturnParams;
    private WebView mWebview;
    private View mLoadingView;
    private Callback mCallBack;
    private boolean pretendToBeAnExternalBrowser;

    private boolean receivedAccessToken = false;
    private boolean isAutoDissmissed = false;
    private Map<String, String> extraHeaders;

    public GenericOAuthFrag() {
        extraHeaders = new HashMap<String, String>();
    }

    private void setArgs(String uri, String redirectUri, String clientId, boolean useCsrfState, boolean pretendToBeAnExternalBrowser, String... returnParams) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_AUTH_URL, uri);
        bundle.putString(KEY_REDIRECT_URI, redirectUri);
        bundle.putString(KEY_CLIENT_ID, clientId);
        bundle.putBoolean(KEY_CSRF_STATE, useCsrfState);
        bundle.putBoolean(KEY_SPOOF_BROWSER, pretendToBeAnExternalBrowser); // pretend to be a browser rather than a webview (Google)
        bundle.putStringArray(KEY_RETURN_PARAMS, returnParams);
        setArguments(bundle);
    }


    protected GenericOAuthFrag show(FragmentManager fm, String uri, String redirectUri, String clientId, boolean useCsrfState, boolean pretendToBeAnExternalBrowser, String... returnParams) {
        setArgs(uri, redirectUri, clientId, useCsrfState, pretendToBeAnExternalBrowser, returnParams);
        show(fm, TAG);
        return this;
    }

    public void setCallback(Callback result) {
        this.mCallBack = result;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            authUrl = bundle.getString(KEY_AUTH_URL);
            mRedirectUri = bundle.getString(KEY_REDIRECT_URI);
            mClientId = bundle.getString(KEY_CLIENT_ID);
            mUseCsrfState = bundle.getBoolean(KEY_CSRF_STATE, false);

            pretendToBeAnExternalBrowser = bundle.getBoolean(KEY_SPOOF_BROWSER);
            if(pretendToBeAnExternalBrowser){
                extraHeaders.put("X-Requested-With", ""); // so it isn't recognized as an embedded browser
                extraHeaders.put("X-Wap-Profile", ""); // this doesn't work
            }

            mReturnParams = bundle.getStringArray(KEY_RETURN_PARAMS);

            state_string = "";
            if(mUseCsrfState)
                state_string = generateCsrfToken();

            try {
                authUrl = authUrl + (authUrl.contains("?") ? "&" : "?")
                        + KEY_CLIENT_ID + "=" + mClientId
                        + "&" + KEY_REDIRECT_URI + "=" + URLEncoder.encode(mRedirectUri, "UTF-8")
                        + "&response_type=token"
                        + (mUseCsrfState ? ("&" + KEY_CSRF_STATE + "=" + state_string) : "");
            } catch (UnsupportedEncodingException e) {
                // will never happen
            }
        }
        View v = inflater.inflate(R.layout.frag_oauth, null);

        mWebview = (WebView) v.findViewById(R.id.webview);
        mWebview.setVisibility(View.GONE);
        mLoadingView = v.findViewById(R.id.loadingView);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        login(view);
    }

    public void login(View view) {
//        CookieSyncManager.createInstance(getActivity());
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookie();
        mWebview.setVisibility(View.GONE);
        mWebview.getSettings().setJavaScriptEnabled(true); // often needed
        if(pretendToBeAnExternalBrowser)
            mWebview.getSettings().setUserAgentString(UserAgentBuilder.getUserAgentString(getActivity()));
        if (Build.VERSION.SDK_INT <= 18) {
            mWebview.getSettings().setSavePassword(false);
        } else {
            // docs: "Saving passwords in WebView will not be supported in future versions"
        }
        mWebview.setVisibility(View.VISIBLE);
        mWebview.clearCache(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith(mRedirectUri)) {
                    receivedAccessToken = true;
                    showLoading();
                    result.clear();
                    result.put(KEY_CALLBACK_RESULT, url);
                    try {
                        Map<String, List<String>> params = getParams(url, mRedirectUri);
                        for (String key : mReturnParams) { // TODO: simplify
                            String value = params.get(key).get(0);
                            result.put(key, value);
                        }
                        result.put(KEY_TOKEN, params.get(KEY_TOKEN).get(0));
                        if(mUseCsrfState)
                            result.put(KEY_CSRF_STATE, params.get(KEY_CSRF_STATE).get(0));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch(NullPointerException e){
                        e.printStackTrace();
                    }

                    if(!csrfTokenMatches(result)){
                        isAutoDissmissed = true;
                        mCallBack.onFail(Reason.CSRF_MISMATCH);
                        dismiss(); // dialog
                    }

                    if(mCallBack.isReturnedDataOK(result)){
                        isAutoDissmissed = true;
                        mCallBack.onSuccess(result);
                        dismiss(); // dialog
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!receivedAccessToken) {
                    mWebview.setVisibility(View.VISIBLE);
                    mLoadingView.setVisibility(View.GONE);
                }
            }

        });
        mWebview.loadUrl(authUrl, extraHeaders);

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isAutoDissmissed) {
            if (mCallBack != null) {
                mCallBack.onCanceled();
            }
        }
    }

    private void showLoading() {
        if (mWebview != null) {
            mWebview.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    private Map<String, List<String>> getParams(String url, String redirectUri) throws UnsupportedEncodingException, MalformedURLException {
        url = url.replaceFirst(redirectUri, "http://a.a");// we use URL parsing but redirectUri need not be one
        url = url.replaceFirst("#", "?"); // not very generic anymore... Dropbox uses a # after the redirect uri
        return splitQuery(new URL(url));
    }
    private Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    private String generateCsrfToken(){
        return (new BigInteger(40,new SecureRandom())).toString();
    }

    private boolean csrfTokenMatches(HashMap<String, String> data){
        if(!mUseCsrfState)
            return true;
        String received = data.get(KEY_CSRF_STATE);
        return state_string.equals(received);
    }


    public interface Callback {

        public void onSuccess(HashMap<String, String> data);
        
        public void onFail(Reason reason);

        public void onCanceled();

        public boolean isReturnedDataOK(HashMap<String, String> data);
    }
}
