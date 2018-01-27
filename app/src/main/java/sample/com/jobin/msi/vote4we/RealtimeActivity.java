package sample.com.jobin.msi.vote4we;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeActivity extends AppCompatActivity {
    String updatestr,updatestr2;
    EditText editText3,editText4;
    private DatabaseReference mDatabase;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);


        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        text = (TextView) findViewById(R.id.text);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("nickname");

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatestr = editText3.getText().toString();
                updatestr2 = editText4.getText().toString();

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("actors").child(updatestr).child("nickname").setValue(updatestr2);
                mDatabase.child("actors").child(updatestr).child("rating").setValue(updatestr2);

                editText3.setText("");
                editText4.setText("");



                mDatabase.child("actors").child(updatestr).child("nickname").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String value = dataSnapshot.getValue(String.class);
                        Toast.makeText(RealtimeActivity.this, value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });

            }
        });


    }
}
