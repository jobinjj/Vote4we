package sample.com.jobin.msi.vote4we;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectActivity extends RegisterActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );

        findViewById(R.id.img_actor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,SearchToVoteActivity.class);
                editor.putString("type","actors");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_singer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,SearchToVoteActivity.class);
                editor.putString("type","singers");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_politician).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,SearchToVoteActivity.class);
                editor.putString("type","politicians");
                editor.apply();
                startActivity(intent);
            }
        });
        findViewById(R.id.img_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,SearchToVoteActivity.class);
                editor.putString("type","players");
                editor.apply();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void onBackPressed() {


        SelectActivity.super.onBackPressed();
        editor.clear().apply();
        Intent intent = new Intent(SelectActivity.this,HomeActivity.class);
        startActivity(intent);


    }

}
