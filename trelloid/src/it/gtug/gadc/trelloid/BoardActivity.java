package it.gtug.gadc.trelloid;


import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;
import java.util.List;
import it.gtug.gadc.trelloid.R;
import org.jboss.resteasy.client.ProxyFactory;
import android.app.ListActivity;
import android.os.Bundle;
import com.androidquery.AQuery;


public class BoardActivity extends ListActivity {
	private static final String serverTrello = "https://api.trello.com";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		String boardId = getIntent().getStringExtra("boardId");
		if (boardId == null) {
			boardId = "4f3f6245e4b1f2a0023665c8";
		}
			
		Board currBoard = getBoard(boardId); 
		setContentView(R.layout.board);
		
		AQuery aq = new AQuery(this);
		aq.id(R.id.boardid).text(currBoard.getId());
		
		
		setListAdapter(new ContainerAdapter(this,
				R.layout.item_list , currBoard.getContainers()  ));
		
	}


	private Board getBoard(String boardId) {
		BoardService service = ProxyFactory.create(BoardService.class,"https://api.trello.com");
		List<CardContainer> lists = service.findListsForBoard(boardId, SplashScreenActivity.testKey);

		Board board = new Board();
		board.setId(lists.get(0).getIdParentBoard());
		board.setContainers(lists);
		
		return board;
	}

//
//	public void jsonBoardListCallback(String url, JSONArray json, AjaxStatus status) {
//		Log.d("trelloid", "jsonBoardListCallback:: " + json);
//		if (json != null) {
//			//Popolare il modello
//			BoardContainer allBoards =TrelloFactory.allocate(BoardContainer.class, json);
//			
//			setListAdapter(
//					new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, allBoards.getArray() )
//					);
//		} else {
//			// ajax error 
//			//TODO: ajax error handling?
//		}
//	}
	
}
