package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dipdip.android.dip.com.hanun.SessionManager.Preferences;

public class AkunActivity extends Activity {
    public static String nama = "";
    public static String usia = "";
    public static String token = "";
    public static String jenisKelamin = "";
    EditText etNama, etUsia, etJenis;
    Button logout;
    private String auth;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        etNama = (EditText)findViewById(R.id.editText7);
        etUsia = (EditText)findViewById(R.id.editText8);
        etJenis = (EditText)findViewById(R.id.editText9);
        logout = (Button)findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();
        auth = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("user_list");
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI
                String nama = ds.child("nama").getValue(String.class);
                 usia = ds.child("usia").getValue(String.class);
                etNama.setText(nama);
                etUsia.setText(usia);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabase.child(auth).addListenerForSingleValueEvent(postListener2);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Preferences.clearLoggedInUser(getBaseContext());
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void onHome(View v)
    {
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
    public void onRiwayat(View v)
    {
        Intent i = new Intent(getBaseContext(), RiwayatActivity.class);
        startActivity(i);
        finish();
    }
    public void onAkun(View v)
    {
        Intent i = new Intent(getBaseContext(), AkunActivity.class);
        startActivity(i);
        finish();
    }
}
