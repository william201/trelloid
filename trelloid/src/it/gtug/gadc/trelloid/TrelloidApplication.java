
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.HashMap;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.Application;

public class TrelloidApplication extends Application {
    private Map<String, Member> membersCache = new HashMap<String, Member>();

    static final String CONSUMER_KEY = "287616866fb290d0ede084f648112848";

    static final String CONSUMER_SECRET = "fb29e30637c7a10b595d42879bd08f529f65479eafd11449aff787bc749b58ea";

    static final String APP_NAME = "Trelloid";
    public static final String TRELLOID_TOKEN = "aq.trelloid.token";
    public static final String TRELLOID_SECRET = "aq.trelloid.secret";
    private static final String TRELLOID_MEMBER_ID = "aq.trelloid.secret";

    public Map<String, Member> getMembersCache() {
        return membersCache;
    }
    
    public Member getMember(String idMemberCreator) {
        Member member = membersCache.get(idMemberCreator);
        if (member == null) {
            MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
            member = memberService.findMembers(idMemberCreator, TrelloidApplication.CONSUMER_KEY);
            membersCache.put(idMemberCreator, member);
        }
        return member;
    }


}
