package com.api.context;

import io.restassured.response.*;;

public class ApiScenarioContext {

    private static final ThreadLocal<Response> LAST_RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<String> LAST_ENDPOINT = new ThreadLocal<>();

    private ApiScenarioContext() {}

    public static void setLastResponse(Response response) {
        LAST_RESPONSE.set(response);
    }

    public static Response getLastResponse() {
        Response response = LAST_RESPONSE.get();
        if (response == null) {
            throw new IllegalStateException("No API response found in context");
        }
        return response;
    }

    // public static void setLastEnd

}
