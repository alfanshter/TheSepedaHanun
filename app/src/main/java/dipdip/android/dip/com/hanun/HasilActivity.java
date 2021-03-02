package dipdip.android.dip.com.hanun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dipdip.android.dip.com.hanun.FCMService.Token;
import dipdip.android.dip.com.hanun.Model.HomeModel;

public class HasilActivity extends Activity {
    private DatabaseReference mDatabase,mDatabase2;
    Spinner sp;
    String CHANNEL_ID="10001";
    ArrayAdapter<String> spinnerArrayAdapter;
    public static List<String> spinnerArray=  new ArrayList<String>();
    public static String waktu = "0";
    public static String suhu = "0";
    public static String detak_jantung = "0";
    public static String putaran = "0";
    public static String kecepatan = "0";
    public static String diagnosis = "";
    private Button buttonbagikan;
    ArrayAdapter <String> adapter;
    private LinearLayout linearLayout,linearLayout2;
    TextView tvWaktu, tvSuhu, tvDetak, tvPutaran, tvKecepatan, tvDiagnosis;
    ListView listviewHanun;
    ArrayList<String> list,list2,listuid;
    HomeModel user;
    ProgressDialog progressDialog;
    FirebaseDatabase database;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        progressDialog = new ProgressDialog(this);
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        final String UserID = auth.getUid();
        String suhu_hasil = getIntent().getStringExtra("suhu");
        String data_jantung_hasil = getIntent().getStringExtra("deta_jantung");
        String putaran_hasil = getIntent().getStringExtra("putaran");
        String kecepatan_hasil = getIntent().getStringExtra("kecepatan");
        String waktu_hasil = getIntent().getStringExtra("waktu");
        user = new HomeModel();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        listuid = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.user_info, R.id.userinfo, list);
        waktu = suhu_hasil;
        detak_jantung = data_jantung_hasil;
        putaran = putaran_hasil;
        kecepatan = kecepatan_hasil;
               listviewHanun = (ListView) findViewById(R.id.listview);
        tvWaktu = (TextView)findViewById(R.id.textView8);
        tvSuhu = (TextView)findViewById(R.id.textView10);
        tvDetak = (TextView)findViewById(R.id.textView12);
        tvPutaran = (TextView)findViewById(R.id.textView14);
        tvKecepatan = (TextView)findViewById(R.id.textView16);
        tvDiagnosis = (TextView)findViewById(R.id.textView18);
        buttonbagikan = (Button) findViewById(R.id.btn_bagihasil);
        linearLayout = (LinearLayout) findViewById(R.id.linear1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
        tvWaktu.setText(waktu_hasil);
        tvSuhu.setText(waktu);
        tvDetak.setText(detak_jantung);
        tvPutaran.setText(putaran);
        tvKecepatan.setText(kecepatan);

        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        mFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase2 = database.getReference("user_list");

        if(Float.parseFloat(suhu)>=35 && Float.parseFloat(suhu)<=38
            && Float.parseFloat(detak_jantung)<220-Float.parseFloat(AkunActivity.usia)){
            tvDiagnosis.setText("Normal");
            diagnosis = "Normal";
        } else {
            tvDiagnosis.setText("Tidak Normal");
            diagnosis = "Tidak Normal";
        }

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        user = ds.getValue(HomeModel.class);
                        if (user!=null){
                            list.add(user.nama);
                            list2.add(user.token);
                            listuid.add(user.uid);
                        }
                    }
                    listviewHanun.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listviewHanun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nama = list.get(position);
                String token = list2.get(position);
                String uid = listuid.get(position);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                Map<String, Object> data = new HashMap<>();
                data.put("message", "Bagi hasil ");
                data.put("from", UserID);
                data.put("suhu" , waktu);
                data.put("detak_jantung" ,detak_jantung);
                data.put("diagnosis" ,diagnosis);
                mFirestore.collection("Users/"+uid+"/Notifications").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            linearLayout.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
               UpdateToken();


        buttonbagikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
            }
        });
    }


    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
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
