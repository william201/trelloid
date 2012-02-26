package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.auth.TrelloHandle;
import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.BoardService;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.json.JSONObject;

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
	
	
	public static final String TRELLOID_TOKEN = "aq.trelloid.token";
    private static final String TRELLOID_SECRET = "aq.trelloid.secret";
    private static final String TRELLOID_MEMBER_ID = "aq.trelloid.secret";
    
    
   
    /**
     * QUeste sono del mio account, pprovate a cambiarle con il vostro
     */
//    public static final String CONSUMER_KEY="0f67a93d8fd77b03601a79c0b8773ef7";
//    private static final String CONSUMER_SECRET="2b6b7c10123436e30aecd2a43a28ec8d4db4bc9132164e9b110ab279238af27d";
    
    //SOno i secret di Trelloid ;)
    public static final String CONSUMER_KEY="287616866fb290d0ede084f648112848";
  	private static final String CONSUMER_SECRET="fb29e30637c7a10b595d42879bd08f529f65479eafd11449aff787bc749b58ea";
    
	private static final String APP_NAME = "Trelloid";
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
	         String token= getMyToken();
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
	 public void showMainPreferences(View view){
		 Intent settingsActivity = new Intent(getBaseContext(),
                 MainPreferencesActivity.class);
		 startActivity(settingsActivity);
	 }
	 /**
	  * Prende in carico la visualizzazione e la gestione dell'autenticazione
	  * Ha come parametro view per essere richiamato dall'onclick nel layout
	  * @param view
	  */
	public void authTrello(View view){
		
       TrelloHandle handler = new TrelloHandle(this, CONSUMER_KEY, CONSUMER_SECRET);
        handler.setAppName(SplashScreenActivity.APP_NAME);
        handler.setScope(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_TYPE_PREF, "read,write"));
        handler.setExpiration(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_EXPIRE_PREF, "30days"));
        
        String url = "https://trello.com/1/authorize?key="+CONSUMER_KEY+"&response_type=fragment";
        AQuery aq = new AQuery(SplashScreenActivity.this);
        ProgressDialog authDialog=new ProgressDialog(this);
        authDialog.setIndeterminate(true);
        authDialog.setCancelable(false);
        authDialog.setInverseBackgroundForced(false);
        authDialog.setCanceledOnTouchOutside(true);
        authDialog.setTitle("Loading..");
        authDialog.setMessage("Colloquio con il server Trello in corso.."); 
        
        aq.auth(handler).progress(authDialog).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
        	
        	
        });     
        
	}
	
	/**
	 * Questo va implementato in asincrono e fatto solo dopo che abbiamo registrato nelle
	 * preferences il token
	 * @param view
	 */
	public void fetchMyBoards(View view){
		List<Board> myBoards = getMyBoards();
		showToast("Ho "+myBoards.size()+ " board!");
		int i=1;
		for (Board board : myBoards) {
			showToast("Board"+i+": "+board.getName());
			i++;
		}
	}
	
	
	private Board getBoard(String boardId) {
		BoardService service = ProxyFactory.create(BoardService.class,"https://api.trello.com");
		List<CardContainer> lists = service.findListsForBoard(boardId, SplashScreenActivity.testKey);

		Board board = new Board();
		board.setId(lists.get(0).getIdBoard());
		board.setContainers(lists);
		
		return board;
	}
	
	private List<Board> getMyBoards(){
		MemberService service= ProxyFactory.create(MemberService.class, "https://api.trello.com");
		Member me=getMemberMe();
		if( me==null){
			showToast("Occorre effettuare l'autenticazione prima. Utente me: null");
		}
		
		return service.findBoardsWichHeIsMember(CONSUMER_KEY,getMyToken());
	}
	
	//TODO: Troviamo una collocazione accessibile da più parti per questi metodi? CI vorrebbe un Service del Service XD
	/**
	 * Recupera il member a cui fa capo il token di autenticazione
	 * @return
	 */
	private Member getMemberMe() {
		String token=getMyToken();
		if(token==null || token.length()<1){
			
			return null;
		}
		TrelloidApplication application = (TrelloidApplication) getApplication();

//		Si può pensare di utilizzare la cache? COme si gestisce l'eventuale logout??
		
//		Map<String, Member> membersCache = application.getMembersCache();
//		Member member = membersCache.get(idMemberCreator);
//		if (member == null) {
//			MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
//			member = memberService.findMembers(idMemberCreator, SplashScreenActivity.CONSUMER_KEY);
//			membersCache.put(idMemberCreator, member);
//		}
		MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
		Map<String, Member> membersCache = application.getMembersCache();
		Member me = memberService.findMe(CONSUMER_KEY, token);
		membersCache.put(me.getId(), me);
		return me;
	}
	private String getMyToken() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(TRELLOID_TOKEN, null);
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
