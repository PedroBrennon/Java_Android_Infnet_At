package paiva.pedro.at_desenvolvimento_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import paiva.pedro.at_desenvolvimento_android.R;

import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.initFirebaseAuthUser;
import static paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes.SPLASH_TIME_OUT;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        initFirebaseAuthUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), InitialActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
