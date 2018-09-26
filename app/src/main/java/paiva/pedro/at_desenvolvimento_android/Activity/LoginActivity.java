package paiva.pedro.at_desenvolvimento_android.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import paiva.pedro.at_desenvolvimento_android.R;

import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.checkFirebaseUser;
import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.firebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findIds();
        eventClicks();
    }

    private View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(checkFirebaseUser()){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                        }
                    });
        }
    };

    private View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    };

    private void eventClicks(){
        btnLogin.setOnClickListener(login);
        btnRegister.setOnClickListener(register);
    }

    private void findIds(){
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }
}
