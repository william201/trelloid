package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.CardContainer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitleProvider;

public class BoardActivity extends Activity {
	private final class ListContainerAdapter extends PagerAdapter implements TitleProvider {

		private final Board board;

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
			return this.board.getContainers().size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ListView listView = new ListView(BoardActivity.this);
			List<Card> element = new ArrayList<Card>();
			if (this.board.getContainers() != null) {
				for (CardContainer cardContainer : this.board.getContainers()) {
					if (getTitle(position).equals(cardContainer.getName())) {
						for (Card card : cardContainer.getCards()) {
							element.add(card);
						}
					}
				}
			} else {
			}
			final ArrayAdapter<Card> adapter = new ArrayAdapter<Card>(BoardActivity.this, android.R.layout.simple_list_item_1, element);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> l, View arg1, int position, long arg3) {
					Card card = adapter.getItem(position);
					startActivity(new Intent(BoardActivity.this, CardActivity.class).putExtra("cardId", card.getId()));
				}
			});
			((ViewPager) container).addView(listView, 0);
			return listView;
		}

		public String getTitle(int position) {
			return this.board.getContainers().get(position).getName();
		}

		public ListContainerAdapter(Board board) {
			super();
			if (board == null) {
				board = new Board();
				ArrayList<CardContainer> containers = new ArrayList<CardContainer>();
				board.setContainers(containers);
				containers.add(new CardContainer("ToDo - Fake"));
				containers.add(new CardContainer("Doing - Fake"));
				containers.add(new CardContainer("Done - Fake"));
			}
			this.board = board;
		}

	}

	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		String title = getIntent().getStringExtra("title");

		setTitle(title);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new ListContainerAdapter((Board) getIntent().getSerializableExtra("board")));

		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

	}

}
