package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.auth.TrelloHandle;
import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.services.BoardService;
import it.gtug.gadc.trelloid.services.MemberService;
import it.gtug.gadc.trelloid.utils.Const;
import it.gtug.trelloid.adapters.BoardsAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.Window;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class HomeActivity extends FragmentActivity {
	private static final String AVATAR_IMAGE_EXTENSION = ".png";
	private static final String AVATAR_PIXEL_DIMENSIONS = "50";
	private static final String DOMAIN_TRELLO_AVATARS = "https://trello-avatars.s3.amazonaws.com/";
	/*
	 * Gli id dei pulsanti
	 */
	private static final int PULSANTE_AVATAR=1;
	private static final int PULSANTE_NEW_BOARD=2;
	private static final int PULSANTE_PREFERENCES=3;
	
	private static ProgressDialog dialog;
	/*
	 * Gli id dei nostri dialog
	 */
	private static final int DIALOG_PROGRESS = 0;
	private static final int DIALOG_LOAD_USER_DATA = 1;
	
	private static final String TAG="HomeActivity";
	
	
	
	private Member me;
	private Drawable myAvatar;
	
	private static int THEME = R.style.Theme_Trelloid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(THEME);
        setContentView(R.layout.boardlist);
        updateUserOnActionBar(this.me);
  		doAuthTrello();
	}

	/**
	 * Invoco l'autenticazione! Nessuna view, non ho premuto su un pulsante ma la chiamo da programma.
	 */
	private void doAuthTrello() {
		authTrello(null);
	}
	
	
	/**
	 * Imposto il nome e cognome come titolo e sottotitolo della actionBar
	 */
	@Override
	protected void onStart() {
		
		super.onStart();
	
	}
	/**
	 * Aggiorna i dati mostrati nella actionBar con quelli contenuti nel Member this.me
	 */
	private void updateUserOnActionBar(Member me) {
		ActionBar actionBar = this.getSupportActionBar();
		if(me==null){
			actionBar.setTitle("Loading..");
			actionBar.setSubtitle(" ");
	        //actionBar.setDisplayHomeAsUpEnabled(true);
			}
		else{
			actionBar.setTitle(me.getUsername());
			actionBar.setSubtitle(me.getFullName());
			
		}
	}


	/**
	 * Gestisce la creazione del menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	 menu.add(0,PULSANTE_AVATAR,1,"Biography")
	    .setIcon(loadUserAvatar(null))
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	
	 return super.onCreateOptionsMenu(menu);
        
	}
	
	/**
	 * Passa di qua ogni invocazionedi invalidateOptionsMenu()
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		if(this.getMyAvatar()!=null){
			menu.findItem(PULSANTE_AVATAR).setIcon(myAvatar);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	/**
	 * Crea l'immagine con i bordi per l'avatar
	 * @return
	 */
	private Drawable loadUserAvatar(String avatarHash) {
		
		Bitmap bitmapOrg;
		
	    HttpURLConnection connection=null;
	    if(avatarHash!=null){
	    	String avatarUrl = DOMAIN_TRELLO_AVATARS+avatarHash+"/"+AVATAR_PIXEL_DIMENSIONS+AVATAR_IMAGE_EXTENSION;
	    
			try {			
				connection = (HttpURLConnection)new URL(avatarUrl).openConnection();
				connection.setRequestProperty("User-agent","Mozilla/4.0");
			    connection.connect();
			    InputStream input = connection.getInputStream();
			    bitmapOrg = BitmapFactory.decodeStream(input);
			} catch (MalformedURLException e) {
				showToast("Impossibile ottenere l'avatar dell'utente: "+e.getMessage()+" "+e.getCause());
				Log.e(TAG,"Impossibile reperire avatar da url: "+avatarUrl,e);
				bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.sample_avatar);
			} catch (IOException e) {
				showToast("Impossibile ottenere l'avatar dell'utente: "+e.getMessage()+" "+e.getCause());
				Log.e(TAG,"Impossibile reperire avatar da url: "+avatarUrl,e);
				bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.sample_avatar);
			}
	    }else{
	    	bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.loading_avatar);
	    }

		int borderSize = 5;
		int width = bitmapOrg.getWidth();
	    int height = bitmapOrg.getHeight();
	    int newWidth = 2*borderSize+width;
	    int newHeight = 2*borderSize+height;
	    
		RectF targetRect = new RectF(borderSize, borderSize, borderSize + width, borderSize + height);
		
	    Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, bitmapOrg.getConfig());
	    Canvas canvas = new Canvas(dest);
	    //Colore bordo
	    canvas.drawColor(Color.LTGRAY);
	    canvas.drawBitmap(bitmapOrg, null, targetRect,null);
	    ImageView img=new ImageView(getApplicationContext());
	    img.setImageBitmap(dest);
	    return img.getDrawable();
	}
	
	/**
	 * Invoca la schermata delle preferences
	 * @param view
	 */
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
		
      TrelloHandle handler = new TrelloHandle(this,getApplicationContext(), Const.CONSUMER_KEY, Const.CONSUMER_SECRET);
       handler.setAppName(Const.APP_NAME);
       handler.setScope(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_TYPE_PREF, "read,write"));
       handler.setExpiration(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_EXPIRE_PREF, "30days"));
       
       String url = "https://trello.com/1/connect?key="+Const.CONSUMER_KEY+"&response_type=fragment";
       AQuery aq = new AQuery(HomeActivity.this);
       ProgressDialog authDialog=new ProgressDialog(this);
       authDialog.setIndeterminate(true);
       authDialog.setCancelable(false);
       authDialog.setInverseBackgroundForced(false);
       authDialog.setCanceledOnTouchOutside(true);
       authDialog.setTitle("Loading..");
       authDialog.setMessage("Colloquio con il server Trello in corso.."); 
       
       aq.auth(handler).progress(authDialog).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
    	   /**
    	    * Diciamo che l'autenticazione ha avuto successo. Devo scaricare le boards, l'avatar e i dati utente
    	    */
    	   @Override
		public void callback(String url, JSONObject jsnObj, AjaxStatus status) {
			super.callback(url, jsnObj, status);
			
			showDialog(DIALOG_PROGRESS);
			
			/**
			 * Ottengo le info utente
			 */
			new AsyncTask<Void, Void, Member>() {

				@Override
				protected Member doInBackground(Void... params) {
					try {
						return getMemberMe();
					} catch (Exception e) {
						Log.e(TAG, "Impossibile ottenere memberMe. Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
						//NOTA: Si può accedere alla UI solo dal Thread principale, quindi: showToast("Impossibile scaricare le info dell'utente"); andrebbe in errore
						return null;
					}
				}
				/**
				 * Devo aggiornare le info mostrate sulla action bar
				 */
				protected void onPostExecute(final Member me) {
					if(me!=null){					
						updateUserOnActionBar(me);
						/**
						 * Ottengo l'avatar dell'utente
						 */
						new AsyncTask<Void, Void, Drawable>() {

							@Override
							protected Drawable doInBackground(Void... params) {
								try {
									return loadUserAvatar(me.getAvatarHash());
								} catch (Exception e) {
									Log.e(TAG, "Impossibile ottenere avatar. AvatarHash:"+me.getAvatarHash()+" Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
									return null;
								}
							}
							/**
							 * Aggiorno i dati nella barra in alto
							 */
							protected void onPostExecute(final Drawable avatar) {
								if(avatar!=null){					
									/*
									 *Passa nel ciclo dell'onPreparateOptionsMenu
									 */
									setMyAvatar(avatar);
									invalidateOptionsMenu();
								}
								else{
									showToast("Impossibile scaricare l'avatar");
								}
							}
						}.execute();
					}else{
						showToast("Impossibile scaricare le info dell'utente");
					}
				}
			}.execute();
			
			
			
			/*
			 * Eseguo il fetch asincrono delle boards
			 */
			new AsyncTask<Void, Void, ArrayList<Board>>() {

				@Override
				protected ArrayList<Board> doInBackground(Void... params) {
					try {
						return getMyBoards();
					} catch (Exception e) {
						Log.e(TAG, "Impossibile ottenere boards utente. Member:"+me.getId()+" Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
						showToast("Impossibile scaricare le boards dell'utente");
						return new ArrayList<Board>();
					}
				}
				/**
				 * Mostro in lista le boards ottenute
				 */
				protected void onPostExecute(final ArrayList<Board> boards) {
					ListView boardsListView=(ListView)findViewById(R.id.boardsListView);
			  		boardsListView.setAdapter(new BoardsAdapter(getApplicationContext(), R.layout.boardlist_item, R.id.boardDescription, boards, HomeActivity.this));
			  		boardsListView.setOnItemClickListener(new OnItemClickListener() {
					    /**
					     * Sul click sulla board devo scaricarne i dati per passarla al metodo suvccessivo
					     */
			  			public void onItemClick(AdapterView<?> parent, View item,
					        int position, long id) {
					    	
					    	final Board selectedBoard = boards.get(position);
					    	showDialog(DIALOG_PROGRESS);
							
							new AsyncTask<Void, Void, Board>() {

								@Override
								protected Board doInBackground(Void... params) {
									Board board=null;
									try{
										board = getBoardWithLists(selectedBoard); 
									}catch (Exception e) {
										Log.e(TAG, "Impossibile ottenere le lists dell'utente. BoardId:"+selectedBoard.getId()+" Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
									}
									return board;
								}

								protected void onPostExecute(Board board) {
									
									if(board!=null){
										Intent intent = new Intent();
									
										intent.setClass(HomeActivity.this, BoardActivity.class);
										intent.putExtra("title",board.getName());
										intent.putExtra("board", board);
										intent.putExtra("boardId", board.getId());
										dismissDialog(DIALOG_PROGRESS);
										startActivity(intent);
									}else{
										showToast("Impossibile scaricare la board"+selectedBoard.getName());
									}
									
								}			

							}.execute();
					    
					    }
					    });
			  		dismissDialog(DIALOG_PROGRESS);
				}			

			}.execute();
			
			
		}

		
    	  
     		//boardsListView.setAdapter(new BoardsAdapter(getApplicationContext(), R.layout.boardlist_item, R.id.boardDescription, boards, this));
       });     
       
	}
	
	/**
	 * Metodo esemplicativo per mostrare un toast per ogni mia board
	 * @param view
	 */
	public void fetchMyBoards(View view){
		List<Board> myBoards;
		try {
			myBoards = getMyBoards();
			showToast("Ho "+myBoards.size()+ " board!");
			int i=1;
			for (Board board : myBoards) {
				showToast("Board"+i+": "+board.getName());
				i++;
			}
		} catch (Exception e) {
			showToast("impossibile scaricare boards");
		}
		
	}
	
	
	/**
	 * Ottiene le cardContainer per la board in parametro
	 * @param board
	 * @return
	 * @throws ClientResponseFailure
	 */
	private Board getBoardWithLists(Board board) throws ClientResponseFailure{
		String token=getMyToken();
		if(token==null || token.length()<1){
			
			return null;
		}
		BoardService service = ProxyFactory.create(BoardService.class,Const.HTTPS_API_TRELLO_COM);
		List<CardContainer> lists = service.findListsForBoard(board.getId(), Const.CONSUMER_KEY,token);
		board.setContainers(lists);
		
		return board;
	}
	/**
	 * Ottiene le boards di un member
	 * @return
	 * @throws ClientResponseFailure
	 */
	private ArrayList<Board> getMyBoards() throws Exception{
		MemberService service= ProxyFactory.create(MemberService.class, Const.HTTPS_API_TRELLO_COM);
		Member me=getMemberMe();
		if( me==null){
			showToast("Occorre effettuare l'autenticazione prima. Utente me: null");
		}
		
		return service.findBoardsWichHeIsMember(Const.CONSUMER_KEY,getMyToken());
	}
	
	//TODO: Troviamo una collocazione accessibile da più parti per questi metodi? CI vorrebbe un Service del Service XD
	/**
	 * Recupera il member a cui fa capo il token di autenticazione
	 * @return
	 * @throws ClientResponseFailure
	 */
	private Member getMemberMe() throws Exception{
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
		MemberService memberService = ProxyFactory.create(MemberService.class, Const.HTTPS_API_TRELLO_COM);
		Map<String, Member> membersCache = application.getMembersCache();
		Member me = memberService.findMe(Const.CONSUMER_KEY, token);
		membersCache.put(me.getId(), me);
		return me;
	}
	private String getMyToken() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(Const.TRELLOID_TOKEN_PREFERENCE, null);
	}
	/**
    * Gestisce le dialog che si possono aprire
    */
   protected Dialog onCreateDialog(int id) {
       Dialog dialog=null;
       switch(id) {
       case DIALOG_PROGRESS:
       	 dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento in corso...");
       	 break;
       case DIALOG_LOAD_USER_DATA:
         	 dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento dati utente in corso...");
         	 break;
       default:
       }
       return dialog;
   }
   /**
    * Mostra un popup con il messaggio indicato
    * @param message
    */
   private void showToast(String message){
   	
       Context context = getApplicationContext();

		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
   }

public Member getMe() {
	return me;
}

public void setMe(Member me) {
	this.me = me;
}

public Drawable getMyAvatar() {
	return myAvatar;
}

public void setMyAvatar(Drawable avatar) {
	this.myAvatar = avatar;
}
}
