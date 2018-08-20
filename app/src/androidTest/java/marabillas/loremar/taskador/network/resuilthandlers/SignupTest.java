package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.SignupTask;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static marabillas.loremar.taskador.utils.LogUtils.logError;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class SignupTest extends ResultHandlerTest implements SignupTask.ResultHandler {
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
}
