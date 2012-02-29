
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Member;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;

public class TrelloidApplication extends Application {
    private Map<String, Member> membersCache = new HashMap<String, Member>();

    static final String CONSUMER_KEY = "287616866fb290d0ede084f648112848";

    static final String CONSUMER_SECRET = "fb29e30637c7a10b595d42879bd08f529f65479eafd11449aff787bc749b58ea";

    static final String APP_NAME = "Trelloid";

    public Map<String, Member> getMembersCache() {
        return membersCache;
    }
}
