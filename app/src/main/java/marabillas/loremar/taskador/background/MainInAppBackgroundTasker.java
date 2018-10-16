package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * This interface contains methods which are to be invoked to execute background tasks for the
 * in-app screen.
 */
public interface MainInAppBackgroundTasker extends BackgroundTasker<MainInAppActivity> {
    /**
     * Method to invoke when in-app screen requires a list of to-do tasks
     */
    void fetchToDoTasksList();

    /**
     * Method to invoke when a user inputs a new to-do task
     *
     * @param task new task to add to account
     */
    void submitNewTask(String task);

    /**
     * Method to invoke when in-app screen requires a list of finished tasks
     */
    void fetchFinishedTasksList();

    /**
     * Method to invoke when in-app screen requires the list of top words
     *
     * @param numResults number of how many words are to be included in the list starting from
     *                   the top
     */
    void fetchTopWordsList(int numResults);

    /**
     * Method to invoke when in-app screen requires the list of excluded words
     */
    void fetchExcludedWordsList();
}
