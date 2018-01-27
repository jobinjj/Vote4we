package sample.com.jobin.msi.vote4we;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void compare(View view) {
        Intent intent = new Intent(HomeActivity.this,FirstActivity.class);
        startActivity(intent);
        finish();
    }

    public void vote(View view) {
        Intent intent = new Intent(HomeActivity.this,SelectActivity.class);
        startActivity(intent);
        finish();
    }
}
