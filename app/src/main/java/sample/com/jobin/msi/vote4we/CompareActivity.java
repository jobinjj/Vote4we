package sample.com.jobin.msi.vote4we;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CompareActivity extends AppCompatActivity  {
    TextView textView2,textView;
    RoundedCornerNetworkImageView img_adduser1,img_adduser2;
    String selected,title,image,str_continue;
    private ProgressDialog pDialog;
    String winnername;
    String url = "https://techpayyans.000webhostapp.com/Vote4We/winner.php?";
    private DatabaseReference mDatabase;
    String firstname,secondname,firstrating,secondrating;
    ImageView testimg;
    String img_url;
    String imageurl;
    NetworkInfo netInfo;
    SharedPreferences pref,pref2;
    SharedPreferences.Editor editor,editor2;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_compare);
        initsharedPreference();
        initViews();
        internetConnection();
        progressDialog();
        handler();

        initFirebase();
        customFont();
        main();
        extras();
        onClick();

    }

    private void onClick() {
        img_adduser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareActivity.this,SearchActivity.class);
                intent.putExtra("which", "first");
                startActivity(intent);
            }
        });
        img_adduser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareActivity.this,SearchActivity.class);
                intent.putExtra("which", "second");
                startActivity(intent);
            }
        });
    }

    private void extras() {
            Bundle extras = getIntent().getExtras();
        Bundle extras2 = getIntent().getExtras();
        if (extras != null) {
            selected = extras.getString("which");
            title = extras.getString("title");
            image = extras.getString("img_url");
            if (title != null && image != null){
                if (selected.equals("first")){
                    editor.putString("first",image);
                    editor.putString("firstname",title);
                    firstname = title;
                    editor.apply();
                    img_adduser1.setImageUrl(pref.getString("first","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
                    textView.setText(pref.getString("firstname","First Actor"));

                    mDatabase.child(pref.getString("type","actor")).child(title).child("rating").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            if (value != null){
                            firstrating  = value;
                            editor.putString("firstrating",value).apply();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("tag", "Failed to read value.", error.toException());
                        }
                    });

                }else if (selected.equals("second")){
                    editor.putString("second",image);
                    editor.putString("secondname",title);
                    secondname = title;
                    editor.apply();
                    img_adduser2.setImageUrl(pref.getString("second","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
                    textView2.setText(pref.getString("secondname","Second Actor"));
                    mDatabase.child(pref.getString("type","actor")).child(title).child("rating").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            if (value != null){
                                secondrating = value;
                                editor.putString("secondrating",value).apply();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("tag", "Failed to read value.", error.toException());
                        }
                    });
                }
            }
        }

        if (extras2 != null){
            str_continue = extras2.getString("continue");

            if(str_continue != null){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        JsonArrayRequest arrayRequest = new JsonArrayRequest(url + "rating1=" + pref.getString("firstrating","") + "&rating2=" + pref.getString("secondrating","") + "&firstname=" + pref.getString("secondname","Second Actor") + "&secondname=" + pref.getString("firstname","Second Actor") + "&imgurl1=" + pref.getString("first","") + "&imgurl2=" + pref.getString("second",""), new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        Toast.makeText(CompareActivity.this, String.valueOf(response.length()), Toast.LENGTH_SHORT).show();
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        Toast.makeText(CompareActivity.this, jsonObject.getString("winner"), Toast.LENGTH_SHORT).show();
                                        imageurl = jsonObject.getString("imgurl");
//                        Toast.makeText(FinalActivity.this, jsonObject.getString("rating1"), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(FinalActivity.this, jsonObject.getString("rating2"), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(FinalActivity.this, jsonObject.getString("firstname"), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(FinalActivity.this, jsonObject.getString("secondname"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        AppController.getInstance().addToRequestQueue(arrayRequest);

                        Intent intent = new Intent(CompareActivity.this,FinalActivity.class);
                        intent.putExtra("img_url",img_url);
                        intent.putExtra("winner",winnername);
                        intent.putExtra("winnerimg",imageurl);
                        startActivity(intent);
                    }
                }, 2000);
            }
        }
    }

    private void main() {
        img_adduser1.setImageUrl(pref.getString("first","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
        img_adduser2.setImageUrl(pref.getString("second","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
        textView.setText(pref.getString("firstname",""));
        textView2.setText(pref.getString("secondname",""));
        textView2.setAllCaps(true);
        textView.setAllCaps(true);
    }

    private void customFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );
    }

    private void internetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
    }

    private void progressDialog() {
        pDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.AppTheme));
        pDialog.setMessage("Please wait");
        pDialog.show();
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void initsharedPreference() {
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        pref2 = getApplicationContext().getSharedPreferences("Mypref2",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        editor2 = pref2.edit();
//        editor2.putInt("secondrating",100);
//        editor2.putInt("firstrating",50);
        editor2.apply();
    }

    private void handler() {
        if (netInfo ==null){
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                }
            }, 2000);
        }
    }

    private void initViews() {
        img_adduser1 = findViewById(R.id.img_adduser1);
        img_adduser2 = findViewById(R.id.img_adduser2);
        textView2 = findViewById(R.id.textView2);
        textView = findViewById(R.id.textView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        CompareActivity.super.onBackPressed();
        editor.clear().apply();
        finish();
    }
}
