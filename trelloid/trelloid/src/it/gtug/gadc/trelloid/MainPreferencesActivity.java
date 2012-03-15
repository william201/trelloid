package it.gtug.gadc.trelloid;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class MainPreferencesActivity extends PreferenceActivity {

	public static final String AUTH_TYPE_PREF = "auth_type";
	public static final String AUTH_EXPIRE_PREF = "auth_expire";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		// Get the custom preference
        Preference customPref = (Preference) findPreference(AUTH_TYPE_PREF);
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				/**
				 * Mostra il valore attuale PRIMA di cambiarlo(Prima che si apra la tendina di selezione)
				 */
                public boolean onPreferenceClick(Preference preference) {
                	
                        String prefValue = preference.getSharedPreferences().getString(AUTH_TYPE_PREF, "read,write");
						Toast.makeText(getBaseContext(),
                                        "AuthType: "+prefValue,
                                        Toast.LENGTH_SHORT).show();
                        return true;
                }

        });
        
        customPref = (Preference) findPreference(AUTH_EXPIRE_PREF);
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				/**
				 * Mostra il valore attuale PRIMA di cambiarlo(Prima che si apra la tendina di selezione)
				 */
                public boolean onPreferenceClick(Preference preference) {
                	
                        String prefValue = preference.getSharedPreferences().getString(AUTH_EXPIRE_PREF, "30days");
						Toast.makeText(getBaseContext(),
                                        "AuthExpire: "+prefValue,
                                        Toast.LENGTH_SHORT).show();
                        return true;
                }

        });
	}
}
	
