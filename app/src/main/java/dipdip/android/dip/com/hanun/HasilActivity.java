package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HasilActivity extends Activity {
    private DatabaseReference mDatabase;
    Spinner sp;
    ArrayAdapter<String> spinnerArrayAdapter;
    public static List<String> spinnerArray=  new ArrayList<String>();
    public static String waktu = "0";
    public static String suhu = "0";
    public static String detak_jantung = "0";
    public static String putaran = "0";
    public static String kecepatan = "0";
    public static String diagnosis = "";
    TextView tvWaktu, tvSuhu, tvDetak, tvPutaran, tvKecepatan, tvDiagnosis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        tvWaktu = (TextView)findViewById(R.id.textView8);
        tvSuhu = (TextView)findViewById(R.id.textView10);
        tvDetak = (TextView)findViewById(R.id.textView12);
        tvPutaran = (TextView)findViewById(R.id.textView14);
        tvKecepatan = (TextView)findViewById(R.id.textView16);
        tvDiagnosis = (TextView)findViewById(R.id.textView18);
        tvWaktu.setText(waktu);
        tvSuhu.setText(suhu);
        tvDetak.setText(detak_jantung);
        tvPutaran.setText(putaran);
        tvKecepatan.setText(kecepatan);
        sp = (Spinner)findViewById(R.id.spinner);

        if(Float.parseFloat(suhu)>=35 && Float.parseFloat(suhu)<=38
            && Float.parseFloat(detak_jantung)<220-Float.parseFloat(AkunActivity.usia)){
            tvDiagnosis.setText("Normal");
        } else {
            tvDiagnosis.setText("Tidak Normal");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("user_list");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String username = ds.getKey();
                    spinnerArray.add(username);
                }
                refreshData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HasilActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void refreshData()
    {
        spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        sp.setAdapter(spinnerArrayAdapter);
    }

    public void onWarning(View v)
    {
        mDatabase.child(sp.getSelectedItem().toString()).child("notif_dari").setValue(AkunActivity.nama);
        mDatabase.child(sp.getSelectedItem().toString()).child("notif_suhu").setValue(suhu);
        mDatabase.child(sp.getSelectedItem().toString()).child("notif_hearth_rate").setValue(detak_jantung);
        mDatabase.child(sp.getSelectedItem().toString()).child("notif_diagnosis").setValue(tvDiagnosis.getText().toString());
        mDatabase.child(sp.getSelectedItem().toString()).child("notify").setValue("1");

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
