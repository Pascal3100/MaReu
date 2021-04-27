package fr.plopez.mareu.view;

import android.content.Context;
import android.widget.Toast;

import fr.plopez.mareu.R;

public class CustomToasts {

    public static void showErrorToast(Context context, String message) {
        Toast toast = Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.drawable.toast_drawable);
        toast.show();
    }
}
