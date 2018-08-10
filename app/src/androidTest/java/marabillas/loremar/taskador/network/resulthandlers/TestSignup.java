package marabillas.loremar.taskador.network.resulthandlers;

import marabillas.loremar.taskador.network.SignupTask;

public class TestSignup implements SignupTask.ResultHandler {
    private Result result;

    public enum Result {
        NEW_ACCOUNT_SAVED,
        FAILED_TO_SUBMIT_NEW_ACCOUNT,
        BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT
    }

    @Override
    public synchronized void newAccountSaved(String message) {
        result = Result.NEW_ACCOUNT_SAVED;
        notify();
    }

    @Override
    public synchronized void failedToSubmitNewAccount(String message) {
        result = Result.FAILED_TO_SUBMIT_NEW_ACCOUNT;
        notify();
    }

    @Override
    public synchronized void backEndUnableToSaveNewAccount(String message) {
        result = Result.BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT;
        notify();
    }

    public Result getResult() {
        return result;
    }
}
