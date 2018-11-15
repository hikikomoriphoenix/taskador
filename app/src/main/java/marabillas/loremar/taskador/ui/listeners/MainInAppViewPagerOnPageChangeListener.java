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

import android.support.v4.view.ViewPager;

import marabillas.loremar.taskador.ui.InAppInterface;

/**
 * Listens to events in in-app screen's {@link ViewPager}.
 */
public class MainInAppViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private InAppInterface mainInApp;

    public MainInAppViewPagerOnPageChangeListener(InAppInterface mainInApp) {
        this.mainInApp = mainInApp;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mainInApp.onListItemSelectionClear();
        switch (position) {
            case 0:
                mainInApp.onTodoTasksWindowSelected();
                break;
            case 1:
                mainInApp.onFinishedTasksWindowSelected();
                break;
            case 2:
                mainInApp.onTopWordsWindowSelected();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
