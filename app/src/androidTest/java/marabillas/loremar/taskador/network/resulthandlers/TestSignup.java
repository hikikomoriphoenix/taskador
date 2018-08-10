package marabillas.loremar.taskador.network.resulthandlers;

import java.util.concurrent.CountDownLatch;

import marabillas.loremar.taskador.network.SignupTask;

public class TestSignup implements SignupTask.ResultHandler {
    private Result result;
    private String message;
    private CountDownLatch countDownLatch;

    public TestSignup() {
        countDownLatch = new CountDownLatch(1);
    }

    public enum Result {
        NEW_ACCOUNT_SAVED,
        FAILED_TO_SUBMIT_NEW_ACCOUNT,
        BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT
    }

    @Override
    public void newAccountSaved(String message) {
        result = Result.NEW_ACCOUNT_SAVED;
        this.message = message;
    }

    @Override
    public void failedToSubmitNewAccount(String message) {
        result = Result.FAILED_TO_SUBMIT_NEW_ACCOUNT;
        this.message = message;
    }

    @Override
    public void backEndUnableToSaveNewAccount(String message) {
        result = Result.BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT;
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
