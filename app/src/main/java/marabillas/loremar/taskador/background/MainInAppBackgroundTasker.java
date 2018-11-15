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

package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.InAppInterface;

/**
 * This interface contains methods which are to be invoked to execute background tasks for the
 * in-app screen.
 */
public interface MainInAppBackgroundTasker extends BackgroundTasker<InAppInterface> {
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
     * Method to invoke to delete a to-do task from account
     *
     * @param position position of task in the list to be deleted
     */
    void deleteToDoTask(int position);

    /**
     * Method to invoke when a user sets a task as finished
     *
     * @param position position  of the task in the list of to-do tasks that is to be set as
     *                 finished.
     */
    void submitFinishedTask(int position);

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

    /**
     * Method to invoke when a word is to be set excluded or not excluded from top words.
     *
     * @param selectedItemPosition position of the word in the list to bet set excluded or not
     *                             excluded.
     * @param excluded             set to 1 if word is to be set excluded and 0 if to be set not excluded
     */
    void setExcluded(int selectedItemPosition, int excluded);
}
