package sample.com.jobin.msi.vote4we;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
    }

    public void compare(View view) {
        Intent intent = new Intent(HomeActivity.this,FirstActivity.class);
        startActivity(intent);

    }

    public void vote(View view) {
        Intent intent = new Intent(HomeActivity.this,SelectActivity.class);
        startActivity(intent);
    }


}
