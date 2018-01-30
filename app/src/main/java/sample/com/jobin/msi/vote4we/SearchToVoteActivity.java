package sample.com.jobin.msi.vote4we;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchToVoteActivity extends AppCompatActivity {
    private ListView listView;
    EditText ed1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectivityManager conMgr;
    private static final String url = "http://techpayyans.000webhostapp.com/Vote4We/getdata.php?";
    RelativeLayout rl_main;
    public List<MovieSearch> movieList = new ArrayList<MovieSearch>();
    public CustomListAdaptersearch adapter;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_to_vote);

        initViews();
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ed1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //  Toast.makeText(getApplicationContext(),"afterTextChanged",Toast.LENGTH_SHORT).show();

                if (netInfo == null) {
                    Toast.makeText(SearchToVoteActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    getData();

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Toast.makeText(getApplicationContext(),"beforeTextChanged",Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();


            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                //Toast.makeText(getApplicationContext(),"onTextChanged",Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();
            }
        });
        adapter = new CustomListAdaptersearch(SearchToVoteActivity.this, movieList,
                SearchToVoteActivity.this);

    }
    private void initViews() {
        conMgr = (ConnectivityManager) SearchToVoteActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        rl_main = findViewById(R.id.rl_main);

        ed1 = findViewById(R.id.ed1);
      
        listView = (ListView) findViewById(R.id.listview);
       
    }
    private void getData() {


        listView.setAdapter(adapter);
        //
        JsonArrayRequest movieReq = new JsonArrayRequest(url + "keyword=" + ed1.getText().toString().replaceAll(" ", "%20")   + "&type=" + pref.getString("type","actor"),
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        movieList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response
                                        .getJSONObject(i);
                                MovieSearch moviesearch = new MovieSearch();

                                moviesearch.setTitle(obj
                                        .getString("name"));
                                moviesearch.setThumbnailUrl(obj.getString("img_url"));



                                movieList.add(moviesearch);

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public class CustomListAdaptersearch extends BaseAdapter {
        private Activity activity;
        String value;
        private LayoutInflater inflater;
        private List<MovieSearch> movieItems;
        MyViewHolder holder;
        Context context;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public CustomListAdaptersearch(Activity activity,
                                       List<MovieSearch> movieItems, Context context) {
            this.activity = activity;
            this.movieItems = movieItems;
            this.context = context;
        }

        @Override
        public int getCount() {
            return movieItems.size();
        }

        @Override
        public Object getItem(int location) {
            return movieItems.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(LAYOUT_INFLATER_SERVICE);

            holder = new MyViewHolder();
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_row_search, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);
            // thumbNail.setDefaultImageResId(R.drawable.busdefault);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.rl_card = (RelativeLayout) convertView.findViewById(R.id.rl_card);

            // getting movie data for the row
            final MovieSearch m = movieItems.get(position);

            // thumbnail image
            // imageLoader.DisplayImage(m.getThumbnailUrl(), thumbNail);
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
            // title
            holder.title.setText(m.getTitle());
            holder.rl_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),VoteActivity.class);
                    intent.putExtra("title",m.getTitle());
                    intent.putExtra("img_url",m.getThumbnailUrl());
                    startActivity(intent);
                    finish();
                }
            });




            return convertView;
        }

        private class MyViewHolder {
            public TextView title;
            public NetworkImageView thumbNail;
            public RelativeLayout rl_card;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
