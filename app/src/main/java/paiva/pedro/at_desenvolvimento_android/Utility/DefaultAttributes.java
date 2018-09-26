package paiva.pedro.at_desenvolvimento_android.Utility;

import android.content.Context;
import android.widget.Toast;

public class DefaultAttributes {

    public static int SPLASH_TIME_OUT = 3000;
    public static final String URL = "http://demo1846932.mockable.io/";
    public static final String UsersDB = "Users";

    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
