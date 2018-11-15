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

package marabillas.loremar.taskador.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import marabillas.loremar.taskador.ui.components.InAppInterface;

/**
 * Listens to events in {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * {@link android.widget.EditText} which is for inputting new tasks.
 */
public class AddTaskBoxTextWatcher implements TextWatcher {
    private InAppInterface mainInApp;

    public AddTaskBoxTextWatcher(InAppInterface mainInApp) {
        this.mainInApp = mainInApp;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mainInApp.onAddTaskBoxTextChanged(s);
    }
}
