package marabillas.loremar.taskador.ui.activity;

import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SplashActivityTest {
    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>
            (SplashActivity.class);

    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
