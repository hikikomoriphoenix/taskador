package marabillas.loremar.taskador.network;

import android.app.Activity;

public class SignupTestActivity extends Activity implements SignupTask.ResultHandler {
    private Result result;
    private Object syncKey;

    public enum Result {
        NEW_ACCOUNT_SAVED,
        FAILED_TO_SUBMIT_NEW_ACCOUNT,
        BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT
    }

    @Override
    public void newAccountSaved(String message) {
        result = Result.NEW_ACCOUNT_SAVED;
        syncKey.notify();
    }

    @Override
    public void failedToSubmitNewAccount(String message) {
        result = Result.FAILED_TO_SUBMIT_NEW_ACCOUNT;
        syncKey.notify();
    }

    @Override
    public void backEndUnableToSaveNewAccount(String message) {
        result = Result.BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT;
        syncKey.notify();
    }

    public void setSyncKey(Object syncKey) {
        this.syncKey = syncKey;
        syncKey.notify();
    }

    public Result getResult() {
        return result;
    }
}
