
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;

import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

public class EditCardActivity extends FragmentActivity {

    private String cardId;

    private ProgressDialog queryDialog;
    
    private Card card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String stringExtra = getIntent().getStringExtra("cardId");
        cardId = stringExtra != null ? stringExtra : "4f3fa1c0b23069041241fbfc";
        setContentView(R.layout.edit_card);
        
        showDialog(1);

        Bundle bundle = new Bundle();
        bundle.putString("cardId", cardId);

        getSupportLoaderManager().initLoader(0, bundle, new LoaderCallbacks<Card>() {

            public Loader<Card> onCreateLoader(int id, Bundle args) {
                return new CardLoader(EditCardActivity.this, args.getString("cardId"));
            }

            public void onLoadFinished(Loader<Card> arg0, Card loadedCard) {
                card = loadedCard;
                AQuery aq = new AQuery(EditCardActivity.this);

                aq.id(R.id.title).text(card.getName());
                aq.id(R.id.description).text(card.getDesc());

                queryDialog.dismiss();
            }

            public void onLoaderReset(Loader<Card> arg0) {
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        queryDialog = new ProgressDialog(this);
        queryDialog.setIndeterminate(true);
        queryDialog.setCancelable(false);
        queryDialog.setInverseBackgroundForced(false);
        queryDialog.setTitle("Loading card...");
        queryDialog.setMessage("Waiting for data...");
        return queryDialog;
    }

    @Override
    public boolean onCreateOptionsMenu(android.support.v4.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.support.v4.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        AQuery aq = new AQuery(EditCardActivity.this);

        card.setName(aq.id(R.id.title).getText().toString());
        card.setDesc(aq.id(R.id.description).getText().toString());
        
        setResult(RESULT_OK, new Intent().putExtra("card", card));
        finish();
    }

    private String getToken() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
                TrelloidApplication.TRELLOID_TOKEN, null);
    }

    private final class CommentAdapter extends ArrayAdapter<Comment> {
        private CommentAdapter(Context context, int resource, int textViewResourceId,
                List<Comment> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TrelloQuery aq = new TrelloQuery(view);

            String idMemberCreator = getItem(position).getIdMemberCreator();
            aq.memberImage(idMemberCreator);

            return view;
        }
    }

}
