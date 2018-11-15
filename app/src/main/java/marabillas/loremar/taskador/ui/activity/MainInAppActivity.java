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

package marabillas.loremar.taskador.ui.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import marabillas.loremar.taskador.ConfigKeys;
import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.MainInAppManager;
import marabillas.loremar.taskador.ui.components.MainInApp;

/**
 * Activity for main in-app screen. This screen allows the user to list to-do tasks, show
 * finished tasks and also features listing of most frequently used words for tasks. These
 * features are contained in a {@link ViewPager} allowing the user to swipe between them.
 */
public class MainInAppActivity extends BaseAppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private MainInApp mainInApp;
    private ViewGroup contentView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininapp);

        // Show the user currently logged in on the top.
        TextView userView = findViewById(R.id.activity_maininapp_user);
        SharedPreferences prefs = getSharedPreferences("config", 0);
        String username = prefs.getString(ConfigKeys.CURRENT_ACCOUNT_USERNAME, null);
        String loggedInAsUser = "Logged in as " + username;
        userView.setText(loggedInAsUser);

        contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mainInApp = new MainInApp(MainInAppActivity.this);
        mainInApp.setUp();
    }

    @Override
    public void onSetupBackgroundService() {
        mainInApp.setupBackgroundService(MainInAppManager.class);
    }

    @Override
    public void onGlobalLayout() {
        // Try to remove focus from EditText when soft keyboard is closed.
        // When soft keyboard closes the height of visible portion of the screen becomes equal to
        // the height of the total screen. In other words, the screen becomes completely visible.
        Rect visibleRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(visibleRect);
        int totalHeight = contentView.getRootView().getHeight();
        int visibleHeight = visibleRect.bottom;
        if (visibleHeight == totalHeight) {
            if (getCurrentFocus() != null) {
                getCurrentFocus().clearFocus();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mainInApp.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public MainInApp getMainInApp() {
        return mainInApp;
    }
}