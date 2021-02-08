package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notifikasi2Activity extends Activity {
    TextView tv1, tv2, tv3;
    private DatabaseReference mDatabase;
    private String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi2);
        auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tv1 = (TextView)findViewById(R.id.textView26);
        tv2 = (TextView)findViewById(R.id.textView10);
        tv3 = (TextView)findViewById(R.id.textView12);

        mDatabase = FirebaseDatabase.getInstance().getReference("user_list").child(auth);
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI
                String nama = ds.child("PERINGATAN !!!").getValue(String.class);
                String suhu = ds.child("notif_suhu").getValue(String.class);
                String detak = ds.child("notif_hearth_rate").getValue(String.class);
                tv1.setText("STOP! DATA ANDA MELEBIHI BATAS NORMAL");
                tv2.setText(suhu);
                tv3.setText(detak);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(Notifikasi2Activity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
}
