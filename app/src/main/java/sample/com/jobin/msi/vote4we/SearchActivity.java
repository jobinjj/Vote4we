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

public class SearchActivity extends AppCompatActivity {
    private static final String url = "http://techpayyans.000webhostapp.com/Vote4We/getdata.php?";
    private ListView listView,listView2;
    String selected,type;
    EditText ed1,ed2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectivityManager conMgr;
    RelativeLayout rl_main,rl_main2;
    NetworkInfo netInfo;
    public List<MovieSearch> movieList = new ArrayList<MovieSearch>();
    public List<MovieSearch> movieList2 = new ArrayList<MovieSearch>();
    public CustomListAdaptersearch  adapter;
    public CustomListAdaptersearch adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        initViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selected = extras.getString("which");
        }
        if (selected.equals("first")){
            rl_main.setVisibility(View.VISIBLE);
            rl_main2.setVisibility(View.GONE);
        }else if (selected.equals("second")){
            rl_main.setVisibility(View.GONE);
            rl_main2.setVisibility(View.VISIBLE);
        }
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
                    Toast.makeText(SearchActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
        ed2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //  Toast.makeText(getApplicationContext(),"afterTextChanged",Toast.LENGTH_SHORT).show();

                if (netInfo == null) {
                    Toast.makeText(SearchActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    getData2();

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
        adapter = new CustomListAdaptersearch(SearchActivity.this, movieList,
                SearchActivity.this);
        adapter2 = new CustomListAdaptersearch(SearchActivity.this, movieList2,
                SearchActivity.this);
//        listView2.setAdapter(adapter2);

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

    private void getData2() {


        listView2.setAdapter(adapter2);
        //
        JsonArrayRequest movieReq = new JsonArrayRequest(url + "keyword=" + ed2.getText().toString().replaceAll(" ", "%20") + "&type=" +  pref.getString("type","actor") ,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        movieList2.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response
                                        .getJSONObject(i);
                                MovieSearch moviesearch = new MovieSearch();

                                moviesearch.setTitle(obj
                                        .getString("name"));
                                moviesearch.setThumbnailUrl(obj.getString("img_url"));



                                movieList2.add(moviesearch);

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }
                        adapter2.notifyDataSetChanged();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void initViews() {
        conMgr = (ConnectivityManager) SearchActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        rl_main = findViewById(R.id.rl_main);
        rl_main2 = findViewById(R.id.rl_main2);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        listView = (ListView) findViewById(R.id.listview);
        listView2 = (ListView) findViewById(R.id.listview2);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                        Intent intent = new Intent(getApplicationContext(),CompareActivity.class);
                        intent.putExtra("title",m.getTitle());
                        intent.putExtra("img_url",m.getThumbnailUrl());
                        intent.putExtra("which",selected);
                        if (selected.equals("second")){
                            intent.putExtra("continue","continue");
                        }


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
}
