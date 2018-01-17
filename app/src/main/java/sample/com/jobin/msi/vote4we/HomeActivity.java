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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {
    TextView textView2,textView;
    RoundedCornerNetworkImageView img_adduser1,img_adduser2;
    String selected,title,image;
    private ProgressDialog pDialog;
    NetworkInfo netInfo;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        img_adduser1 = findViewById(R.id.img_adduser1);
        img_adduser2 = findViewById(R.id.img_adduser2);
        textView2 = findViewById(R.id.textView2);
        textView = findViewById(R.id.textView);
        pDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.AppTheme));
        pDialog.setMessage("Please wait");
        pDialog.show();
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
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



        img_adduser1.setImageUrl(pref.getString("first","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
        img_adduser2.setImageUrl(pref.getString("second","https://techpayyans.000webhostapp.com/Vote4We/images/user.png"),imageLoader);
        textView.setText(pref.getString("firstname",""));
        textView2.setText(pref.getString("secondname",""));
        textView2.setAllCaps(true);
        textView.setAllCaps(true);
        Bundle extras = getIntent().getExtras();
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






        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/CeraPRO-Regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build() );


//        img_adduser1.setImageUrl("https://techpayyans.000webhostapp.com/Vote4We/images/add-user.png",imageLoader);
//        img_adduser2.setImageUrl("https://techpayyans.000webhostapp.com/Vote4We/images/add-user.png",imageLoader);

        img_adduser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                intent.putExtra("which", "first");
                startActivity(intent);



            }
        });
        img_adduser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                intent.putExtra("which", "second");
                startActivity(intent);

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {


            HomeActivity.super.onBackPressed();
            editor.clear().apply();


    }
}
