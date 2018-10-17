package marabillas.loremar.taskador.network.resuilthandlers;

import junit.framework.Assert;

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.tasks.CheckUsernameAvailabilityTask;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckUsernameAvailabilityTest extends ResultHandlerTest implements
        CheckUsernameAvailabilityTask.ResultHandler {
    @Override
    public void UsernameAvailabilityCheckResultsObtained(String message, JSON data) {
        try {
            boolean available = data.getBoolean("available");
            assertThat(available, is(false));
        } catch (FailedToGetFieldException e) {
            Assert.fail(e.getMessage());
        }

        handleSuccess(message);
    }

    @Override
    public void failedUsernameAvailabilityCheckRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void BackendFailedToCheckUsernameAbility(String message) {
        handleFailure(message);
    }

    @Override
    public void CheckUsernameAvailabilityTaskIncomplete(String message) {
        handleFailure(message);
    }
}
