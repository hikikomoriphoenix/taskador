package marabillas.loremar.taskador.background;

public interface BackgroundTasker<A extends BackgroundServiceClient> {
    void bindClient(A client);

    A getClient();
}
