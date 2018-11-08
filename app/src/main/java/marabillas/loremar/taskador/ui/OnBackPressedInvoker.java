package marabillas.loremar.taskador.ui;

/**
 * Interface for object that is notified of back button press events and in turn notifies an
 * {@link OnBackPressedListener}.
 */
public interface OnBackPressedInvoker {
    /**
     * Set the listener that will be notified of back button press events.
     */
    void setOnBackPressedListener(OnBackPressedListener onBackPressedListener);
}
