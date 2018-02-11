package sample.com.jobin.msi.vote4we;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class FinalActivity extends CompareActivity {
    ImageView img_adduser1;
    private DatabaseReference mDatabase,mDatabase2;
    String winnerimg;


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            winnerimg = extras.getString("winnerimg");
        }
        Toast.makeText(this, winnerimg, Toast.LENGTH_SHORT).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_final);
        String url = "https://techpayyans.000webhostapp.com/Vote4We/winner.php?";
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();



        initViews();
        initsharedPreference();
        initFirebase();
        progressDialog();
        internetConnection();
        customFont();
        extras();
        onClick();
        main();




        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);


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




    }






    private void onClick() {
    }

    private void extras() {
    }

    private void main() {
    }

    private void customFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );
    }

    private void internetConnection() {
    }

    private void progressDialog() {
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
    }

    private void initsharedPreference() {
    }

    private void initViews() {img_adduser1= findViewById(R.id.img_adduser1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FinalActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        editor.remove("firstrating").apply();
//        editor2.clear().apply();
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

