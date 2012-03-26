package it.gtug.gadc.trelloid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@Deprecated
public class TrelloFactory {

	public static BoardContainer allocate(Class<BoardContainer> class1,
			JSONArray json) {

		BoardContainer boardContainerBC = new BoardContainer(json.length());

		int length = json.length();
		for (int i = 0; i < length; i++) {
			try {
				JSONObject jsonObject = json.getJSONObject(i);
				boardContainerBC.addBoard(jsonObject.getString("id"),
										jsonObject.getString("name"));
			} catch (JSONException e) {
			}
		}

		return boardContainerBC;
	}

}
