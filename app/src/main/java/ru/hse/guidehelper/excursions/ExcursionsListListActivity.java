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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        System.out.println("00000000");

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

        private String  url = "http://192.168.3.225:8080";
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
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            holder.mContentView.setText(response);
                            // textView.setText("Response is: "+ response.substring(0,12));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.mContentView.setText("22222222" + error.getMessage() + "\nLocal:\n" + error.getLocalizedMessage()
                                    + "\nCause:\n" + error.getCause() + "\ntoString:\n" + error.toString());

                    // textView.setText("That didn't work!\n" + error.getMessage() + "\nLocal:\n" + error.getLocalizedMessage()
                    //        + "\nCause:\n" + error.getCause() + "\ntoString:\n" + error.toString());
                }
            });

            queueRequest.add(stringRequest);

            holder.mIdView.setText(mValues.get(position).id);
            // holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));


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