package marabillas.loremar.taskador.network;

public interface CookieHandledTracker {
    boolean isCookieHandled();

    void resetCookieHandledTracking();

    void finalizeCookieHandling();
}
