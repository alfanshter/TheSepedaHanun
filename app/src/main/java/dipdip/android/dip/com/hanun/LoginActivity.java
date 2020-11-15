package dipdip.android.dip.com.hanun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dipdip.android.dip.com.hanun.SessionManager.Preferences;

public class LoginActivity extends Activity {
    private DatabaseReference mDatabase;
    EditText etUsername, etPassword;
    private FirebaseAuth mAuth;
    public static String uname;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirestore = FirebaseFirestore.getInstance();


        etUsername = (EditText)findViewById(R.id.editText);
        etPassword = (EditText)findViewById(R.id.editText3);

    }
    public void onLogin(View v)
    {
        mAuth = FirebaseAuth.getInstance();
        final String token = FirebaseInstanceId.getInstance().getToken();
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String hanun = user.getUid();
                            mFirestore = FirebaseFirestore.getInstance();
                            Map<String, Object> data = new HashMap<>();
                            data.put("uid", hanun);
                            data.put("token_id", refreshedToken);
                            mFirestore.collection("Users").document(hanun).set(data);

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("user_list");
                            mDatabase.child(hanun).child("token").setValue(refreshedToken);
                            Preferences.setLoggedInUser(getBaseContext(),Preferences.getRegisteredUser(getBaseContext()));
                            Preferences.setLoggedInStatus(getBaseContext(),true);
                            Intent i = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(i);
                            finish();


                        } else {

                        }

                        // ...
                    }
                });
    }
    public void onDaftar(View v)
    {
        Intent i = new Intent(getBaseContext(), DaftarActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),HomeActivity.class));
            finish();
        }
    }
}
