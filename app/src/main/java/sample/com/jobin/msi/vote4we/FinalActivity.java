package sample.com.jobin.msi.vote4we;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinalActivity extends CompareActivity {
    ImageView img_adduser1;
    private DatabaseReference mDatabase;
    String winner;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_final);

        initViews();
        initsharedPreference();
        initFirebase();
        progressDialog();
        internetConnection();
        customFont();
        extras();
        onClick();
        main();
    }

    private void onClick() {
    }

    private void extras() {
    }

    private void main() {
        Toast.makeText(this, pref.getString("firstname","jobin"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, pref.getString("secondname","jithin"), Toast.LENGTH_SHORT).show();
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
    }

    private void initsharedPreference() {pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }

    private void initViews() {img_adduser1= findViewById(R.id.img_adduser1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.clear().apply();
        Intent intent = new Intent(FinalActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

