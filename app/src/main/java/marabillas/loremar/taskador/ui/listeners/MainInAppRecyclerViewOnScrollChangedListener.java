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

import android.support.v7.widget.RecyclerView;

import marabillas.loremar.taskador.ui.InAppInterface;

/**
 * Listener for scroll events in recycler view. List item selection in the in-app screen needs to
 * be cleared when scrolling. Otherwise, with uncleared selection, list items can not be swiped
 * except for the last selected item prior to scrolling.
 */
public class MainInAppRecyclerViewOnScrollChangedListener extends RecyclerView.OnScrollListener {
    private InAppInterface mainInApp;
    private boolean isKeepingSelection;

    public MainInAppRecyclerViewOnScrollChangedListener(InAppInterface mainInApp) {
        this.mainInApp = mainInApp;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!isKeepingSelection) {
            mainInApp.onListItemSelectionClear();
        }

        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!isKeepingSelection) {
            mainInApp.onListItemSelectionClear();
        }

        super.onScrolled(recyclerView, dx, dy);
    }

    /**
     * Disallow any scroll events in recycler view to clear selection of list item.
     *
     * @param keepingSelection true or false
     */
    public void setKeepingSelection(boolean keepingSelection) {
        isKeepingSelection = keepingSelection;
    }
}
