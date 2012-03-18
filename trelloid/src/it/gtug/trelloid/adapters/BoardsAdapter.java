package it.gtug.trelloid.adapters;

import it.gtug.gadc.trelloid.R;
import it.gtug.gadc.trelloid.model.Board;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BoardsAdapter extends ArrayAdapter<Board> {
	
	private static final String TAG = "ManifestazioneAdapter";
	private Context context;
	private ArrayList<Board> items;
	private Activity activity;
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @param textViewResourceId
	 * @param items
	 * @param act
	 */
	public BoardsAdapter(Context context, int resourceId,int textViewResourceId,ArrayList<Board> items, Activity act) {
		super(context, resourceId, textViewResourceId, items);
		this.context=context;
		this.activity=act;
		this.items=items;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		super.getView(position, convertView, parent);
		View res = convertView;

        if (res == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            res = vi.inflate(R.layout.boardlist_item, null);
        }

        Board board= items.get(position);
        
      
        if (board != null) {
            
            TextView descBoard = (TextView) res.findViewById(R.id.boardDescription);
            
            //ImageView icon=(ImageView) res.findViewById(R.id.boardVisibilityIcon);
            
            ViewGroup itemView=(ViewGroup)descBoard.getParent();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        	params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            ImageView visibilityIcon= new ImageView(context);
            
	            //FIXME:Come faccio a sapere se la board è pubblica o privata? --board.isPublic()
	            if(board.isPublic()){
		            visibilityIcon.setImageResource(R.drawable.unlocked_icon);
		           // publicIcon.setOnClickListener(new ImageViewOnClickListener(...));
		           
	            }else{
	            	 visibilityIcon.setImageResource(R.drawable.locked_icon);
	            }
            visibilityIcon.setLayoutParams(params);
            itemView.addView(visibilityIcon);
	        descBoard.setText(board.getName());
        }
        return res;
	}
	
	
	
	//FIXME: implementare un longClickListener per cambaire l'action split bar in funzione dell'elemento cliccato
}
