package marabillas.loremar.taskador.network;

/**
 * This class tracks the state of cookie-handling when a request receives a cookie from a
 * shared-hosting site instead of a JSON response from taskador's back-end. This ensures that the
 * cookie is handled only once. And if the host still sends a cookie then the response should be
 * declared to be invalid. A cookie is considered handled when the value of cookie is obtained and
 * saved to be later sent along with the same request.
 */
public interface CookieHandledTracker {
    /**
     * Determine if cookie is already handled or not.
     *
     * @return true if cookie is already handled and false if not.
     */
    boolean isCookieHandled();

    /**
     * Code to execute before cookie-handling including setting cookie as not being handled yet.
     */
    void resetCookieHandledTracking();

    /**
     * Code to execute after handling cookie. Set cookie as already handled.
     */
    void finalizeCookieHandling();
}
