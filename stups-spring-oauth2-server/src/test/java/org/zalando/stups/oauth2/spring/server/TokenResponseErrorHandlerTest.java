package org.zalando.stups.oauth2.spring.server;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import static org.zalando.stups.oauth2.spring.server.TokenResponseErrorHandler.getDefault;

import java.io.IOException;

import java.util.EnumSet;

import org.junit.Test;

import org.mockito.Mockito;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import org.springframework.web.client.HttpClientErrorException;

public class TokenResponseErrorHandlerTest {

    @Test
    public void create() {
        TokenResponseErrorHandler responseHandler = new TokenResponseErrorHandler(EnumSet.noneOf(HttpStatus.class));
        assertThat(responseHandler).isNotNull();
    }

    @Test
    public void createDefault() {
        TokenResponseErrorHandler responseHandler = getDefault();
        assertThat(responseHandler.getUnhandledStatusSet()).contains(BAD_REQUEST, UNAUTHORIZED, FORBIDDEN);
    }

    @Test
    public void noErrorHandlingOnBadRequest() throws IOException {
        getDefault().handleError(mockResponseWithStatus(BAD_REQUEST));
    }

    @Test
    public void noErrorHandlingOnForbidden() throws IOException {
        getDefault().handleError(mockResponseWithStatus(HttpStatus.FORBIDDEN));
    }

    @Test
    public void noErrorHandlingOnUnauthorized() throws IOException {
        getDefault().handleError(mockResponseWithStatus(UNAUTHORIZED));
    }

    @Test(expected = HttpClientErrorException.class)
    public void expectExceptionForMethodNotAllowed() throws IOException {
        getDefault().handleError(mockResponseWithStatus(HttpStatus.METHOD_NOT_ALLOWED));
    }

    protected ClientHttpResponse mockResponseWithStatus(final HttpStatus status) throws IOException {
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(status);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xml");
        when(response.getHeaders()).thenReturn(headers);
        return response;
    }

}
