package sample.com.jobin.msi.vote4we;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FirstActivity extends AppCompatActivity {
    SharedPreferences pref,pref2;
    SharedPreferences.Editor editor,editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        pref2 = getApplicationContext().getSharedPreferences("Mypref2",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        editor2 = pref2.edit();
        editor2.apply();



        findViewById(R.id.img_actor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,CompareActivity.class);
                editor.putString("type","actors");
                editor.remove("secondname");
                editor.remove("firstname");
                editor.remove("firstrating");
                editor.remove("secondrating");

                editor.remove("first");
                editor.remove("second");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_singer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,CompareActivity.class);
                editor.putString("type","singers");
                editor.remove("secondname");
                editor.remove("firstname");
                editor.remove("first");
                editor.remove("second");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_politician).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,CompareActivity.class);
                editor.putString("type","politicians");
                editor.remove("secondname");
                editor.remove("second");
                editor.remove("first");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,CompareActivity.class);
                editor.putString("type","players");
                editor.remove("secondname");
                editor.remove("firstname");
                editor.remove("second");
                editor.remove("first");
                editor.apply();
                startActivity(intent);
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void close(View view) {

        finish();
    }


}
