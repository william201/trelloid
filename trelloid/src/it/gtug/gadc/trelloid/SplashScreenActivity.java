package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;

/**
 * Per generare un token orario
 * 
 * @see https://trello.com/1/appKey/generate
 */
public class SplashScreenActivity extends ListActivity {

	private static final String DEMO_BOARD = "4f3f6245e4b1f2a0023665c8";
	private final static String key = "9bd5f87e01424e4cae086ea481513c86";
	public final static String testKey = "1b59bc31a7420d12ce644d7d822161a2";
	public final static String testToken = "a30c5f4656e4003a9a84c36a25fda4f8152d3069c39a356d915cb0dcbc094e72";
	private static final int DIALOG_PROGRESS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		List<Class<?>> l = Arrays.<Class<?>> asList(BoardListActivity.class,
				BoardActivity.class, CardActivity.class);
		ArrayList<Map<String, Object>> listaBoards = new ArrayList<Map<String, Object>>();
		
		
		HashMap<String, Object> demoBoard=new HashMap<String, Object>();
		demoBoard.put("titolo", "TrelloAndroid");
		demoBoard.put("descrizione","L'unica board pubblica che usiamo");
		listaBoards.add(demoBoard);
		
		String[] from = { "titolo", "descrizione"};
        int[] views = { android.R.id.text1, android.R.id.text2 };
        
		HashMap<String, Object> elements= new HashMap<String,Object >();
		elements.put("titoloBoard", "L'unica board pubblica disponibile");
		setListAdapter(new SimpleAdapter(this,listaBoards, android.R.layout.two_line_list_item, from,views));

	}

	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		showDialog(DIALOG_PROGRESS);
		
		new AsyncTask<Void, Void, Board>() {

			@Override
			protected Board doInBackground(Void... params) {
				Board board = getBoard(DEMO_BOARD);
				return board;
			}

			protected void onPostExecute(Board board) {
				//AQuery aq = new AQuery(SplashScreenActivity.this);
				
				/*Scusate ma non mi piaceva proprio scegliere l'activity da lanciare per mezzo di una lista di classi*/
				//Class<?> c = (Class<?>) getListAdapter().getItem(position);
				
				//intent.setClass(SplashScreenActivity.this, c);
//				View view=(View)l.getItemAtPosition(position);
//				TextView titleView=(TextView)view.findViewById(android.R.id.text1);
				
				Intent intent = new Intent();
				intent.setClass(SplashScreenActivity.this, BoardActivity.class);
				//FIXME: Ricordarsi di estrarre il titolo dalla view una volta implementato un custom layout
				intent.putExtra("title","TrelloAndroid");
				intent.putExtra("board", board);
				intent.putExtra("boardId", DEMO_BOARD);
				dismissDialog(DIALOG_PROGRESS);
				startActivity(intent);
			}			

		}.execute();
		
	}
	private Board getBoard(String boardId) {
		BoardService service = ProxyFactory.create(BoardService.class,"https://api.trello.com");
		List<CardContainer> lists = service.findListsForBoard(boardId, SplashScreenActivity.testKey);

		Board board = new Board();
		board.setId(lists.get(0).getIdBoard());
		board.setContainers(lists);
		
		return board;
	}
	
	/**
     * Gestisce le dialog che si possono aprire
     */
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch(id) {
        case DIALOG_PROGRESS:
        	 dialog= ProgressDialog.show(SplashScreenActivity.this,"","Caricamento in corso...");
        	 break;
        default:
        }
        return dialog;
    }
}
