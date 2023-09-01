package org.example.clients;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.example.utils.JsonUtils.castObjectToJsonString;

@Slf4j
@Component
@Profile("component-test | api-test")
public class RestClient {

    private static final ThreadLocal<RestClient> instance = new ThreadLocal<>();
    private RequestSpecification requestSpecification;

    @Value("${default.jwtToken}:invalidToken")
    protected String jwtToken;
    private static String pingToken;

    private static final String LOG_MESSAGE_ALL_PARAMS = "[{}] request was sent for URI: {}\nWith path params:\n{}\nAnd url params:\n{}";
    private static final String LOG_MESSAGE_WITHOUT_PARAMS = "[{}] request was sent for URI: {}";
    private static final String LOG_MESSAGE_REQUEST_BODY = "[{}] request was sent for URI: {}\nWith path params:\n{}\nAnd request body:\n{}";

    private RestClient() {
        initializeSpecifications();
    }

    public static RestClient getInstance() {
        if (instance.get() == null) {
            instance.set(new RestClient());
        }

        return instance.get();
    }

    public Response sendRequestWithParameters(Method method, String url, Map<String, ?> pathParams, Map<String, ?> urlParams) {
        requestSpecification
                .pathParams(pathParams)
                .queryParams(urlParams);

        log.info(LOG_MESSAGE_ALL_PARAMS, method.toString(), url, pathParams, urlParams);
        return sendRequestForHttpMethod(method, url, requestSpecification);
    }

    public Response sendRequestWithBody(Method method, String url, Map<String, ?> urlParams, Map<String, ?> pathParams, Object requestBody) {
        requestSpecification.pathParams(pathParams);
        return sendRequestWithBody(method, url, urlParams, requestBody);
    }

    public Response sendRequestWithBody(Method method, String url, Map<String, ?> urlParams, Object requestBody) {
        if (MapUtils.isNotEmpty(urlParams)) {
            requestSpecification.queryParams(urlParams);
        }
        if (Objects.nonNull(requestBody)) {
            requestSpecification.body(requestBody);
        }

        log.info(LOG_MESSAGE_REQUEST_BODY, method.toString(), url, urlParams, castObjectToJsonString(requestBody));
        return sendRequestForHttpMethod(method, url, requestSpecification);
    }

    public Response sendRequestWithoutParams(Method httpMethod, String url) {
        log.info(LOG_MESSAGE_WITHOUT_PARAMS, httpMethod.toString(), url);
        return sendRequestForHttpMethod(httpMethod, url, requestSpecification);
    }

    public Response sendRequestForHttpMethod(Method httpMethod, String url, RequestSpecification requestSpecification) {
        return requestSpecification.request(httpMethod, url);
    }

    public RestClient initializeSpecifications() {
        requestSpecification = given();
        return this;
    }
}

