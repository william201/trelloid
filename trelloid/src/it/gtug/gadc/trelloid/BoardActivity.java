package it.gtug.gadc.trelloid;


import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.BoardContainer;
import it.gtug.gadc.trelloid.model.TrelloFactory;

import java.text.MessageFormat;
import it.gtug.gadc.trelloid.auth.TrelloHandle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;


public class BoardActivity extends ListActivity {
	private static final String serverTrello = "https://api.trello.com";
	private String testKey="5ab5ad43320989a74b677ab82a349db2";
	private String testToken="40c575a5aeed5826d29241019f470218bf31bdacc94a32f7597fd4684b7e8f70";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		String trelloQuery=serverTrello+BoardContainer.apiCall;
		String currentMemberID="me";
		String trelloBoardsURL=MessageFormat.format(trelloQuery,currentMemberID,testKey,testToken);
		AQuery aq = new AQuery(this);
		aq.ajax(trelloBoardsURL, JSONArray.class, this, "jsonBoardListCallback");
	}


	public void jsonBoardListCallback(String url, JSONArray json, AjaxStatus status) {
		Log.d("trelloid", "jsonBoardListCallback:: " + json);
		if (json != null) {
			//Popolare il modello
			BoardContainer allBoards =TrelloFactory.allocate(BoardContainer.class, json);
			
			setListAdapter(
					new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, allBoards.getArray() )
					);
		} else {
			// ajax error 
			//TODO: ajax error handling?
		}
	}
	
}
