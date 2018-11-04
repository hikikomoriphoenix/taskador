package marabillas.loremar.taskador.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Utility class for displaying pop-up dialogs.
 */
public final class PopUpUtils {
    private PopUpUtils() {
    }

    /**
     * Dislay a pop-up dialog showing error and an OK button to exit dialog.
     *
     * @param context         context required to create the dialog.
     * @param message         error message
     * @param onClickListener listens for OK button clicks. This is primarily used for executing
     *                        commands when user clicks the OK button.
     */
    public static void showErrorPopUp(Context context, String message, DialogInterface
            .OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle("ERROR!")
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .create()
                .show();
    }
}
