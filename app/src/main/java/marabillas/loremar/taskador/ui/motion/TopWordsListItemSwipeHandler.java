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

package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 */
public class TopWordsListItemSwipeHandler extends ListItemSwipeHandler {
    public TopWordsListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.RIGHT);
    }

    @Override
    boolean checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getTopWordsFragment().getRecyclerView().getWidth();
        return Math.abs(translation) > 0.50 * totalWidth;
    }

    @Override
    void performActionOnMarkedItem(MainInAppActivity mainInAppActivity) {
        mainInAppActivity.onWordSwipedToMark();
    }
}
