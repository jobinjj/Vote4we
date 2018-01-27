package sample.com.jobin.msi.vote4we;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VoteActivity extends RegisterActivity {
    ImageView img_pushbutton;
    TextView txt_votes;
    String title,img_url;
    RoundedCornerNetworkImageView img_celeb;
    private DatabaseReference mDatabase;
    int rating;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    GoogleSignInAccount account;
    String name;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        account = GoogleSignIn.getLastSignedInAccount(VoteActivity.this);


        initViews();


        Bundle extras = getIntent().getExtras();
        img_pushbutton = findViewById(R.id.img_pushbutton);
        if (extras != null) {
            title = extras.getString("title");
            if (isAlpha(title)){

            }
            img_url = extras.getString("img_url");

            img_celeb.setImageUrl(img_url,imageLoader);
        }

        if (account != null){


            mDatabase.child(pref.getString("type","actor")).child(title).child("users").child(account.getDisplayName()).child("rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String value = dataSnapshot.getValue(String.class);
                    if (value != null){
                        if (value.equals("rated")){
                            Toast.makeText(VoteActivity.this, "already rated", Toast.LENGTH_SHORT).show();
                            AnimatedVectorDrawableCompat anim2 = AnimatedVectorDrawableCompat.create(VoteActivity.this,R.drawable.pushbutton_anim);
                            if (anim2 != null){
                                anim2.start();
                                img_pushbutton.setImageDrawable(anim2);
                                img_pushbutton.setEnabled(false);
                            }
                        }
                        else {
                            Toast.makeText(VoteActivity.this, "eligible", Toast.LENGTH_SHORT).show();
                            img_pushbutton.setEnabled(true);
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("tag", "Failed to read value.", error.toException());
                }
            });
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CeraPRO-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );


        mDatabase.child(pref.getString("type","actor")).child(title).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                if (value != null){
                    rating = Integer.parseInt(value);
                    txt_votes.setText(String.valueOf(rating));
                }


             }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
        img_pushbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnimatedVectorDrawableCompat anim = AnimatedVectorDrawableCompat.create(VoteActivity.this,R.drawable.pushbutton_anim);
                img_pushbutton.setImageDrawable(anim);
                if (anim != null){
                    anim.start();
                    img_pushbutton.setEnabled(false);
                    mDatabase.child(pref.getString("type","actor")).child(title).child("rating").setValue(String.valueOf(rating + 1) );
                    if (account != null){
                        mDatabase.child(pref.getString("type","actor")).child(title).child("users").child(account.getDisplayName()).child("rating").setValue("rated");
                    }

                    txt_votes.setText(String.valueOf(rating + 1));
                }

            }
        });
    }

    private void initViews() {
        img_celeb = findViewById(R.id.img_celeb);
        txt_votes = findViewById(R.id.txt_votes);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onBackPressed() {


        VoteActivity.super.onBackPressed();
    }
    @Override
    protected void onStart() {
        super.onStart();
        checklogin();

            }
    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
