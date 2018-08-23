package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.LoginTask;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static marabillas.loremar.taskador.utils.LogUtils.logError;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class LoginTest extends ResultHandlerTest implements LoginTask.ResultHandler {
    @Override
    public void loggedInSuccessfuly(String message) {
        log(message);
        assertThat(message, is("Back-end process success."));
        pause.countDown();
    }

    @Override
    public void failedToSubmitLogin(String message) {
        logError(message);
        assertNotNull(message);
        pause.countDown();
    }

    @Override
    public void loginDenied(String message) {
        logError(message);
        assertNotNull(message);
        pause.countDown();
    }
}
