package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;
import it.gtug.gadc.trelloid.services.CardService;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

public class CardLoader extends AsyncTaskLoader<Card> {

	private String cardId;

	public CardLoader(Context context, String cardId) {
		super(context);
		this.cardId = cardId;
	}

	@Override
	public Card loadInBackground() {
		Card card = getCard(cardId);
//		for (Comment comment : card.getComments()) {
//			((TrelloidApplication) getContext().getApplicationContext()).getMember(comment.getIdMemberCreator());
//		}
		return card;
	}

	protected void onStartLoading() {
		forceLoad();
	}

	private Card getCard(String cardId) {
		CardService service = ProxyFactory.create(CardService.class, "https://api.trello.com");

		String token = getToken();
		Card card = service.getCard(cardId, TrelloidApplication.CONSUMER_KEY, token);
		List<Comment> comments = service.getComments(cardId, TrelloidApplication.CONSUMER_KEY, token, "commentCard");
		card.setComments(comments);
		return card;
	}

	private String getToken() {
		return PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext())
				.getString(TrelloidApplication.TRELLOID_TOKEN, null);
	}

}
