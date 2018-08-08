package marabillas.loremar.taskador.network;

import marabillas.loremar.taskador.json.JSON;

interface ResultListener {
    void onStatusOK(String message, JSON data);

    void onClientError(String message);

    void onServerError(String message);
}
