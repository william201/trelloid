package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.CardService;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class CardActivity extends ListActivity {

	private String cardId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String stringExtra = getIntent().getStringExtra("cardId");
		cardId = stringExtra != null ? stringExtra : "4f3fa1c0b23069041241fbfc";
		setContentView(R.layout.card);

		new AsyncTask<Void, Void, Card>() {

			@Override
			protected Card doInBackground(Void... params) {
				Card card = getCard(cardId);
				for (Comment comment : card.getComments()) {
					getMember(comment.getIdMemberCreator());
				}
				return card;
			}

			@Override
			protected void onPostExecute(Card card) {
				AQuery aq = new AQuery(CardActivity.this);

				aq.id(R.id.title).text(card.getName());
				aq.id(R.id.description).text(card.getDesc());

				ArrayAdapter<Comment> arrayAdapter = new CommentAdapter(CardActivity.this, R.layout.comment, R.id.text, card.getComments());
				setListAdapter(arrayAdapter);
			}

		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.commenta:
			String token = getToken();
			AQuery aq = new AQuery(this);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("text", "Prova");
			params.put("key", TrelloidApplication.CONSUMER_KEY);
			params.put("token", token);

			aq.ajax("https://api.trello.com/1/cards/" + cardId + "/actions/comments", params, JSONObject.class, new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject json, AjaxStatus status) {
					System.out.println(123);
				}
			});
			// new AsyncTask<Void, Void, String>() {
			//
			// @Override
			// protected String doInBackground(Void... arg0) {
			// CardService service = ProxyFactory.create(CardService.class,
			// "https://api.trello.com");
			// String token =
			// PreferenceManager.getDefaultSharedPreferences(CardActivity.this).getString(SplashScreenActivity.TRELLOID_TOKEN,
			// null);
			// service.addComment(cardId, TrelloidApplication.CONSUMER_KEY,
			// token, "{ text: 'Prova da trelloid' }");
			// return null;
			// }
			// }.execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getToken() {
		return PreferenceManager.getDefaultSharedPreferences(CardActivity.this).getString(SplashScreenActivity.TRELLOID_TOKEN, null);
	}

	private Card getCard(String cardId) {
		CardService service = ProxyFactory.create(CardService.class, "https://api.trello.com");

		String token = getToken();
		Card card = service.getCard(cardId, SplashScreenActivity.CONSUMER_KEY, token);
		List<Comment> comments = service.getComments(cardId, SplashScreenActivity.CONSUMER_KEY, "commentCard", token);
		card.setComments(comments);
		return card;
	}

	private Member getMember(String idMemberCreator) {
		TrelloidApplication application = (TrelloidApplication) getApplication();
		Map<String, Member> membersCache = application.getMembersCache();
		Member member = membersCache.get(idMemberCreator);
		if (member == null) {
			MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
			member = memberService.findMembers(idMemberCreator, SplashScreenActivity.CONSUMER_KEY);
			membersCache.put(idMemberCreator, member);
		}
		return member;
	}

	private final class CommentAdapter extends ArrayAdapter<Comment> {
		private CommentAdapter(Context context, int resource, int textViewResourceId, List<Comment> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			AQuery aq = new AQuery(view);

			Member member = getMember(getItem(position).getIdMemberCreator());
			if (member.getAvatarHash() != null) {
				aq.id(R.id.avatar).visible().image("https://trello-avatars.s3.amazonaws.com/" + member.getAvatarHash() + "/30.png");
				aq.id(R.id.initial).invisible();
			} else {
				aq.id(R.id.initial).visible().text(member.getInitials());
				aq.id(R.id.avatar).invisible();
			}

			return view;
		}
	}

}
