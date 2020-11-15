package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class RiwayatActivity extends Activity {
    public int year, month, day;
    public String sYear, sMonth, sDay;
    public static String tglLengkap="";
    TextView tvTanggal;
    private Calendar calendar;
    public boolean dateSelected = false;
    public static ArrayList<Double> listSuhu = new ArrayList<Double>();
    public static ArrayList<Double> listKecepatan = new ArrayList<Double>();
    public static ArrayList<Double> listPutaran = new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        tvTanggal = (TextView)findViewById(R.id.textView24);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    tvTanggal.setText(String.valueOf(arg3)+"-"+String.valueOf(arg2)+"-"+String.valueOf(arg1));
                    sDay = String.valueOf(arg3);
                    sMonth = String.valueOf(arg2);
                    sYear = String.valueOf(arg1);
                    if(arg3<10)sDay = "0"+sDay;
                    if(arg2<10)sMonth = "0"+sMonth;
                    if(arg1<10)sYear = "0"+sYear;
                    tglLengkap = sDay+"-"+sMonth+"-"+sYear;
                    dateSelected = true;
                }
            };

    @SuppressWarnings("deprecation")
    public void onTanggal(View view) {
        showDialog(999);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    public void onSuhu(View v)
    {
        GrafikActivity.jenis="suhu";
        Intent i = new Intent(getBaseContext(), GrafikActivity.class);
        startActivity(i);
    }
    public void onKecepatan(View v)
    {
        GrafikActivity.jenis="rpm";
        Intent i = new Intent(getBaseContext(), GrafikActivity.class);
        startActivity(i);
    }
    public void onPutaran(View v)
    {
        GrafikActivity.jenis="putaran";
        Intent i = new Intent(getBaseContext(), GrafikActivity.class);
        startActivity(i);
    }
    public void onDetakJantung(View v)
    {
        GrafikActivity.jenis="hearth_rate";
        Intent i = new Intent(getBaseContext(), GrafikActivity.class);
        startActivity(i);
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
