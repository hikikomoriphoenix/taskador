package marabillas.loremar.taskador.ui.listeners;

import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.view.ExpandingDialogView;

/**
 * Listens to click events in in-app screen. Some components in the screen may have their own
 * listeners.
 */
public class MainInAppOnClickListener implements View.OnClickListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppOnClickListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_todotasks_addtask_button:
                mainInAppActivity.onAddTaskButtonClicked();
                break;

            case R.id.fragment_topwords_viewbutton:
                mainInAppActivity.onTopWordsViewButtonClicked();
                break;

            case R.id.activtiy_maininapp_reloadtool:
                mainInAppActivity.onReloadClicked();
                break;

            case R.id.activity_maininapp_helptool:
                int page = mainInAppActivity.getPager().getCurrentItem();
                switch (page) {
                    case 0: // To-do tasks page is the current page.
                        int todoTasksHelpText = R.string.todotasks_help_contents;
                        showExpandingDialog(v, todoTasksHelpText);
                        break;

                    case 1: // Finished tasks page is the current page.
                        int finishedTasksHelpText = R.string.finishedtasks_help_contents;
                        showExpandingDialog(v, finishedTasksHelpText);
                        break;

                    case 2: // Top words page is the current page.
                        int topWordsHelpText = R.string.topwords_help_contents;
                        showExpandingDialog(v, topWordsHelpText);
                        break;
                }
                break;

            case R.id.activity_maininapp_abouttool:
                View content = View.inflate(mainInAppActivity, R.layout
                        .activity_maininapp_about_content, null);

                TextView textView = content.findViewById(R.id.activity_maininapp_about_content_text);
                Spanned text = Html.fromHtml(mainInAppActivity.getString(R.string.about_contents));
                textView.setText(text);
                textView.setVerticalScrollBarEnabled(true);
                textView.setMaxLines(500);
                textView.setMovementMethod(new ScrollingMovementMethod());

                showExpandingDialog(v, content);
                break;

            case R.id.activity_maininapp_logouttool:
                mainInAppActivity.logout();
                break;
        }
    }

    private void showExpandingDialog(View v, int textResId) {
        TextView textView = new TextView(mainInAppActivity);
        textView.setVerticalScrollBarEnabled(true);
        textView.setMaxLines(500);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Spanned text = Html.fromHtml(mainInAppActivity.getString(textResId));
        textView.setText(text);

        showExpandingDialog(v, textView);
    }

    private void showExpandingDialog(View tool, View content) {
        ViewGroup parent = (ViewGroup) mainInAppActivity.getWindow().getDecorView()
                .getRootView();

        // Get the center of the tool that will serve as the starting point of the expanding dialog.
        int startX = tool.getLeft() + tool.getWidth() / 2;
        int startY = tool.getTop() + tool.getHeight() / 2;

        ExpandingDialogView dialogView = new ExpandingDialogView(mainInAppActivity,
                parent);
        mainInAppActivity.setOnBackPressedListener(dialogView);
        dialogView.show(startX, startY, content);
    }
}
