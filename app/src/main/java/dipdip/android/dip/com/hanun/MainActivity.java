package dipdip.android.dip.com.hanun;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView tv,tv2,tv3,tv4;
    MediaPlayer notif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView2);
        tv2 = (TextView)findViewById(R.id.textView4);
        tv3 = (TextView)findViewById(R.id.textView6);
        tv4 = (TextView)findViewById(R.id.textView2a);
        notif = MediaPlayer.create(this, R.raw.notifikasi);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    double score = ds.child("suhu").getValue(Double.class);
                    double score2 = ds.child("putaran").getValue(Double.class);
                    double score3 = ds.child("rpm").getValue(Double.class);
                    double score4 = ds.child("hearth_rate").getValue(Double.class);
                    tv.setText(String.valueOf(score));
                    tv2.setText(String.valueOf(score2));
                    tv3.setText(String.valueOf(score3));
                    tv4.setText(String.valueOf(score4));
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(MainActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}
