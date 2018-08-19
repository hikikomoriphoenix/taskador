package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static marabillas.loremar.taskador.utils.LogUtils.logError;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BackEndAPICallTaskerTest {
    @Test
    public void signup() {
        final String username = "test2";
        final String password = "password";
        final CountDownLatch pause = new CountDownLatch(1);

        SignupTask.ResultHandler resultHandler = new SignupTask.ResultHandler() {
            @Override
            public void newAccountSaved(String message) {
                log(message);
                assertThat(message, is("Back-end process success."));
                pause.countDown();
            }

            @Override
            public void failedToSubmitNewAccount(String message) {
                logError(message);
                assertNotNull(message);
                pause.countDown();
            }

            @Override
            public void backEndUnableToSaveNewAccount(String message) {
                logError(message);
                assertNotNull(message);
                pause.countDown();
            }
        };

        BackEndAPICallTasker.getInstance().signup(resultHandler, username, password);

        try {
            pause.await();
        } catch (InterruptedException e) {
            Assert.fail("Await interrupted: " + e.getMessage());
        }
    }
}