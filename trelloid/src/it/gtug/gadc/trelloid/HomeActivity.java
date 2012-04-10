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

//import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

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
    TrelloidApplication app=TrelloidApplication.getInstance();
    
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
	private static ProgressDialog authDialog;

	/*
	 * Gli id dei nostri dialog
	 */
	private static final int DIALOG_PROGRESS = 0;
	private static final int DIALOG_LOAD_USER_DATA = 1;
	private static final int DIALOG_TEST_TOKEN = 2;
	public static final int DIALOG_LOAD_USER_BOARD = 3;
	public static final int DIALOG_LOAD_USER_BOARDS = 4;
	public static final int DIALOG_LOAD_USER_AVATAR = 5;
	
	private static final String TAG="HomeActivity";
 
   
	private TokenTesterAsyncTask tokenTester=null;
	private LoadAvatarAsyncTask avatarLoader=null;
	private UserInfoLoaderAsyncTask userInfoLoader=null;
	private UserBoardsLoaderAsyncTask userBoardsLoader=null;

    private boolean trelloidWantsToShowADialog;
	
	
	private static int THEME = R.style.Theme_Trelloid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(THEME);
        setContentView(R.layout.boardlist);
        
        updateUserOnActionBar(null);
        
        this.tokenTester = new TokenTesterAsyncTask();
        this.avatarLoader= new LoadAvatarAsyncTask();
        this.userInfoLoader=new UserInfoLoaderAsyncTask();
        this.userBoardsLoader=new UserBoardsLoaderAsyncTask();
        
        
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(!app.isAuthenticated()||!(app.isMeLoaded()&&app.isBoardsLoaded())){
            tokenTester.execute();
        }else{
            //Devo solo ripristinare i dati sullo schermo
            ListView boardsListView=(ListView)findViewById(R.id.boardsListView);
            setBoardsListView(app.getBoards(), boardsListView);
            updateUserOnActionBar(app.getMember(app.getMyMemberId()));                        
        }
        
    }

    /**
	 * Aggiorna i dati mostrati nella actionBar con quelli contenuti nel Member this.me
	 */
	private void updateUserOnActionBar(Member me) {
		ActionBar actionBar = this.getSupportActionBar();
		if(me==null){
			actionBar.setTitle("Loading..");
			actionBar.setSubtitle(" ");
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
		
		Drawable avatar = app.getMyAvatar();
        if(avatar!=null){
			menu.findItem(PULSANTE_AVATAR).setIcon(avatar);
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
	    	ImageView img=new ImageView(getApplicationContext());
	        img.setImageBitmap( BitmapFactory.decodeResource(getResources(),R.drawable.loading_avatar));
	        return img.getDrawable();
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
	  * Prende in carico la  gestione dell'autenticazione
	  */
	public void doAuthTrello(){
		//Questo viene invocato solo se l'autenticazione ha avuto successo
       TrelloHandle handler = new TrelloHandle(this,getApplicationContext(), Const.CONSUMER_KEY, Const.CONSUMER_SECRET,new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String arg0, JSONObject arg1, AjaxStatus arg2) {
                app.setAuthenticated(true);
                userBoardsLoader.execute();
            }
           
       });
       handler.setAppName(Const.APP_NAME);
       handler.setScope(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_TYPE_PREF, "read,write"));
       handler.setExpiration(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MainPreferencesActivity.AUTH_EXPIRE_PREF, "30days"));
       String url = "https://trello.com/1/connect?key="+Const.CONSUMER_KEY+"&response_type=fragment";
       AQuery aq = new AQuery(HomeActivity.this);
       authDialog=new ProgressDialog(this);
       authDialog.setIndeterminate(true);
       authDialog.setCancelable(false);
       authDialog.setInverseBackgroundForced(false);
       authDialog.setCanceledOnTouchOutside(true);
       authDialog.setTitle("Loading..");
       authDialog.setMessage("Autenticazione con il server Trello in corso.."); 
       trelloidWantsToShowADialog=true;
       
       aq.auth(handler).progress(authDialog).
       ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
    	   /**
    	    * Diciamo che l'autenticazione ha avuto successo. Devo scaricare le boards, l'avatar e i dati utente
    	    */
    	   @Override
		public void callback(String url, JSONObject jsnObj, AjaxStatus status) {
			super.callback(url, jsnObj, status);
			//FIXME: In realt� il procedimento di autenticazione non � proprio esatto.. arrivo qui con status 401! 
			//Andrebbe realizzata l'autenticazione con un altra libreria?
			 String token=PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString(TrelloidApplication.TRELLOID_TOKEN, null);
			if(token!=null){
			    app.setAuthenticated(true);
			    userBoardsLoader.execute();
			}else{
			    //FIXME: ANdrebbe data una via di uscita elegante per scegliere se uscire dall'app o riprovare l'autenticazione
			    //showToast("Non si � autenticati.. Il processo di autenticazione verr� rieseguito.");
			    //FIXME: Pu� succedere che risultiamo no nautenticati anche se un procedimento � andato a buon fine, visto l'asincronicit�
			    //Occorre gestire un handler sull'autenticazione nel trelloHandle
			    //doAuthTrello();
			}
			
			
    	   }
    	   
       }); 
       
       
       
	}
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
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
	
	//TODO: Troviamo una collocazione accessibile da pi� parti per questi metodi? CI vorrebbe un Service del Service XD
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
		

