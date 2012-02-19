package it.gtug.gadc.trelloid;

import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * URL per ottenere il token orario
 * @see https://trello.com/1/appKey/generate
 * 
 */
public class SplashScreenActivity extends ListActivity {

	//created Sun Feb 19 11:05:42 CET 2012
	private static String testKey="9bd5f87e01424e4cae086ea481513c86";
	private static String testToken="ef5d11bd557e2e97f4977b71fdf0be631403c57e75782812a2529cc060d9449b";

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<Class<?>> l = Arrays.<Class<?>> asList(BoardListActivity.class,
				BoardActivity.class, CardActivity.class);
		setListAdapter(new ArrayAdapter<Class<?>>(this,
				android.R.layout.simple_list_item_1, l));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Class<?> c = (Class<?>) getListAdapter().getItem(position);
		startActivity(new Intent(this, c));
	}
}
