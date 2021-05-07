package ru.hse.guidehelper.excursions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;
import ru.hse.guidehelper.R;

import ru.hse.guidehelper.excursions.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of ExcursionsList. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ExcursionsListDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class ExcursionsListListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    RequestQueue queueRequest;
    DummyContent dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursionslist_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.excursionslist_list);
        assert recyclerView != null;


        queueRequest = Volley.newRequestQueue(this);

        dummy = new DummyContent(queueRequest);

        queueRequest.start();

        setupRecyclerView((RecyclerView) recyclerView);

//        Bundle bundle = getIntent().getExtras();
//        if(bundle != null && bundle.getString("bot") != null) {
//            Toast.makeText(getApplicationContext(), bundle.getString("bot"), Toast.LENGTH_SHORT).show();
//        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, dummy.ITEMS, queueRequest));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        // private String  url = "http://192.168.3.225:8080";
        private String  url = "http://192.168.0.137:8080";
        private RequestQueue queueRequest;
        private final ExcursionsListListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
//        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
//
//                Context context = view.getContext();
//                Intent intent = new Intent(context, ExcursionsListDetailActivity.class);
//                intent.putExtra(ExcursionsListDetailFragment.ARG_ITEM_ID, item.id);
//
//                context.startActivity(intent);
//
//            }
//        };

        SimpleItemRecyclerViewAdapter(ExcursionsListListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      RequestQueue queueRequest) {
            mValues = items;
            mParentActivity = parent;
            this.queueRequest = queueRequest;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.excursionslist_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + "/tours", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {

                        holder.mContentView.setText(response.getJSONObject(position).getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.mContentView.setText("Error Cause:\n" + error.getCause() + "\n");
                }
            });

            queueRequest.add(request);

            holder.mIdView.setText(String.valueOf(position + 1));
            holder.itemView.setTag(mValues.get(position)); // tag ???


            // holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}