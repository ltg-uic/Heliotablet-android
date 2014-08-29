package ltg.heliotablet_android;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MiscUtil {

    public static void makeTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
        toast.show();
    }
}
