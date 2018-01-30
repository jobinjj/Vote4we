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

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CompareActivity extends AppCompatActivity  {
    TextView textView2,textView;
    RoundedCornerNetworkImageView img_adduser1,img_adduser2;
    String selected,title,image,str_continue;
    private ProgressDialog pDialog;
    String winnername;
    private DatabaseReference mDatabase;
    ImageView testimg;
    String img_url;
    int firstrating;
            int secondrating;
    NetworkInfo netInfo;
    SharedPreferences pref;
    SharedPreferences.Editor
            editor;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_compare);

        initViews();
        handler();
        initsharedPreference();
        initFirebase();
        progressDialog();
        internetConnection();
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
                    editor.apply();
                    img_adduser1.setImageUrl(pref.getString("first","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
                    textView.setText(pref.getString("firstname","First Actor"));

                }else if (selected.equals("second")){
                    editor.putString("second",image);
                    editor.putString("secondname",title);
                    editor.apply();
                    img_adduser2.setImageUrl(pref.getString("second","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
                    textView2.setText(pref.getString("secondname","Second Actor"));
                }
            }
        }

        if (extras2 != null){
            str_continue = extras2.getString("continue");

            if(str_continue != null){
                mDatabase.child(pref.getString("type","actor")).child(pref.getString("firstname","First Actor")).child("rating").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        if (value != null){
                            firstrating = Integer.parseInt(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });

                mDatabase.child(pref.getString("type","actor")).child(pref.getString("secondname","Second Actor")).child("rating").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        if (value != null){
                            secondrating = Integer.parseInt(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });

                if (firstrating > secondrating){
                    img_url = pref.getString("first","https://techpayyans.000webhostapp.com/Vote4We/images/user.png");
                    winnername = pref.getString("firstname","First Actor");
                }else if (secondrating > firstrating){
                    img_url = pref.getString("second","https://techpayyans.000webhostapp.com/Vote4We/images/user.png");
                    winnername = pref.getString("secondname","Second Actor");
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CompareActivity.this,FinalActivity.class);
                        intent.putExtra("img_url",img_url);
                        intent.putExtra("winner",winnername);
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
        editor = pref.edit();
        editor.apply();
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
