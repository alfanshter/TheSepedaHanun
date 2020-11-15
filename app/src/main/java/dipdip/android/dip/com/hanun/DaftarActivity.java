package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import dipdip.android.dip.com.hanun.SessionManager.Preferences;

public class DaftarActivity extends Activity {
    private DatabaseReference mDatabase;
    EditText et1, et2, et3, et4;
    RadioGroup rad1;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    public boolean setted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        et1 = (EditText)findViewById(R.id.editText2);
        et2 = (EditText)findViewById(R.id.editText4);
        et3 = (EditText)findViewById(R.id.editText5);
        et4 = (EditText)findViewById(R.id.editText6);
        rad1 = (RadioGroup)findViewById(R.id.radgroup);
        mFirestore = FirebaseFirestore.getInstance();

    }

    public void onDaftar(View v)
    {
        mAuth = FirebaseAuth.getInstance();
        final String nama = et1.getText().toString();
        final String usia = et2.getText().toString();
        final String username = et3.getText().toString();
        final String password = et4.getText().toString();

        mAuth.createUserWithEmailAndPassword(nama, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                            String hanun = user.getUid();
                            HashMap<String, String> days = new HashMap<>();

                            // mengisi nilai ke objek days
                            days.put("user_list", username );
                            days.put("usia", usia);
                            days.put("password", password);
                            days.put("jenis_kelamin", "---");
                            days.put("notify", "0");
                            days.put("notif_suhu", "0");
                            days.put("notif_hearth_rate", "0");
                            days.put("notif_diagnosis", "0");
                            days.put("notif_dari", "0");
                            days.put("nama", username);
                            days.put("token", refreshedToken);
                            days.put("uid", hanun);
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("user_list");
                            mDatabase.child(hanun).setValue(days);
                            Map<String, Object> data = new HashMap<>();
                            data.put("uid", hanun);
                            data.put("token_id", refreshedToken);
                            mFirestore.collection("Users").document(hanun).set(data);

                            setted = true;
                            Preferences.setLoggedInUser(getBaseContext(),Preferences.getRegisteredUser(getBaseContext()));
                            Preferences.setLoggedInStatus(getBaseContext(),true);
                            Intent i = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(DaftarActivity.this, "Daftar Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onKembali(View v)
    {
        finish();
    }
}
