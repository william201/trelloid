package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitleProvider;

public class BoardActivity extends Activity {
	private static final String DEMO_BOARD = "4f3f6245e4b1f2a0023665c8";
	private final class ListContainerAdapter extends PagerAdapter implements
			TitleProvider {
		

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getCount() {
			return board.getContainers().size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ListView listView = new ListView(BoardActivity.this);
			List<String> element = new ArrayList<String>();
			if(board.getContainers()!=null){
				for (CardContainer cardContainer : board.getContainers()) {
					if(getTitle(position).equals(cardContainer.getName())){
						for (Card card : cardContainer.getCards()) {
							element.add(card.getName());
						}
					}
				}
			}else{
				element.add("NoOne");
			}
			listView.setAdapter(new ArrayAdapter<String>(BoardActivity.this,
					android.R.layout.simple_list_item_1, element));
			((ViewPager) container).addView(listView, 0);
			return listView;
		}

		public String getTitle(int position) {
			return board.getContainers().get(position).getName();
		}
	}

	private static final String serverTrello = "https://api.trello.com";

	ViewPager mPager;
	PageIndicator mIndicator;

	private Board board;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String boardId = getIntent().getStringExtra("boardId");
		if (boardId == null) {
			boardId = DEMO_BOARD;
		}

		String title = getIntent().getStringExtra("title");
		if (title == null) {
			title = "abc";
		}
		board = (Board) getIntent().getSerializableExtra("board");
		if (board == null) {
			board = new Board();
			ArrayList<CardContainer> containers = new ArrayList<CardContainer>();
			board.setContainers(containers);
			containers.add(new CardContainer("ToDo - Fake"));
			containers.add(new CardContainer("Doing - Fake"));
			containers.add(new CardContainer("Done - Fake"));
		}

		setTitle(title);

		setContentView(R.layout.board);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new ListContainerAdapter());

		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		// Board currBoard = getBoard(boardId);

		// AQuery aq = new AQuery(this);
		// aq.id(R.id.boardid).text(currBoard.getId());
		//
		//
		// setListAdapter(new ContainerAdapter(this,
		// R.layout.item_list , currBoard.getContainers() ));

	}

	

	//
	// public void jsonBoardListCallback(String url, JSONArray json, AjaxStatus
	// status) {
	// Log.d("trelloid", "jsonBoardListCallback:: " + json);
	// if (json != null) {
	// //Popolare il modello
	// BoardContainer allBoards =TrelloFactory.allocate(BoardContainer.class,
	// json);
	//
	// setListAdapter(
	// new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
	// allBoards.getArray() )
	// );
	// } else {
	// // ajax error
	// //TODO: ajax error handling?
	// }
	// }

}
