package paiva.pedro.at_desenvolvimento_android.Repository;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConnectionFirebase {

    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static FirebaseUser firebaseUser;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        if(databaseReference == null){
            databaseReference = firebaseDatabase.getReference();
        }
        return databaseReference;
    }

    public static boolean checkFirebaseUser() {
        firebaseAuth = ConnectionFirebase.getFirebaseAuth();
        firebaseUser = ConnectionFirebase.getFirebaseUser();

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            initFirebaseAuth();
        }
        return firebaseAuth;
    }

    public static FirebaseUser getFirebaseUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            firebaseUser = user;
        }
        return firebaseUser;
    }

    private static void initFirebaseAuth(){
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    firebaseUser = user;
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static void initFirebaseAuthUser(){
        getFirebaseAuth();
        getFirebaseUser();
    }

    public static void logout(){
        firebaseAuth.signOut();
    }
}
