package marabillas.loremar.taskador.network.resuilthandlers;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;

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

    CountDownLatch getResultWaiter() {
        return resultWaiter;
    }
}
