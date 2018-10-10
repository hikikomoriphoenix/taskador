package marabillas.loremar.taskador.entries;

/**
 * An entry containing a finished task and the date it was finished.
 */
public class TaskDatePair {
    public String finishedTask;
    public String dateFinished;

    public TaskDatePair(String finishedTask, String dateFinished) {
        this.finishedTask = finishedTask;
        this.dateFinished = dateFinished;
    }
}
