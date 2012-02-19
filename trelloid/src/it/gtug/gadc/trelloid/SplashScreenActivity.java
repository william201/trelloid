package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.services.BoardService;
import it.gtug.gadc.trelloid.services.MemberService;

import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Per generare un token orario
 * @see https://trello.com/1/appKey/generate
 */
public class SplashScreenActivity extends ListActivity {

    private final static String key = "5ab5ad43320989a74b677ab82a349db2";
    public final static String testKey="5ab5ad43320989a74b677ab82a349db2";
	public final static String testToken="cf3077895e0c6c523bb9b9e601fdc449258e0ae85735be5b0a2cde50dceddb9a";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        
        MemberService service = ProxyFactory.create(MemberService.class, "https://api.trello.com");
        BoardService boardService = ProxyFactory.create(BoardService.class, "https://api.trello.com");

		List<Board> boards = service.listMemberBoards("fabriziooo", key);
        
        for(Board board: boards) {
            System.err.println("id: "+board.getId());
            System.err.println("desc: "+board.getName());
        	
            List<CardContainer> listOfCardContainer = boardService.findListsForBoard(board.getId(), key);
            
            for(CardContainer cardContainer: listOfCardContainer) {
                System.err.println("\tid: "+cardContainer.getId());
                System.err.println("\tdesc: "+cardContainer.getName());
                
                for(Card card: cardContainer.getCards()) {
                    System.err.println("\t\tid: "+card.getId());
                    System.err.println("\t\tdesc: "+card.getName());
                	
                }
            	
            }
            
        }
		
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
