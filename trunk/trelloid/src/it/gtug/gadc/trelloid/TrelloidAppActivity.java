/**
 * 
 */

package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.auth.TrelloHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

/**
 * @author fabrizio
 */
public class TrelloidAppActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trelloidapp);
    }

    /**
     * Prende in carico la visualizzazione e la gestione dell'autenticazione Ha
     * come parametro view per essere richiamato dall'onclick nel layout
     * 
     * @param view
     */
    public void authTrello(View view) {

        TrelloHandle handler = new TrelloHandle(this, TrelloidApplication.CONSUMER_KEY,
                TrelloidApplication.CONSUMER_SECRET);
        handler.setAppName(TrelloidApplication.APP_NAME);
        handler.setScope(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(
                MainPreferencesActivity.AUTH_TYPE_PREF, "read,write"));
        handler.setExpiration(PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(MainPreferencesActivity.AUTH_EXPIRE_PREF, "30days"));

        String url = "https://trello.com/1/authorize?key=" + TrelloidApplication.CONSUMER_KEY
                + "&response_type=fragment";
        AQuery aq = new AQuery(this);
        ProgressDialog authDialog = new ProgressDialog(this);
        authDialog.setIndeterminate(true);
        authDialog.setCancelable(false);
        authDialog.setInverseBackgroundForced(false);
        authDialog.setCanceledOnTouchOutside(true);
        authDialog.setTitle("Loading..");
        authDialog.setMessage("Colloquio con il server Trello in corso..");

        aq.auth(handler).progress(authDialog)
                .ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json != null) {
                            // successful ajax call, show status code and json                           
                        } else {
                            // FIXME: questo ramo dovrebbe essere per ajax error
                            Intent intent = new Intent();
                            intent.setClass(TrelloidAppActivity.this, BoardListActivity.class);
                            startActivity(intent);
                        }
                    }
                });        
    }

   
}
