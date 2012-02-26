package it.gtug.gadc.trelloid;

import android.app.Application;
import it.gtug.gadc.trelloid.model.Member;

import java.util.HashMap;
import java.util.Map;

public class TrelloidApplication extends Application {
	private Map<String, Member> membersCache = new HashMap<String, Member>();

	public Map<String, Member> getMembersCache() {
		return membersCache;
	}
}
