
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class TrelloidApplication extends Application {
    private Map<String, Member> membersCache = new HashMap<String, Member>();

    static final String CONSUMER_KEY = "287616866fb290d0ede084f648112848";

    static final String CONSUMER_SECRET = "fb29e30637c7a10b595d42879bd08f529f65479eafd11449aff787bc749b58ea";

    static final String APP_NAME = "Trelloid"; 
    public static final String TRELLOID_TOKEN = "aq.trelloid.token";
    public static final String TRELLOID_SECRET = "aq.trelloid.secret";
    
    private ArrayList<Board> boards;
    private boolean authenticated;
    private String myMemberId;
    private Drawable myAvatar;

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
    //singleton design pattern
    static TrelloidApplication instance;  
    public static TrelloidApplication getInstance(){
            if(instance==null){
                    Log.v("TrelloidApplication", "instance created");
                    instance=new TrelloidApplication();
            }
            Log.v("TrelloidApplication", "instance returned");
            return instance;
    }
    
    @Override
    public void onCreate() {
            super.onCreate();
            Log.v("TrelloidApplication", "onCreate");
            
            TrelloidApplication trelloid=getInstance();
            trelloid.boards=null;
            trelloid.membersCache=new HashMap<String, Member>();
            trelloid.authenticated=false;
            trelloid.myAvatar=null;
            trelloid.myMemberId=null;
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<Board> boards) {
        this.boards = boards;
    }

    public void setMembersCache(Map<String, Member> membersCache) {
        this.membersCache = membersCache;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getMyMemberId() {
        return myMemberId;
    }

    public void setMyMemberId(String myMemberId) {
        this.myMemberId = myMemberId;
    }

    public boolean isMeLoaded() {
        if(myMemberId!=null&&myMemberId.length()>2){
            return true;
        }else{
            return false;
        }
    }

    public Drawable getMyAvatar() {
        return myAvatar;
    }

    public void setMyAvatar(Drawable myAvatar) {
        this.myAvatar = myAvatar;
    }

    public boolean isBoardsLoaded() {
       if(boards!=null){
           return true;
       }return false;
    }

}
