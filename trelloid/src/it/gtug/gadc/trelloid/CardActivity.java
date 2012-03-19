
package it.gtug.gadc.trelloid;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class CardActivity extends FragmentActivity {

    private String cardId;

    private ProgressDialog queryDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stringExtra = getIntent().getStringExtra("cardId");
        cardId = stringExtra != null ? stringExtra : "4f3fa1c0b23069041241fbfc";
        setContentView(R.layout.card);

        showDialog(1);

        Bundle bundle = new Bundle();
        bundle.putString("cardId", cardId);

        getSupportLoaderManager().initLoader(0, bundle, new LoaderCallbacks<Card>() {

            public Loader<Card> onCreateLoader(int id, Bundle args) {
                return new CardLoader(CardActivity.this, args.getString("cardId"));
            }

            public void onLoadFinished(Loader<Card> arg0, Card card) {
                showCardInLayout(card);
                queryDialog.dismiss();
            }

            public void onLoaderReset(Loader<Card> arg0) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showCardInLayout((Card)data.getSerializableExtra("card"));
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
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.support.v4.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.commenta:
                createComment();
                return true;
            case R.id.edit:
                editCard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editCard() {
        startActivityForResult(new Intent(this, EditCardActivity.class), 1);
        // startActionMode(new ActionMode.Callback() {
        //
        // public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // return false;
        // }
        //
        // public void onDestroyActionMode(ActionMode mode) {
        //
        // }
        //
        // public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // menu.add("Done");
        // mode.setTitle("");
        // return true;
        // }
        //
        // public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // mode.finish();
        // return true;
        // }
        // });
    }

    private void createComment() {
        String token = getToken();
        AQuery aq = new AQuery(this);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("text", "Prova");
        params.put("key", TrelloidApplication.CONSUMER_KEY);
        params.put("token", token);

        aq.ajax("https://api.trello.com/1/cards/" + cardId + "/actions/comments", params,
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        System.out.println(123);
                    }
                });
    }

    private String getToken() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
                TrelloidApplication.TRELLOID_TOKEN, null);
    }

    private void showCardInLayout(Card card) {
        AQuery aq = new AQuery(CardActivity.this);

        aq.id(R.id.title).text(card.getName());
        aq.id(R.id.description).text(card.getDesc());

        ArrayAdapter<Comment> arrayAdapter = new CommentAdapter(CardActivity.this,
                R.layout.comment, R.id.text, card.getComments());
        ((ListView)findViewById(android.R.id.list)).setAdapter(arrayAdapter);
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

            // AQuery aq = new AQuery(view);
            //
            // Member member = ((TrelloidApplication)
            // getContext().getApplicationContext()).getMember(idMemberCreator);
            // if (member.getAvatarHash() != null) {
            // aq.id(R.id.avatar).visible().image("https://trello-avatars.s3.amazonaws.com/"
            // + member.getAvatarHash() + "/30.png");
            // aq.id(R.id.initial).invisible();
            // } else {
            // aq.id(R.id.initial).visible().text(member.getInitials());
            // aq.id(R.id.avatar).invisible();
            // }

            return view;
        }
    }

}
