package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class NotifikasiActivity extends Activity {
    TextView tv1, tv2, tv3, tv4;
    String data,suhu,detak_jantung,diagnosis;
    private DatabaseReference mDatabase;
    private String auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        data = getIntent().getStringExtra("from_user_id");
        suhu = getIntent().getStringExtra("suhu");
        detak_jantung = getIntent().getStringExtra("detak_jantung");
        diagnosis = getIntent().getStringExtra("diagnosis");

        auth = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("user_list");
        tv1 = (TextView)findViewById(R.id.textView26);
        tv2 = (TextView)findViewById(R.id.textView10);
        tv3 = (TextView)findViewById(R.id.textView12);
        tv4 = (TextView)findViewById(R.id.textView18);


        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI
                String nama = ds.child("nama").getValue(String.class);
                tv1.setText("User " +nama+ "Mengirim Notifikasi");
                tv2.setText(suhu);
                tv3.setText(detak_jantung);
                tv4.setText(diagnosis);
                // ...
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(NotifikasiActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };

        mDatabase.child(data).addListenerForSingleValueEvent(postListener2);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
}
