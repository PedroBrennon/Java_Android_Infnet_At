package paiva.pedro.at_desenvolvimento_android.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;

import paiva.pedro.at_desenvolvimento_android.Model.UserModel;
import paiva.pedro.at_desenvolvimento_android.R;

import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.firebaseAuth;
import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.firebaseUser;
import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.getDatabaseReference;
import static paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes.UsersDB;

public class InitialActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        findIds();
        eventClicks();
        facebook();
    }

    private void facebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.loginButtonFb);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        //Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Log.d(TAG, "facebook:onError", error);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();

                            UserModel user = new UserModel();
                            user.setId(firebaseUser.getUid());
                            user.setName(firebaseUser.getDisplayName());
                            user.setEmail(firebaseUser.getEmail());
                            getDatabaseReference().child(UsersDB).child(user.getId()).setValue(user);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
    }

    private View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    };

    private View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    };

    private void findIds(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void eventClicks(){
        btnRegister.setOnClickListener(register);
        btnLogin.setOnClickListener(login);
    }
}
