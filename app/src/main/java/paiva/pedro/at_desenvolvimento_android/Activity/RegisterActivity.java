package paiva.pedro.at_desenvolvimento_android.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import paiva.pedro.at_desenvolvimento_android.Utility.ApplyMasks;
import paiva.pedro.at_desenvolvimento_android.R;
import paiva.pedro.at_desenvolvimento_android.Model.UserModel;
import paiva.pedro.at_desenvolvimento_android.Utility.Cryptography;
import paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes;

import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.firebaseAuth;
import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.getDatabaseReference;
import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.logout;
import static paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes.UsersDB;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword, edtCpf;
    private String name, email, password, confirmPassword, cpf;
    private Button btnSignUp;

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findIds();
        eventClicks();
    }

    private View.OnClickListener signUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            convertFieldsInStrings();

            if(!validateFields()){
                return;
            }

            userModel = new UserModel();
            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setPassword(password);
            userModel.setConfirmPassword(confirmPassword);
            userModel.setCpf(cpf);

            authAndDatabase(userModel);
        }
    };

    private void authAndDatabase(final UserModel model){
        firebaseAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel u = model;
                            try {
                                u.setId(task.getResult().getUser().getUid());
                                u.setPassword(Cryptography.encrypt(u.getPassword()));
                                u.setConfirmPassword(Cryptography.encrypt(u.getPassword()));
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }

                            getDatabaseReference().child(UsersDB).child(u.getId()).setValue(u);
                            DefaultAttributes.toastMessage(getApplicationContext(), "Usuário cadastrado com sucesso");

                            logout();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }

    //convert Fields of the layout in Strings
    private void convertFieldsInStrings(){
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();
        cpf = edtCpf.getText().toString();
    }

    //validate the fields of the layout.
    private boolean validateFields(){
        boolean result = true;

        Pattern emailRegex = Patterns.EMAIL_ADDRESS;
        Matcher mEmail = emailRegex.matcher(email);

        Pattern cpfRegex = Pattern.compile("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)");
        Matcher mCpf = cpfRegex.matcher(cpf);

        if(name.equals("") || name == null || name.length() < 3 || name.matches(".*\\d+.*")){
            edtName.setError("Digite um nome válido");
            result = false;
        }
        if(!mEmail.find() || email.equals("") || email == null){
            edtEmail.setError("Digite um email válido");
            result = false;
        }
        if(password.equals("") || password == null || password.length() < 6){
            edtPassword.setError("Digite uma senha válida (min. 6 dígitos)");
            result = false;
        }
        if(!confirmPassword.equals(password) || confirmPassword.equals("") || confirmPassword == null){
            edtConfirmPassword.setError("Digite uma confirmação de senha válida");
            result = false;
        }
        if(!mCpf.find() || cpf.equals("") || cpf == null){
            edtCpf.setError("Digite um cpf válido");
            result = false;
        }

        return result;
    }

    private void eventClicks(){
        btnSignUp.setOnClickListener(signUp);
    }

    //find id of the objects of the layout.
    private void findIds(){
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtCpf = findViewById(R.id.edtCpf);
        edtCpf.addTextChangedListener(ApplyMasks.mask(edtCpf, ApplyMasks.FORMAT_CPF));
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}
