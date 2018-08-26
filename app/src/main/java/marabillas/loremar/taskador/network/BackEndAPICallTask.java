package marabillas.loremar.taskador.network;

import android.support.annotation.NonNull;

import java.util.concurrent.FutureTask;

import marabillas.loremar.taskador.network.tasks.RunnableTask;

public class BackEndAPICallTask extends FutureTask<BackEndResponse> {
    private RunnableTask runnableTask;

    public BackEndAPICallTask(@NonNull RunnableTask runnableTask, BackEndResponse result) {
        super(runnableTask, result);
        this.runnableTask = runnableTask;
        runnableTask.trackForResult(result);
    }

    public RunnableTask getRunnableTask() {
        return runnableTask;
    }
}
