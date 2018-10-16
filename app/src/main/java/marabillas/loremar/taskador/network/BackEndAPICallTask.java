package marabillas.loremar.taskador.network;

import android.support.annotation.NonNull;

import java.util.concurrent.FutureTask;

import marabillas.loremar.taskador.network.tasks.RunnableTask;

/**
 * Class for executing {@link RunnableTask}'s. As a {@link FutureTask}, it executes the task on a separate
 * working thread and allows cancelling of the thread midway through the operation. Call
 * {@link FutureTask#get} method to acquire results of its process. The {@link FutureTask#get}
 * method is a  blocking process which makes the calling thread of this method wait while another
 * thread tries to complete the task.
 */
public class BackEndAPICallTask extends FutureTask<BackEndResponse> {
    private RunnableTask runnableTask;

    /**
     * Creates a {@link BackEndAPICallTask} instance.
     *
     * @param runnableTask the {@link RunnableTask} to execute
     * @param result       a {@link BackEndResponse} object to store the contents of response sent by the
     *                     back-end server
     */
    public BackEndAPICallTask(@NonNull RunnableTask runnableTask, BackEndResponse result) {
        super(runnableTask, result);
        this.runnableTask = runnableTask;
        runnableTask.trackForResult(result);
    }

    public RunnableTask getRunnableTask() {
        return runnableTask;
    }
}
