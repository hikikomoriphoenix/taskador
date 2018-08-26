package marabillas.loremar.taskador.network.tasks;

import marabillas.loremar.taskador.json.JSON;

public interface ResultListener {
    void onStatusOK(String message, JSON data);

    void onClientError(String message);

    void onServerError(String message);
}
