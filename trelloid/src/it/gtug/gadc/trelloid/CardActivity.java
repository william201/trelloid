package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.CardService;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

public class CardActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String stringExtra = getIntent().getStringExtra("cardId");
		final String cardId = stringExtra != null ? stringExtra : "4f3fa1c0b23069041241fbfc";
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

	private Card getCard(String cardId) {
		CardService service = ProxyFactory.create(CardService.class, "https://api.trello.com");

		Card card = service.getCard(cardId, SplashScreenActivity.testKey);
		List<Comment> comments = service.getComments(cardId, SplashScreenActivity.testKey, "commentCard");
		card.setComments(comments);
		return card;
	}

	private Member getMember(String idMemberCreator) {
		TrelloidApplication application = (TrelloidApplication) getApplication();
		Map<String, Member> membersCache = application.getMembersCache();
		Member member = membersCache.get(idMemberCreator);
		if (member == null) {
			MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
			member = memberService.findMembers(idMemberCreator, SplashScreenActivity.testKey);
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
