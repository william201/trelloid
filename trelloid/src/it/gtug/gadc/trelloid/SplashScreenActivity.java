package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;

import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Per generare un token orario
 * 
 * @see https://trello.com/1/appKey/generate
 */
public class SplashScreenActivity extends ListActivity {

	private final static String key = "9bd5f87e01424e4cae086ea481513c86";
	public final static String testKey = "1b59bc31a7420d12ce644d7d822161a2";
	public final static String testToken = "a30c5f4656e4003a9a84c36a25fda4f8152d3069c39a356d915cb0dcbc094e72";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		// MemberService service = ProxyFactory.create(MemberService.class,
		// "https://api.trello.com");
		// BoardService boardService = ProxyFactory.create(BoardService.class,
		// "https://api.trello.com");
		//
		// List<Board> boards = service.listMemberBoards("fabriziooo", key);
		//
		// for (Board board : boards) {
		// System.err.println("id: " + board.getId());
		// System.err.println("desc: " + board.getName());
		//
		// List<CardContainer> listOfCardContainer = boardService
		// .findListsForBoard(board.getId(), key);
		//
		// for (CardContainer cardContainer : listOfCardContainer) {
		// System.err.println("\tid: " + cardContainer.getId());
		// System.err.println("\tdesc: " + cardContainer.getName());
		//
		// for (Card card : cardContainer.getCards()) {
		// System.err.println("\t\tid: " + card.getId());
		// System.err.println("\t\tdesc: " + card.getName());
		//
		// }
		//
		// }
		//
		// }

		List<Class<?>> l = Arrays.<Class<?>> asList(BoardListActivity.class,
				BoardActivity.class, CardActivity.class);
		setListAdapter(new ArrayAdapter<Class<?>>(this,
				android.R.layout.simple_list_item_1, l));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Class<?> c = (Class<?>) getListAdapter().getItem(position);
		Intent intent = new Intent(this, c);
		Bundle data=new Bundle();
		//FIMXE: Aggiungere un asincTask
		//FIXME: Ricordarsi di estrarre il titolo dalla view una volta implementato un custom layout
		intent.putExtra("title", "NO_TITLE");
		intent.putExtra("board", getBoard("4f3f6245e4b1f2a0023665c8"));
		startActivity(intent);
	}
	private Board getBoard(String boardId) {
		BoardService service = ProxyFactory.create(BoardService.class,"https://api.trello.com");
		List<CardContainer> lists = service.findListsForBoard(boardId, SplashScreenActivity.testKey);

		Board board = new Board();
		board.setId(lists.get(0).getIdBoard());
		board.setContainers(lists);
		
		return board;
	}
}
