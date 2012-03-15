
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.TokenData;

import it.gtug.gadc.trelloid.services.MemberService;
import it.gtug.gadc.trelloid.services.TokenService;
import android.preference.PreferenceManager;

import java.util.List;
import org.jboss.resteasy.client.ProxyFactory;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class BoardListActivity extends ListActivity {
    private static final int DIALOG_PROGRESS = 0;
    private BoardListAdapter boliAdapter;
    protected List<Board> boardLists;
    private AsyncTask<Void, Void, Void> execute;
    protected ProgressDialog queringDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queringDialog = new ProgressDialog(this);
        queringDialog.setIndeterminate(true);
        queringDialog.setCancelable(false);
        queringDialog.setInverseBackgroundForced(false);
        //queringDialog.setCanceledOnTouchOutside(true);
        queringDialog.setTitle("Loading boards..");
        queringDialog.setMessage("Waiting for data...");
        queringDialog.show();
        
        new AsyncTask<Void, Void, List<Board>>() {

            @Override
            protected List<Board> doInBackground(Void... params) {               
                return getBoardList();    
            }
            
            @Override
            protected void onPostExecute(List<Board> blist) {
                boardLists=blist;
                ListView lv = getListView();                
                boliAdapter = new BoardListAdapter(getApplicationContext(), boardLists);
                lv.setAdapter(boliAdapter);
                queringDialog.dismiss();
            }
        }.execute();
       
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Board board = boliAdapter.getItem(position);
        
        Intent intent = new Intent();
        intent.setClass(BoardListActivity.this, BoardActivity.class);

        intent.putExtra("title",board.getName());
        intent.putExtra("board", board);
        intent.putExtra("boardId", board.getId());

        startActivity(intent);
    }
    

    private String getToken() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
                TrelloidApplication.TRELLOID_TOKEN, null);
    }

    private List<Board> getBoardList() {
        String token = getToken();
        TokenService tiServe=  ProxyFactory.create(TokenService.class,"https://api.trello.com");
        TokenData myTok = tiServe.getTokenMemberInfo(token, TrelloidApplication.CONSUMER_KEY);
        
        MemberService service = ProxyFactory.create(MemberService.class, "https://api.trello.com");

        List<Board> listOB = service.listMemberBoards(myTok.getUsername(), TrelloidApplication.CONSUMER_KEY,token);
        //List<Board> listOB = service.findPublicBoardsWichHeIsMember(TrelloidApplication.CONSUMER_KEY, getToken());
        return listOB;
    }

    class BoardListAdapter extends BaseAdapter {
        private Context context;
        private List<Board> allBoards;

        public BoardListAdapter(Context context,List<Board> objects) {           
            this.context = context;
            this.allBoards = objects;
        }

        public int getCount() {
            return allBoards.size();
        }

        public Board getItem(int position) {
            return allBoards.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                // ROW INFLATION
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_list, parent, false);
            }
            Board corrente = getItem(position);

            TextView boardName = (TextView)row.findViewById(R.id.list_name);
            boardName.setText(corrente.getName());
            ImageView listIcon = (ImageView) row.findViewById(R.id.list_icon );
            listIcon.setImageResource(R.drawable.ic_launcher);
            TextView boardDesc = (TextView)row.findViewById(R.id.list_description);
            boardDesc.setText(corrente.getDesc());
            
            return row;
        }

        public long getItemId(int position) {
            return (long)position;
        }

    }

}
