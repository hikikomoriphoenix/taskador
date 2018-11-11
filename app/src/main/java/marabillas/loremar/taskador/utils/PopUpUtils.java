/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
