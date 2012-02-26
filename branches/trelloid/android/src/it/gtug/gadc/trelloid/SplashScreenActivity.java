package it.gtug.gadc.trelloid;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import it.gtug.gadc.trelloid.auth.TrelloHandle;
import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.json.JSONObject;

import java.util.*;

/**
 * Per generare un token orario
 * 
 * @see https://trello.com/1/appKey/generate
 */
public class SplashScreenActivity extends Activity {

	private static final String DEMO_BOARD = "4f3f6245e4b1f2a0023665c8";
	private final static String key = "9bd5f87e01424e4cae086ea481513c86";
	public final static String testKey = "1b59bc31a7420d12ce644d7d822161a2";
	public final static String testToken = "a30c5f4656e4003a9a84c36a25fda4f8152d3069c39a356d915cb0dcbc094e72";
	
	
	private static final String TRELLOID_TOKEN = "aq.trelloid.token";
    private static final String TRELLOID_SECRET = "aq.trelloid.secret";
    
    
//   private static final String CONSUMER_KEY=key;
//   private static final String CONSUMER_SECRET=testToken;
    
    /**
     * QUeste sono del mio account, pprovate a cambiarle con il vostro
     */
    private static final String CONSUMER_KEY="0f67a93d8fd77b03601a79c0b8773ef7";
    private static final String CONSUMER_SECRET="2b6b7c10123436e30aecd2a43a28ec8d4db4bc9132164e9b110ab279238af27d";
	private static final int DIALOG_PROGRESS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board_list);
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
		ListView lv=(ListView)findViewById(R.id.boardList);
		lv.setAdapter(new SimpleAdapter(this,listaBoards, android.R.layout.two_line_list_item, from,views));
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View item,
		        int position, long id) {
		    	showDialog(DIALOG_PROGRESS);
				
				new AsyncTask<Void, Void, Board>() {

					@Override
					protected Board doInBackground(Void... params) {
						Board board = getBoard(DEMO_BOARD);
						return board;
					}

					protected void onPostExecute(Board board) {
						
						Intent intent = new Intent();
						intent.setClass(SplashScreenActivity.this, BoardActivity.class);
						//FIXME: Ricordarsi di estrarre il titolo della board
						intent.putExtra("title","TrelloAndroid");
						intent.putExtra("board", board);
						intent.putExtra("boardId", DEMO_BOARD);
						dismissDialog(DIALOG_PROGRESS);
						startActivity(intent);
					}			

				}.execute();
		    
		    }
		    });
		
		
		
		
	    //Non ci interessa passargli una view. Serve solo per il click sul pulsante ;)
		authTrello(null);
		
		
		
		 
		
        
	}
	/**
	 * Ha come parametro view per essere richiamato dall'onclick nel layout
	 * @param view
	 */
	 public void showToken(View view){
	         String token= PreferenceManager.getDefaultSharedPreferences(this).getString(TRELLOID_TOKEN, null);
	        showToast("Token:"+token);
	 }
	 /**
	  * Cancella le credenziali registrate in seguito all'autenticazione
	 * Ha come parametro view per essere richiamato dall'onclick nel layout
	 * @param view
	 */
	 public void clearPreferences(View view){
		 SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		    Editor editor = settings.edit();
		    editor.clear();
		    editor.commit();
		    
	        showToast("Credenziali pulite");
	 }
	 /**
	  * Prende in carico la visualizzazione e la gestione dell'autenticazione
	  * Ha come parametro view per essere richiamato dall'onclick nel layout
	  * @param view
	  */
	public void authTrello(View view){
       
		
       TrelloHandle handler = new TrelloHandle(this, CONSUMER_KEY, CONSUMER_SECRET){
           
//           @Override
//			public boolean authenticated() {
//                   if(token!=null){
//                   Context context = getApplicationContext();
//	           		CharSequence text = "Authenticated!Token:"+fetchStoredPreference(TRELLOID_TOKEN);
//	           		int duration = Toast.LENGTH_LONG;
//	
//	           		Toast toast = Toast.makeText(context, text, duration);
//	           		toast.show();
//                   }
//	           		return token != null && secret != null;
//	           		
//           }
           
           
   };
        
        String url = "https://trello.com/1/authorize?key="+CONSUMER_KEY+"&name=Trelloid&response_type=fragment&expiration=1day&scope=read,write";
        AQuery aq = new AQuery(SplashScreenActivity.this);
        aq.auth(handler).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){});
        

   
   
        
        
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
        	 dialog= ProgressDialog.show(SplashScreenActivity.this, "", "Caricamento in corso...");
        	 break;
        default:
        }
        return dialog;
    }
    /**
     * MOstra un popup con il messaggio indicato
     * @param message
     */
    private void showToast(String message){
    	
        Context context = getApplicationContext();

		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
    }
}
