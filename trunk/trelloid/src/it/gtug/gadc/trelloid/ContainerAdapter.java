package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.CardContainer;

import java.util.List;

import it.gtug.gadc.trelloid.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

//Classe da cancellare
@Deprecated
public class ContainerAdapter extends ArrayAdapter<CardContainer> {

	private Context context;
	private List<CardContainer> allContainer;


	public ContainerAdapter(Context context, int itemListResourceId,
			List<CardContainer> containers) {
		
		super(context, itemListResourceId, containers);
		this.context = context;
		this.allContainer = containers;
	}

	public int getCount() {
		return allContainer.size();
	}

	public CardContainer getItem(int position) {		
		return allContainer.get(position);
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			//Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_list, parent, false);
			//Log.d(tag, "Successfully completed XML Row Inflation!");
		}
		CardContainer prova=getItem(position);

		ImageView listIcon = (ImageView) row.findViewById(R.id.list_icon );
		TextView listName = (TextView) row.findViewById(R.id.list_name);
		
		listName.setText(prova.getName());
		

		listIcon.setImageResource(R.drawable.t_launcher);

		return row;
	}

}
