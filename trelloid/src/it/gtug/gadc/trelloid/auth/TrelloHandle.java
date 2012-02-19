package it.gtug.gadc.trelloid.auth;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;

import com.androidquery.auth.AccountHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class TrelloHandle extends AccountHandle {
	
	private static final String OAUTH_REQUEST_TOKEN = "https://trello.com/1/OAuthGetRequestToken";
    private static final String OAUTH_ACCESS_TOKEN = "https://trello.com/1/OAuthGetAccessToken";
    private static final String OAUTH_AUTHORIZE = "https://trello.com/1/OAuthAuthorizeToken";
	private static final String TW_TOKEN = null;
	private static final String TW_SECRET = null;
	private Activity act;
	private CommonsHttpOAuthConsumer consumer;
	 private String token;
     private String secret;
	private CommonsHttpOAuthProvider provider;
	//    private static final String CALLBACK_URI = "twitter://callback";
	//    private static final String CANCEL_URI = "twitter://cancel";

	
	@Override
	protected void auth() {
		
	}

	public TrelloHandle(Activity act, String consumerKey, String consumerSecret) {
		super();
		this.act = act;
        
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        
//        token = fetchToken(TW_TOKEN);
//        secret = fetchToken(TW_SECRET);
//        
//        if(token != null && secret != null){
//                consumer.setTokenWithSecret(token, secret);
//        }
        
        provider = new CommonsHttpOAuthProvider(OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN, OAUTH_AUTHORIZE);
	}

	@Override
	public boolean authenticated() {
		//return token!=null&&secret!=null;
		return false;
	}

	@Override
	public boolean expired(AbstractAjaxCallback<?, ?> arg0, AjaxStatus arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reauth(AbstractAjaxCallback<?, ?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