//		Si pu� pensare di utilizzare la cache? COme si gestisce l'eventuale logout??
		
//		Map<String, Member> membersCache = application.getMembersCache();
//		Member member = membersCache.get(idMemberCreator);
//		if (member == null) {
//			MemberService memberService = ProxyFactory.create(MemberService.class, "https://api.trello.com");
//			member = memberService.findMembers(idMemberCreator, SplashScreenActivity.CONSUMER_KEY);
//			membersCache.put(idMemberCreator, member);
//		}
		MemberService memberService = ProxyFactory.create(MemberService.class, Const.HTTPS_API_TRELLO_COM);
		Map<String, Member> membersCache = app.getMembersCache();
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
       if(this.trelloidWantsToShowADialog==false){
           return null;
       }
       trelloidWantsToShowADialog=false;
       switch(id) {
       case DIALOG_PROGRESS:
       	 dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento in corso...");
       	 break;
       case DIALOG_LOAD_USER_DATA:
         	 dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento dati utente in corso...");
         	 break;
       case DIALOG_TEST_TOKEN:
           dialog= ProgressDialog.show(HomeActivity.this,"","Verifica credenziali in corso...");
           break;
       case DIALOG_LOAD_USER_BOARDS:
           dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento delle boards dell'utente in corso...");
           break;
       case DIALOG_LOAD_USER_BOARD:
           dialog= ProgressDialog.show(HomeActivity.this,"","Caricamento delle liste della board in corso...");
           break; 
       case DIALOG_LOAD_USER_AVATAR:
           dialog=ProgressDialog.show(HomeActivity.this,"","Caricamento dell'avatar in corso...");
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


/**
 * Imposta le boards su una listView
 * @param boards
 * @param boardsListView
 */
private void setBoardsListView(final ArrayList<Board> boards, ListView boardsListView) {
    boardsListView.setAdapter(new BoardsAdapter(getApplicationContext(), R.layout.boardlist_item, R.id.boardDescription, boards, HomeActivity.this));
    boardsListView.setOnItemClickListener(new OnItemClickListener() {
        /**
         * Sul click sulla board devo scaricarne i dati per passarla al metodo successivo
         */
        public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
            BoardListsLoaderAsyncTask boardListsLoader=new BoardListsLoaderAsyncTask();
            boardListsLoader.execute(boards.get(position));       
            }
        });
    }
    /**
     * Task per il check dell token memorizzato enlle preferences
     * @author malo
     *
     */
    private class TokenTesterAsyncTask extends AsyncTask<Void, Void, Boolean> {
    
        private static final String TAG = "TokenTesterAsyncTask";

        @Override
        protected void onPreExecute() {
            
            super.onPreExecute();
            showTrelloDialog(DIALOG_TEST_TOKEN);
        }
    
        @Override
        protected Boolean doInBackground(Void... params) {
            
            URL url=null;
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            try {             
                url = new URL(Const.HTTPS_API_TRELLO_COM+"/1/tokens/"+settings.getString(TrelloidApplication.TRELLOID_TOKEN, "blank")+"?key="+Const.CONSUMER_KEY);
            } catch (Exception e) {
                Log.e(TAG, "Impossibile determinare validit� token:"+url,e);
                return null;
            }
            Integer responseCode=null;
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                responseCode = urlConnection.getResponseCode();
            } catch (IOException e) {
                
                Log.e(TAG, "Impossibile ricavare lo stato della connessione http all'url: "+url);
                return false;
            }
            //FIXME: In realt� dovrei testare la validit� dell'expiration.
            //Per ora mi basta che il server NON risponda 401(Unauthorized) ma solo un codice di successo 2xx
            if(responseCode >=200&&responseCode<300){
                Log.i(TAG, "Token valido: "+url);
                return true;
                
            }
            else {
                Log.w(TAG,"Token non valido: "+url);
                
                Editor editor = settings.edit();
                //Sbianco il token
                editor.putString(TrelloidApplication.TRELLOID_TOKEN, null);
                editor.putString(TrelloidApplication.TRELLOID_SECRET, null);
                editor.commit();
                return false;
            }
        }
        
        protected void onPostExecute(final Boolean valid) {
            try {
                dismissDialog(DIALOG_TEST_TOKEN);
            } catch (Exception e) {
                Log.e(this.TAG, "IMpossibile dismettere la dialog DIALOG_TEST_TOKEN",e);
            }
            if(valid!=null&&valid.booleanValue()==true){   
                app.setAuthenticated(true);
                userBoardsLoader.execute();
                
            }
            else{
                doAuthTrello();
            }
        
        }
    
    
    }
    /**
     * Task per reperire l'avatar dell'utente
     * @author malo
     *
     */
    private class LoadAvatarAsyncTask extends AsyncTask<String, Void, Drawable>{
    

        private final String TAG = "LoadAvatarAsyncTask";
        
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showTrelloDialog(DIALOG_LOAD_USER_AVATAR);
        }
        @Override
        protected Drawable doInBackground(String... params) {
            if(params.length<1){
                Log.e(this.TAG,"Nessun hashtag fra i parametri");
                return null;
            }
            String avatarHash=params[0];
            try {
                return loadUserAvatar(avatarHash);
            } catch (Exception e) {
                Log.e(TAG, "Impossibile ottenere avatar. AvatarHash:"+avatarHash+" Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
                return null;
            }
        }
        /**
         * Aggiorno i dati nella barra in alto
         */
        protected void onPostExecute(final Drawable avatar) {
            try {
                dismissDialog(DIALOG_LOAD_USER_AVATAR);
            } catch (Exception e) {
                Log.e(this.TAG, "Impossibile dismettere dialog DIALOG_LOAD_USER_AVATAR", e);
            }
            if(avatar!=null){                   
                /*
                 *Passa nel ciclo dell'onPreparateOptionsMenu
                 */
                app.setMyAvatar(avatar);
                invalidateOptionsMenu();
            }
            else{
                showToast("Impossibile scaricare l'avatar");
            }
            
        }
    }
    
    /**
     * Permette di repereire le info utente
     */
    private class UserInfoLoaderAsyncTask extends AsyncTask<Void, Void, Member>{
        private  final String TAG = "UserInfoLoaderAsyncTask";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showTrelloDialog(DIALOG_LOAD_USER_DATA);
        }
        
        @Override
        protected Member doInBackground(Void... params) {
            try {
                return getMemberMe();
            } catch (Exception e) {
                Log.e(this.TAG, "Impossibile ottenere memberMe. Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
                return null;
            }
        }
        /**
         * Devo aggiornare le info mostrate sulla action bar
         */
        protected void onPostExecute(final Member me) {
            try {
                dismissDialog(DIALOG_LOAD_USER_DATA);
            } catch (Exception e) {
                Log.e(this.TAG,"Impossibile dismettere la dialog DIALOG_LOAD_USER_DATA",e);
            }
            if(me!=null){    
                
                app.setMyMemberId(me.getId());
                updateUserOnActionBar(me);
                avatarLoader.execute(me.getAvatarHash());
            }else{
                
                showToast("Impossibile scaricare le info dell'utente");
            }
        }
    }
    
    /**
     * AsyncTask per ottenere la board
     */
    private class BoardListsLoaderAsyncTask extends AsyncTask<Board, Void, Board> {

        private  final String TAG = "BoardListsLoaderAsyncTask";
        
        @Override
        protected void onPreExecute() {
            
            super.onPreExecute();
            showTrelloDialog(DIALOG_LOAD_USER_BOARD);
        }

        @Override
        protected Board doInBackground(Board... params) {
            if(params.length<1){
                Log.e(this.TAG, "Occorre fornire almeno una board per ottenerne le lists");
            }
            
            Board board=params[0];
            try{
                board = getBoardWithLists(board); 
            }catch (Exception e) {
                Log.e(TAG, "Impossibile ottenere le lists dell'utente. BoardId:"+board.getId()+" Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
            }
            return board;
        }

        protected void onPostExecute(Board board) {
            try {
                dismissDialog(DIALOG_LOAD_USER_BOARD);
            } catch (Exception e) {
                Log.e(this.TAG,"Impossibile dismettere la dialog DIALOG_LOAD_USER_BOARD",e);
            }
            if(board!=null){
                Intent intent = new Intent();
            
                intent.setClass(HomeActivity.this, BoardActivity.class);
                intent.putExtra("title",board.getName());
                intent.putExtra("board", board);
                intent.putExtra("boardId", board.getId());
                startActivity(intent);
            }else{
                showToast("Impossibile scaricare le lists della board");
            }
            
        }           

    }
    /**
     * AsyncTask per ottenere le board dell'utente che ha il token registrato
     */
    private class UserBoardsLoaderAsyncTask extends AsyncTask<Void, Void, ArrayList<Board>>{
            
        private static final String TAG = "UserBoardsLoaderAsyncTask";
        @Override
        protected void onPreExecute() {
            
            super.onPreExecute();
            showTrelloDialog(DIALOG_LOAD_USER_BOARDS);
        }
        @Override
        protected ArrayList<Board> doInBackground(Void... params) {
           
            try {
                return getMyBoards();
            } catch (Exception e) {
                Log.e(TAG, "Impossibile ottenere boards utente. Token:"+getMyToken()+" ConsumerKey"+Const.CONSUMER_KEY,e);
                //showToast("Impossibile scaricare le boards dell'utente");
                
                return new ArrayList<Board>();
            }
        }
        /**
         * Mostro in lista le boards ottenute
         */
        protected void onPostExecute(final ArrayList<Board> boards) {
            try {
                dismissDialog(DIALOG_LOAD_USER_BOARDS);
            } catch (Exception e) {
                Log.e(this.TAG, "Impossibile dismettere dialog DIALOG_LOAD_USER_BOARDS", e);
            }
            app.setBoards(boards);
            
            ListView boardsListView=(ListView)findViewById(R.id.boardsListView);
            setBoardsListView(boards, boardsListView);
            userInfoLoader.execute();
            
        }
                    

    }
    /**
     * Workaround per gestire problemi di dismiss dialog nella rotazione del dispositivo.
     * Provare a passare alle FragmentDialog
     * @param dialogId
     */
    public void showTrelloDialog(int dialogId){
        // http://stackoverflow.com/questions/891451/android-dismissdialog-does-not-dismiss-the-dialog
        this.trelloidWantsToShowADialog=true;
        showDialog(dialogId);
    }
    
    
}
