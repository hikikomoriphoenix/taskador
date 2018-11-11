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

import android.view.View;
import android.widget.AdapterView;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listens to events in {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.widget.Spinner} which is used for selecting number of results for top words list.
 */
public class TopWordsNumResultsSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MainInAppActivity mainInAppActivity;

    public TopWordsNumResultsSpinnerItemSelectedListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mainInAppActivity.onChangeTopWordsNumResults(10);
                break;
            case 1:
                mainInAppActivity.onChangeTopWordsNumResults(20);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
