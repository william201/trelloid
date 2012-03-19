package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.androidquery.AbstractAQuery;

public class TrelloQuery extends AbstractAQuery<TrelloQuery> {

	public TrelloQuery(View view) {
		super(view);
	}

	public TrelloQuery(Activity act) {
		super(act);
	}

	public TrelloQuery memberImage(final String idMemberCreator) {
		if (view != null) {
			final Map<String, Member> membersCache = ((TrelloidApplication) getContext().getApplicationContext()).getMembersCache();
			Member member = membersCache.get(idMemberCreator);
			if (member == null) {
			    new AsyncTask<Void, Void, Member>(){

					@Override
					protected Member doInBackground(Void... params) {
					    MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
					    Member member = memberService.findMembers(idMemberCreator, TrelloidApplication.CONSUMER_KEY);
					    membersCache.put(idMemberCreator, member);
						return member;
					}
					
					protected void onPostExecute(Member member) {
					    downloadAvatar(member);
					}
			        
			    }.execute();
			} else {
				downloadAvatar(member);
			}
		}
		return this;
	}

	private void downloadAvatar(Member member) {
		if (member.getAvatarHash() != null) {
			id(R.id.avatar).visible().image("https://trello-avatars.s3.amazonaws.com/" + member.getAvatarHash() + "/30.png");
			id(R.id.initial).invisible();
		} else {
			id(R.id.initial).visible().text(member.getInitials());
			id(R.id.avatar).invisible();
		}
	}

}