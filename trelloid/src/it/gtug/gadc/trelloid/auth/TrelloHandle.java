
package it.gtug.gadc.trelloid.auth;

import it.gtug.gadc.trelloid.TrelloidApplication;
import it.gtug.gadc.trelloid.model.TrelloToken;
import it.gtug.gadc.trelloid.services.TokenService;
import it.gtug.gadc.trelloid.utils.Const;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.ProxyFactory;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.WebDialog;
import com.androidquery.auth.AccountHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

/**
 * La classe rappresenta un handler di autenticazione per aQuery verso il ws di
 * Trello.com
 * 
 * @author malo
 * @param appName, nome della applicazione che lo utilizza
 * @param expiration, durata del token di autenticazione
 * @param scope, tipologia del token di autenticazione
 */
public class TrelloHandle extends AccountHandle {

    private static final String TAG = "TrelloHandle";

    private static final String OAUTH_REQUEST_TOKEN = "https://trello.com/1/OAuthGetRequestToken";

    private static final String OAUTH_ACCESS_TOKEN = "https://trello.com/1/OAuthGetAccessToken";

    private static final String OAUTH_AUTHORIZE = "https://trello.com/1/OAuthAuthorizeToken";

    private static final String CALLBACK_URI = "x-oauthflow://callback";

    private static final String CANCEL_URI = "x-oauthflow://cancel";

    private static final String URL_ENCODING = "utf-8";

    private Activity act;

    private WebDialog dialog;

    private CommonsHttpOAuthConsumer consumer;

    private CommonsHttpOAuthProvider provider;

    protected String token;

    protected String secret;

    private String appName;

    private String expiration;

    private String scope;
    
    private Context mainApp;
    private AjaxCallback<JSONObject> successCallback;

    public TrelloHandle(Activity act, Context application, String consumerKey, String consumerSecret, AjaxCallback<JSONObject> cb) {
        this(act,application,consumerKey,consumerSecret);
        this.successCallback=cb;

    }
    public TrelloHandle(Activity act, Context application, String consumerKey, String consumerSecret) {
        
        this.act = act;
        this.mainApp=application;
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        token = fetchToken(TrelloidApplication.TRELLOID_TOKEN);
        secret = fetchToken(TrelloidApplication.TRELLOID_SECRET);

        if (token != null && secret != null) {
            consumer.setTokenWithSecret(token, secret);
        }

        provider = new CommonsHttpOAuthProvider(OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN,
                OAUTH_AUTHORIZE);

    }

    private void dismiss() {
        if (dialog != null) {
            new AQuery(act).dismiss(dialog);
            dialog = null;
        }
    }

    private void show() {
        if (dialog != null) {
            new AQuery(act).show(dialog);
        }
    }

    private void failure() {
        dismiss();

        failure(act, 401, "cancel");
    }

    protected void auth() {

        Task task = new Task();
        task.execute();

    }
    
    @Override
	protected synchronized void failure(Context ctx, int arg1, String arg2) {
		System.out.println("Errore!");
		super.failure(ctx, arg1, arg2);
	}

