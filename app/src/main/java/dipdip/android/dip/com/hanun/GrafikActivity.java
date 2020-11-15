package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GrafikActivity extends Activity {
    public static String jenis = "suhu";
    public String dbg = "";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik);
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });*/
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        /*
        if(jenis.equals("suhu")){
            for(int i=0;i<RiwayatActivity.listSuhu.size();i++){
                series.appendData(new DataPoint(RiwayatActivity.listSuhu.get(i), i),true, RiwayatActivity.listSuhu.size());
            }
        }
        if(jenis.equals("kecepatan")){
            for(int i=0;i<RiwayatActivity.listKecepatan.size();i++){
                series.appendData(new DataPoint(RiwayatActivity.listKecepatan.get(i), i),true, RiwayatActivity.listKecepatan.size());
            }
        }if(jenis.equals("putaran")){
            for(int i=0;i<RiwayatActivity.listPutaran.size();i++){
                series.appendData(new DataPoint(RiwayatActivity.listPutaran.get(i), i),true, RiwayatActivity.listPutaran.size());
            }
        }

         */



        mDatabase = FirebaseDatabase.getInstance().getReference("riwayat").child(jenis);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int ind = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey().toString();
                    String value = ds.getValue().toString();
                    String parts[] = key.split("_");
                    //Toast.makeText(GrafikActivity.this, parts[1], Toast.LENGTH_SHORT).show();
                    if(parts[0].equals(LoginActivity.uname)){
                        series.appendData(new DataPoint(ind,Float.valueOf(value)),true, 100);
                        ind++;
                    }
                }
                graph.addSeries(series);

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(GrafikActivity.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void onKembali(View v)
    {
        finish();
    }

}
