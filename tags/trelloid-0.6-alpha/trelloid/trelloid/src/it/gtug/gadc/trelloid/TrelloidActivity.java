package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.BoardContainer;
import it.gtug.gadc.trelloid.model.TrelloFactory;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.androidquery.callback.AjaxStatus;

public class TrelloidActivity extends ListActivity {
	private static final String serverTrello = "https://api.trello.com";
	private String testKey = "5ab5ad43320989a74b677ab82a349db2";
	private String testToken = "bff8a4e2e5309eecbe7f2d5ba4ae4c62951185af2a8ca0ce5277bacfb444f62d";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		String urlTrello = "https://api.trello.com/1/members/me/boards?key=&token=";
//		AQuery aq = new AQuery(this);
//		aq.ajax(urlTrello, JSONArray.class, this, "jsonCallback");
		
		//setListAdapter(new ArrayAdapter<String>(this,
		//		android.R.layout.simple_list_item_1,
		//		Arrays.asList("aaa", "bbb")));
		//------------------------------
	}

	public void jsonCallback(String url, JSONArray json, AjaxStatus status) {
		Log.d("trelloid", "callback: " + json);
		if (json != null) {
			int length = json.length();
			ArrayList<String> boardList = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				try {
					JSONObject jsonObject = json.getJSONObject(i);
					boardList.add(jsonObject.getString("name"));
				} catch (JSONException e) {
				}
			}
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, boardList));
		} else {
			// ajax error
		}
	}

	public void jsonBoardListCallback(String url, JSONArray json,
			AjaxStatus status) {
		Log.d("trelloid", "jsonBoardListCallback:: " + json);
		if (json != null) {
			// Popolare il modello
			BoardContainer allBoards = TrelloFactory.allocate(
					BoardContainer.class, json);

			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, allBoards.getArray()));
		} else {
			// ajax error
			// TODO: ajax error handling?
		}
	}
}