	private class Task extends AsyncTask<String, String, String> implements OnCancelListener,
            Runnable {

        private AbstractAjaxCallback<?, ?> cb;

        @Override
        protected String doInBackground(String... params) {

            String url = null;
            try {
                provider.setOAuth10a(true);
                url = provider.retrieveRequestToken(consumer, CALLBACK_URI);
            } catch (Exception e) {
                AQUtility.report(e);
                return null;
            }

            return url;
        }

        @Override
        protected void onPostExecute(String url) {
            URI uri = null;

            try {
                uri = new URI(url);
                List<NameValuePair> params = URLEncodedUtils.parse(uri, URL_ENCODING);

                // Aggiungo i parametri che mi servono nella url da chiamare
                if (appName != null)
                    params.add(new BasicNameValuePair("name", appName));

                if (scope != null) {
                    NameValuePair scopeParam = new BasicNameValuePair("scope", scope);
                    params.add(scopeParam);
                }
                if (expiration != null) {
                    NameValuePair expirationParam = new BasicNameValuePair("expiration", expiration);
                    params.add(expirationParam);
                }
                // FIXME: Mi torna utile solo perchè mi trasforma la lista di
                // namedValue in una qryString per l'url
                String qryString = URLEncodedUtils.format(params, URL_ENCODING);
                // FIXME: BAD! Decodifico solo perchè quando creao un oggetto
                // URI altrimenti la queryString verrebbe nuovamente codificata
                qryString = URLDecoder.decode(qryString, URL_ENCODING);
                url = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                        uri.getPath(), qryString, uri.getFragment()).toString();

            } catch (URISyntaxException e) {
                Log.d(TAG, "url per l'autenticazione non valido: " + url);
                url = null;
            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "Parametri nella url non validi: " + url);
                url = null;
            }
            if (url != null) {

                dialog = new WebDialog(act, url, new TrelloWebViewClient());
                dialog.setOnCancelListener(this);
                show();
                // dialog.hide();

                dialog.load();

            } else {
                failure();
            }

        }

        public void onCancel(DialogInterface arg0) {
            failure();
        }

        public void run() {
            auth(cb);
        }

    }

    //private static final String TRELLOID_TOKEN = "aq.trelloid.token";

    //private static final String TRELLOID_SECRET = "aq.trelloid.secret";

    private String fetchToken(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mainApp).getString(key, null);
    }

    private void storeToken(String key1, String token1, String key2, String token2) {
        PreferenceManager.getDefaultSharedPreferences(mainApp).edit().putString(key1, token1)
                .putString(key2, token2).commit();
    }

    private String extract(String url, String param) {

        Uri uri = Uri.parse(url);
        String value = uri.getQueryParameter(param);
        return value;

    }

    private class Task2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                provider.setOAuth10a(true);
                provider.retrieveAccessToken(consumer, params[0]);
            } catch (Exception e) {
                AQUtility.report(e);
                e.printStackTrace();
                return null;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String url) {

            if (url != null) {

                token = consumer.getToken();
                secret = consumer.getTokenSecret();

                AQUtility.debug("token", token);
                AQUtility.debug("secret", secret);

                storeToken(TrelloidApplication.TRELLOID_TOKEN, token, TrelloidApplication.TRELLOID_SECRET, secret);
                //Context context = act.getApplicationContext();
                CharSequence text = "Authenticated!Token:" + token;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(mainApp, text, duration);
                toast.show();
                dismiss();
                success(act);
            } else {
                failure();
            }

        }

    }

    /**
     * Il componente che permette di visualizzare una url come fossimo un un
     * browser. Permette all'utente di autenticarsi e di restituirci attraverso
     * la CALLBACK_URI il valore del token di autenticazione
     * 
     * @author malo
     */
    private class TrelloWebViewClient extends WebViewClient {

        private boolean checkDone(String url) {
            if (url.startsWith(CALLBACK_URI)) {

                String verf = extract(url, "oauth_verifier");

                dismiss();
                Task2 task = new Task2();
                task.execute(verf);

                return true;

            } else if (url.startsWith(CANCEL_URI)) {
                failure();
                return true;
            }

            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return checkDone(url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            AQUtility.debug("started", url);

            if (checkDone(url)) {
            } else {
                super.onPageStarted(view, url, favicon);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            AQUtility.debug("finished", url);
            super.onPageFinished(view, url);

            show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            failure();
        }

    }

    @Override
    public boolean expired(AbstractAjaxCallback<?, ?> cb, AjaxStatus status) {
        int code = status.getCode();
        return code == 400 || code == 401;
    }

    @Override
    public boolean reauth(final AbstractAjaxCallback<?, ?> cb) {

        token = null;
        secret = null;
        storeToken(TrelloidApplication.TRELLOID_TOKEN, null, TrelloidApplication.TRELLOID_SECRET, null);

        Task task = new Task();
        task.cb = cb;

        AQUtility.post(cb);

        return false;
    }

    @Override
    public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpRequest request) {

        AQUtility.debug("apply token", cb.getUrl());

        try {
            consumer.sign(request);
        } catch (Exception e) {
            AQUtility.report(e);
        }

    }
    
    
    @Override
    protected synchronized void success(Context context) {
        
        super.success(context);
        if(successCallback != null){
            
            successCallback.async(context);
        }
        successCallback=null;
    }

    @Override
    public boolean authenticated() {
//        if (token != null) {
//        	TokenService service=  ProxyFactory.create(TokenService.class,Const.HTTPS_API_TRELLO_COM);
//        	TrelloToken trelloToken =null;
//        	try{
//            	//FIXME: In attesa che Luca Masini mi aiuti a sistemare gli errori di 401 nel service che causano stackOverflow!
//        		//trelloToken = service.getTrelloToken(token, Const.CONSUMER_KEY);
//        		URL url=new URL(Const.HTTPS_API_TRELLO_COM+"/1/tokens/"+token+"?key="+Const.CONSUMER_KEY);
//        		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        		int responseCode = urlConnection.getResponseCode();
//        		//FIXME: In realt� dovrei testare la validit� dell'expiration.
//    			//Per ora mi basta che il server NON risponda 401(Unauthorized) ma solo un codice di successo 2xx
//        		if(responseCode >=200&&responseCode<300){
//        			Log.i(TAG, "Token valido: "+url);
//        			return true;
//        			
//        		}else if(responseCode==404){
//        			Log.w(TAG,"Token non trovato: "+url);
//        			return false;
//        		}else if(responseCode==401){
//        			Log.w(TAG,"Token non autorizzato: "+url);
//        			return false;
//        		}
//            }catch (Exception e) {
//				Log.e(TAG, "Impossibile verificare il token: "+token);
//			}
//        	if(trelloToken!=null&&trelloToken.isValid()){
//        		return true;
//        	}
//            //Context context = act.getApplicationContext();
////            CharSequence text = "Authenticated!Token:" + token;
////            int duration = Toast.LENGTH_LONG;
////
////            Toast toast = Toast.makeText(mainApp, text, duration);
////            toast.show();
//        }
       return token != null && secret != null;
        //return false;
    }

    @Override
    public void unauth() {

        token = null;
        secret = null;

        CookieSyncManager.createInstance(act);
        CookieManager.getInstance().removeAllCookie();

        storeToken(TrelloidApplication.TRELLOID_TOKEN, null, TrelloidApplication.TRELLOID_SECRET, null);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
