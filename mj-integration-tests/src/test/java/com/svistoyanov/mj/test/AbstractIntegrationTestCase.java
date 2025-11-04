package com.svistoyanov.mj.test;

import com.svistoyanov.mj.IntegrationTest;
import com.svistoyanov.mj.injector.BaseMsgUri;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@IntegrationTest
public class AbstractIntegrationTestCase {
    @BaseMsgUri
    protected String baseMsgUri;  // Will be injected by Junit

    protected void assertSuccess(Response response) {
        assertStatusCode(response, 200);
    }

    protected Response actuatorEndpoint(String path) {
        //        return RestAssured.get("http://localhost:8080/api" + path);
        return RestAssured.get(baseMsgUri + path);
    }

    protected void assertStatusCode(Response response, int status) {
        response
                .then()
                .statusCode(status)
                .log()
                .all();
    }
}
