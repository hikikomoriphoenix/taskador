package marabillas.loremar.taskador.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.background.SplashBackgroundTasker;

public class SplashActivityTest {
    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule;

    @Test
    public void testLogin() {
        login("test1", "password");
        await();
    }

    @Test
    public void testLoginWrongUsername() {
        login("userunknown", "password");
        await();
    }

    @Test
    public void testLoginWrongPassword() {
        login("test1", "passport");
        await();
    }

    private void login(String username, String password) {
        Intent intent = new Intent(App.getInstance(), SplashActivity.class);

        Bundle input = new Bundle();
        input.putInt("action", SplashBackgroundTasker.Action.LOGIN.ordinal());
        input.putString("username", username);
        input.putString("password", password);
        intent.putExtra("input", input);

        App.getInstance().setBackgroundTaskManagerSupport(true);

        activityTestRule = new ActivityTestRule<>(SplashActivity.class);
        activityTestRule.launchActivity(intent);
    }

    private void await() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
