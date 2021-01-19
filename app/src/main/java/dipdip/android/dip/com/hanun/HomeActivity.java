package dipdip.android.dip.com.hanun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dipdip.android.dip.com.hanun.Adapter.HomeAdapter;
import dipdip.android.dip.com.hanun.FCMService.NotificationSender;
import dipdip.android.dip.com.hanun.FCMService.Token;
import dipdip.android.dip.com.hanun.Model.HomeModel;
import dipdip.android.dip.com.hanun.Network.APIService;
import dipdip.android.dip.com.hanun.Network.Client;
import dipdip.android.dip.com.hanun.SessionManager.Preferences;


public class HomeActivity extends Activity {
    private APIService apiService;
    String CHANNEL_ID="10001";
    private DatabaseReference mDatabase, mDatabase2;
    private DatabaseReference mDatabaseHist;
    private TextView tv,tv2,tv3,tv4;
    private Button bt1;
    private LinearLayout linearLayout,linearLayout2;
    public boolean isMulai = false;
    public static int timer = 0;
    MediaPlayer notif;
    long millis;
    public static String lastMenit = "0";
    public static boolean notif2 = false;
    private FirebaseAuth mAuth;
    String uid;
    Button btnHsil;
    ListView listView;
    private LinearLayoutManager mManager;
    ArrayList<String> array = new ArrayList<>();
    Spinner spinner2;
    ListView listviewHanun;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayAdapter <String> adapter;
    HomeModel user;
    ArrayList<String> list,list2,listuid;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressDialog = new ProgressDialog(this);
        user = new HomeModel();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        listuid = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.user_info, R.id.userinfo, list);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        notif = MediaPlayer.create(this, R.raw.notifikasi);
//        listviewHanun = (ListView) findViewById(R.id.listview);
        tv = (TextView)findViewById(R.id.textView8);
        tv2 = (TextView)findViewById(R.id.textView10);
        tv3 = (TextView)findViewById(R.id.textView12);
        tv4 = (TextView)findViewById(R.id.textView14);
        bt1 = (Button)findViewById(R.id.button6);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        btnHsil  =(Button)findViewById(R.id.button8);
        btnHsil.setVisibility(View.INVISIBLE);
        millis = System.currentTimeMillis();


        mDatabase = FirebaseDatabase.getInstance().getReference("monitoring");
        mDatabaseHist = FirebaseDatabase.getInstance().getReference("riwayat");
        database = FirebaseDatabase.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        mDatabase2 = database.getReference("user_list");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                // Get Post object and use the values to update the UI
                double score = ds.child("suhu").getValue(Double.class);
                double score2 = ds.child("putaran").getValue(Double.class);
                double score3 = ds.child("rpm").getValue(Double.class);
                double score4 = ds.child("hearth_rate").getValue(Double.class);
                String usiaString = Preferences.getUsia(getBaseContext());
                double usia = Double.parseDouble(usiaString);
                if(isMulai && score2>0 && notif2 == false) {
                    if(score>38 || score4>(220-usia)){
                        notif.start();
                        notifikasi("STOP! DATA ANDA MELEBIHI BATAS NORMAL", "PERINGATAN !!!");
                        notif2 = true;
                    } else {
                        notif2 = false;
                    }
                    tv.setText(String.valueOf(score)+"C");
                    tv2.setText(String.valueOf(score2));
                    tv3.setText(String.valueOf(score3)+"RPM");
                    tv4.setText(String.valueOf(score4)+"Bpm");
                    if(System.currentTimeMillis()-millis>1000){
                        //RiwayatActivity.listSuhu.add(score);
                        //RiwayatActivity.listPutaran.add(score2);
                        //RiwayatActivity.listKecepatan.add(score3);
                        millis = System.currentTimeMillis();

                        String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String waktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String menit = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
                        if(!lastMenit.equals(menit)) {
                            mDatabaseHist.child("putaran").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score2);
                            mDatabaseHist.child("rpm").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score3);
                            mDatabaseHist.child("suhu").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score);
                            mDatabaseHist.child("hearth_rate").child(LoginActivity.uname +"_"+ tanggal + "_" + waktu).setValue(score4);
                            HasilActivity.detak_jantung = String.valueOf(score4);
                            HasilActivity.suhu = String.valueOf(score);
                            HasilActivity.putaran = String.valueOf(score2);
                            HasilActivity.kecepatan = String.valueOf(score3);
                            lastMenit=menit;
                        }

                    }
                } else {

                }
                tv.setText(score + "C");
                tv2.setText(score2 + "");
                tv3.setText(score3 + "RPM");
                tv4.setText(score4 + "Bpm");
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(HomeActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        user = ds.getValue(HomeModel.class);
                        list.add(user.nama);
                        list2.add(user.token);
                        listuid.add(user.uid);
                    }
//                    listviewHanun.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     /*   listviewHanun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nama = list.get(position);
                String token = list2.get(position);
                String uid = listuid.get(position);
                Toast.makeText(HomeActivity.this, "Hasilnyaaa" + nama, Toast.LENGTH_SHORT).show(); //mengecek
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                Map<String, Object> data = new HashMap<>();
                data.put("message", "Bagi hasil ");
                data.put("from", uid);
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
        });*/
        UpdateToken();

        btnHsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hasil = tv.getText().toString();
                Intent i = new Intent(getBaseContext(), HasilActivity.class);
                i.putExtra("suhu", tv.getText().toString());
                i.putExtra("deta_jantung", tv4.getText().toString());
                i.putExtra("putaran", tv2.getText().toString());
                i.putExtra("kecepatan", tv3.getText().toString());
                Toast.makeText(HomeActivity.this, hasil, Toast.LENGTH_LONG).show();
                startActivity(i);
/*                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);*/
            }

        });




        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isMulai){
                    if(timer>0)timer--;
                    if(timer<=0){
                        isMulai=!isMulai;
                        bt1.setText("Mulai");
                        notif.start();

                        HomeActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                btnHsil.setVisibility(View.VISIBLE);
                            }

                        });
                        //Toast.makeText(HomeActivity.this, "Waktu Habis", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, 0, 1000);
    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }


    public void onMulai(View v)
    {
        if(isMulai){
            isMulai=!isMulai;
            bt1.setText("Mulai");
        } else {
            timer = Integer.valueOf(spinner2.getSelectedItem().toString())*60;
            HasilActivity.waktu = spinner2.getSelectedItem().toString();
            RiwayatActivity.listKecepatan.clear();
            RiwayatActivity.listPutaran.clear();
            RiwayatActivity.listSuhu.clear();
            mDatabase.child("hearth_rate").setValue(0);
            mDatabase.child("putaran").setValue(0);
            mDatabase.child("rpm").setValue(0);
            mDatabase.child("suhu").setValue(0);
            if(timer>0) {
                isMulai = !isMulai;
                bt1.setText("Stop");
            } else {
                Toast.makeText(this, "Waktu Belum Di isi", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void notifikasi(String pesan, String pengirim)
    {
        String notification_title = pengirim;
        String notification_message = pesan;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);
        Intent intent = new Intent(this, Notifikasi2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
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
    public void onHasil(View v)
    {

        Intent i = new Intent(getBaseContext(), HasilActivity.class);
        startActivity(i);
        finish();
    }



}
