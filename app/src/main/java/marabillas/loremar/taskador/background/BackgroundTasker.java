package marabillas.loremar.taskador.background;

/**
 * Represents an object that will perform background tasks for a client that requests it.
 *
 * @param <A> A class implementing {@link BackgroundServiceClient} or its extending interface,
 *           for the client object which will request background tasks.
 */
public interface BackgroundTasker<A extends BackgroundServiceClient> {
    /**
     * Bind a {@link BackgroundServiceClient} object to this object. BackgroundTasker will
     * perform background tasks for this client.
     *
     * @param client an instance of
     * {@link BackgroundServiceClient}.
     */
    void bindClient(A client);

    A getClient();
}
