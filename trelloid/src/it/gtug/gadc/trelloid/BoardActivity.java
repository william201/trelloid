package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitleProvider;

public class BoardActivity extends Activity {
	private final class ListContainerAdapter extends PagerAdapter implements
			TitleProvider {
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((TextView) object);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView textView = new TextView(BoardActivity.this);
			textView.setText("Page " + position);
			((ViewPager) container).addView(textView, 0);
			return textView;
		}

		public String getTitle(int position) {
			return "Page " + position;
		}
	}

	private static final String serverTrello = "https://api.trello.com";

	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String boardId = getIntent().getStringExtra("boardId");
		if (boardId == null) {
			boardId = "4f3f6245e4b1f2a0023665c8";
		}

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

	private Board getBoard(String boardId) {
		BoardService service = ProxyFactory.create(BoardService.class,
				"https://api.trello.com");
		List<CardContainer> lists = service.findListsForBoard(boardId,
				SplashScreenActivity.testKey);

		Board board = new Board();
		board.setId(lists.get(0).getIdBoard());
		board.setContainers(lists);

		return board;
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
