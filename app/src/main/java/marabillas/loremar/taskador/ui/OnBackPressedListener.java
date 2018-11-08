package marabillas.loremar.taskador.ui;

/**
 * Listener that should be notified for back button press events.
 */
public interface OnBackPressedListener {
    /**
     * Callback invoked when the back button is pressed. The listener also gets a reference to
     * the {@link OnBackPressedInvoker} that is notifying this listener.
     *
     * @param onBackPressedInvoker the {@link OnBackPressedInvoker} that is notifying this
     *                             listener of back button press events.
     */
    void onBackPressed(OnBackPressedInvoker onBackPressedInvoker);
}
