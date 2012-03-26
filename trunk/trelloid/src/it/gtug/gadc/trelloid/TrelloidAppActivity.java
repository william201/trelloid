/**
 * 
 */

package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.auth.TrelloHandle;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

//Classe da cancellare
@Deprecated
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

        TrelloHandle handler = new TrelloHandle(this,this.getApplicationContext(), TrelloidApplication.CONSUMER_KEY,
                TrelloidApplication.CONSUMER_SECRET);
        handler.setAppName(TrelloidApplication.APP_NAME);
        handler.setScope(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
                MainPreferencesActivity.AUTH_TYPE_PREF, "read,write"));
        handler.setExpiration(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
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
                            //TODO:
                            //FIXME: questo ramo dovrebbe essere per ajax error
                            Intent intent = new Intent();
                            intent.setClass(TrelloidAppActivity.this, BoardListActivity.class);
                            startActivity(intent);
                        }
                    }
                });        
    }

    /**
     * Cancella le credenziali registrate in seguito all'autenticazione
    * Ha come parametro view per essere richiamato dall'onclick nel layout
    * @param view
    */
    public void clearPreferences(View view){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
           Editor editor = settings.edit();
           editor.clear();
           editor.commit();
           
           showToast("Credenziali pulite");
    }
    
    public void showMainPreferences(View view){
        Intent settingsActivity = new Intent(getBaseContext(),
                MainPreferencesActivity.class);
        startActivity(settingsActivity);
    }
    
    /**
     * MOstra un popup con il messaggio indicato
     * @param message
     */
    private void showToast(String message){
        
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
    
    /**
    * Ha come parametro view per essere richiamato dall'onclick nel layout
    * @param view
    */
    public void showToken(View view){
            String token= getMyToken();
           showToast("Token:"+token);
    }
    private String getMyToken() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(TrelloidApplication.TRELLOID_TOKEN, null);
    }
}
