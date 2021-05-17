package ru.hse.guidehelper.excursions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcursionsListActivity extends AppCompatActivity {

    RequestQueue queueRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursionslist_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        View recyclerView = findViewById(R.id.excursionslist_list);
        assert recyclerView != null;


        queueRequest = Volley.newRequestQueue(this);

        queueRequest.start();

        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, queueRequest));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final String  url = "http://192.168.0.62:8080";
        private final RequestQueue queueRequest;
        private final ExcursionsListActivity mParentActivity;
        private final List<DummyItem> mValues;
        private final String suffTours = "/tours";

        private JSONArray arrOfTours = null;
        public static Map<String, DummyItem> itemMap = new HashMap<>();

        private final View.OnClickListener mOnClickListener = view -> {
            DummyItem item = (DummyItem) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, ExcursionsListDetailActivity.class);
            intent.putExtra(ExcursionsListDetailFragment.ARG_ITEM_ID, item.id);

            context.startActivity(intent);
        };

        SimpleItemRecyclerViewAdapter(ExcursionsListActivity parent,
                                      RequestQueue queueRequest) {
            mValues = new ArrayList<>();
            mParentActivity = parent;
            this.queueRequest = queueRequest;


            new AsyncRequestGetCntOfTours().execute("");

            synchronized (JSONArray.class) {
                while(arrOfTours == null) {
                    try {
                        JSONArray.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for(int i = 1; i <= arrOfTours.length(); i++) {
                    DummyItem item = new DummyItem(String.valueOf(i));
                    mValues.add(item);
                    itemMap.put(item.id, item);
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.excursionslist_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                holder.mContentView.setText(arrOfTours.getJSONObject(position).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.mIdView.setText(String.valueOf(position + 1));

            try {
                mValues.get(position).details = arrOfTours.getJSONObject(position).getString("description");
                mValues.get(position).content = arrOfTours.getJSONObject(position).getString("title") + " - " +
                        arrOfTours.getJSONObject(position).getString("city");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return arrOfTours.length();
        }

        private class AsyncRequestGetCntOfTours extends AsyncTask<String, Integer, Integer> {

            @Override
            protected Integer doInBackground(String... arg) {
                try {
                    return readJsonFromUrl().length();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return 0;
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
            arrOfTours = json;
            synchronized (JSONArray.class) {
                JSONArray.class.notifyAll();
            }
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
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }

        public class DummyItem {
            public String id;
            public String content;
            public String details;

            public DummyItem(String id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return content;
            }
        }

    }
}