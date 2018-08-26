package marabillas.loremar.taskador.network.resuilthandlers;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static marabillas.loremar.taskador.utils.LogUtils.logError;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * This is the base class for all the ResultHandlers that will be used in the tests.
 */
public abstract class ResultHandlerTest {
    /**
     * CountdownLatch is used to wait for results. countDown function must be called as soon as
     * results are received.
     */
    private CountDownLatch resultWaiter;

    ResultHandlerTest() {
        resultWaiter = new CountDownLatch(1);
    }

    public void waitForResults() {
        try {
            resultWaiter.await();
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }

    /*CountDownLatch getResultWaiter() {
        return resultWaiter;
    }*/

    void handleSuccess(String message) {
        log(message);
        assertThat(message, is("Back-end process success."));
        resultWaiter.countDown();
    }

    void handleFailure(String message) {
        logError(message);
        assertNotNull(message);
        resultWaiter.countDown();
    }
}
