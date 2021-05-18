package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsListDetailActivity;
import ru.hse.guidehelper.excursions.ExcursionsListDetailFragment;

public class ExcursionFragment extends Fragment {

    RequestQueue queueRequest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //View root = inflater.inflate(R.layout.fragment_excursion, container, false);
        View root = inflater.inflate(R.layout.activity_excursionslist, container, false);

//        Intent in = new Intent(getActivity(), ExcursionsListDetailActivity.class);
//        startActivity(in);

        if(this.getActivity() == null) {
            System.out.println(1);
        }
        System.out.println("!!!!!!");

        // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // setSupportActionBar(toolbar); ??



        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        View recyclerView = root.findViewById(R.id.excursionslist_list);
        assert recyclerView != null;

        queueRequest = Volley.newRequestQueue(root.getContext());

        queueRequest.start();

        setupRecyclerView((RecyclerView) recyclerView);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, queueRequest));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final String  url = "http://192.168.0.62:8080";
        private final RequestQueue queueRequest;
        private final ExcursionFragment mParentActivity;
        private final List<SimpleItemRecyclerViewAdapter.DummyItem> mValues;
        private final String suffTours = "/tours";

        private JSONArray arrOfTours = null;
        public static Map<String, SimpleItemRecyclerViewAdapter.DummyItem> itemMap = new HashMap<>();

        private final View.OnClickListener mOnClickListener = view -> {
            SimpleItemRecyclerViewAdapter.DummyItem item = (SimpleItemRecyclerViewAdapter.DummyItem) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, ExcursionsListDetailActivity.class);
            intent.putExtra(ExcursionsListDetailFragment.ARG_ITEM_ID, item.id);

            context.startActivity(intent);
        };

        SimpleItemRecyclerViewAdapter(ExcursionFragment parent,
                                      RequestQueue queueRequest) {
            mValues = new ArrayList<>();
            mParentActivity = parent;
            this.queueRequest = queueRequest;

            new SimpleItemRecyclerViewAdapter.AsyncRequestGetTours().execute("");

            synchronized (JSONArray.class) {
                while(arrOfTours == null) {
                    try {
                        JSONArray.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for(int i = 1; i <= arrOfTours.length(); i++) {
                    SimpleItemRecyclerViewAdapter.DummyItem item = new SimpleItemRecyclerViewAdapter.DummyItem(String.valueOf(i));
                    mValues.add(item);
                    itemMap.put(item.id, item);
                }
            }
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.excursionslist_list_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
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

        private class AsyncRequestGetTours extends AsyncTask<String, Integer, Integer> {

            private OkHttpClient client = new OkHttpClient();

            @Override
            protected Integer doInBackground(String... arg) {
                try {
                    getTours(client);
                    return 1;
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

        private void getTours(OkHttpClient client) throws IOException, JSONException {
            Request request = new Request.Builder()
                    .url(url + suffTours)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String res = response.body().string();
                System.out.println(res);

                arrOfTours = new JSONArray(res);
                synchronized (JSONArray.class) {
                    JSONArray.class.notifyAll();
                }
            }
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