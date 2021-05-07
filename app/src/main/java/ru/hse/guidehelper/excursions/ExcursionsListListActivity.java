package ru.hse.guidehelper.excursions;

import android.os.AsyncTask;
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
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import ru.hse.guidehelper.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

        queueRequest.start();

        setupRecyclerView((RecyclerView) recyclerView);

//        Bundle bundle = getIntent().getExtras();
//        if(bundle != null && bundle.getString("bot") != null) {
//            Toast.makeText(getApplicationContext(), bundle.getString("bot"), Toast.LENGTH_SHORT).show();
//        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, queueRequest));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        // private String  url = "http://192.168.3.225:8080";
        private String  url = "http://192.168.0.137:8080";
        private RequestQueue queueRequest;
        private final ExcursionsListListActivity mParentActivity;
        private final List<DummyItem> mValues;
        private int cntOfTours = 1;
        private final String suffTours = "/tours";

        // private JSONArray arrOfTours = null; 1

//        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
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
                                      RequestQueue queueRequest) {
            mValues = new ArrayList<>();
            for(int i = 1; i <= 100; i++) {
                mValues.add(new DummyItem());
            }
            mParentActivity = parent;
            this.queueRequest = queueRequest;

            new AsyncRequestGetCntOfTours().execute("");
//            while(arrOfTours == null) { 1
//                System.out.println(0);
//            }
//
//            for(int i = 0; i < 3; i++) {
//                try {
//                    System.out.println("1 " + arrOfTours.getJSONObject(i).getString("title"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.excursionslist_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + suffTours, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        cntOfTours = response.length();
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

//            try {
//                holder.mContentView.setText(arrOfTours.getJSONObject(position).getString("title"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            holder.mIdView.setText(String.valueOf(position + 1));
            // holder.itemView.setTag(mValues.get(position)); // tag ???


            // holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return cntOfTours;
        }

        private class AsyncRequestGetCntOfTours extends AsyncTask<String, Integer, Integer> {

            @Override
            protected Integer doInBackground(String... arg) {
                try {
                    cntOfTours = readJsonFromUrl().length();
                    return readJsonFromUrl().length();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return 10;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
            }
        }

        private JSONArray readJsonFromUrl() throws IOException, JSONException {
            InputStream is = new URL(url + suffTours).openStream();
            JSONArray json;
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                json = new JSONArray(readAll(rd));
            } finally {
                is.close();
            }
            // arrOfTours = json; 1
            return json;
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
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

        public class DummyItem {
            public String id;
            public String content;
            public String details;

            @Override
            public String toString() {
                return content;
            }
        }

    }
}