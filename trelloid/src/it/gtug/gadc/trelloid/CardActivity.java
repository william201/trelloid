package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;
import it.gtug.gadc.trelloid.services.CardService;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

public class CardActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String cardId = getIntent().getStringExtra("cardId");
		if (cardId == null) {
			cardId = "4f3fa1c0b23069041241fbfc";
		}
		Card card = getCard(cardId);
		setContentView(R.layout.card);

		AQuery aq = new AQuery(this);

		aq.id(R.id.title).text(card.getName());
		aq.id(R.id.description).text(card.getDesc());

		setListAdapter(new ArrayAdapter<Comment>(this,
				android.R.layout.simple_list_item_1, card.getComments()));
	}

	private Card getCard(String cardId) {
		CardService service = ProxyFactory.create(CardService.class,
				"https://api.trello.com");

		String key = "1b59bc31a7420d12ce644d7d822161a2";
		String token = "56891c779696c37f4bf28fdfac62c2c6b8f66ad52cb53eeaf4d43eb6c1ebbd65";
		Card card = service.getCard(cardId, key, token);
		List<Comment> comments = service.getComments(cardId, key, token,
				"commentCard");
		card.setComments(comments);
		return card;
	}

}
