package fr.plopez.mareu.view.view_utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fr.plopez.mareu.R;

public class CustomToasts {

    public static void showErrorToast(Context context, String message) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_toast_layout, null);

        Toast toast = new Toast(context);

        TextView toastMessage = view.findViewById(R.id.custom_toast_message_text);
        toastMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
}
