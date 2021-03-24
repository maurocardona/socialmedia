package ar.com.wolox.consumer.base;

import ar.com.wolox.model.socialmedia.media.Post;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestConsumerTest {

    private class RestConsumerExample extends RestConsumer{

        public RestConsumerExample(String baseURL) {
            super(baseURL);
        }
    }
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private RestConsumerExample restConsumerExample;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(requestBodyUriMock.uri(anyString())).thenReturn(requestBodyMock);
        when(webClientMock.method(HttpMethod.GET)).thenReturn(requestBodyUriMock);
        when(requestBodyMock.headers(any())).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(), any())).thenReturn(responseMock);

        restConsumerExample = new RestConsumerExample("");
        restConsumerExample.setWebClient(webClientMock);
    }

    @Test
    public void getRequest_SendBasicRequestWithoutParameters_FluxOfClass(){
        when(responseMock.bodyToFlux(Post.class)).thenReturn(Flux.just(Post.builder().id("1").build()));
        Flux<Post> result = restConsumerExample.getRequest("", Post.class);

        StepVerifier.create(result)
                .assertNext(post -> assertThat(post).isInstanceOf(Post.class))
                .verifyComplete();
    }

    @Test
    public void getRequest_SendRequestWithQueryParams_FluxOfClass() throws URISyntaxException {
        when(responseMock.bodyToFlux(Post.class)).thenReturn(Flux.just(Post.builder().id("1").build()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("postId", "1");

        Flux<Post> result = restConsumerExample.getRequest("/post", Post.class, queryParams);

        verify(requestBodyUriMock).uri(captor.capture());

        StepVerifier.create(result)
                .assertNext(post -> assertThat(post.getId()).isEqualTo("1"))
                .verifyComplete();

        Map<String, String> params = new URIBuilder(new URI("http://testing.com" + captor.getValue()))
                .getQueryParams()
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

        assertEquals("1", params.get("postId"));
    }

    @Test
    public void getRequest_SendRequestWithPathParams_FluxOfClass() throws URISyntaxException {
        when(responseMock.bodyToFlux(Post.class)).thenReturn(Flux.just(Post.builder().id("1").build()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Flux<Post> result = restConsumerExample.getRequest("/post/%s", Post.class, "1");

        verify(requestBodyUriMock).uri(captor.capture());

        StepVerifier.create(result)
                .assertNext(post -> assertThat(post.getId()).isEqualTo("1"))
                .verifyComplete();

        List<String> segments = new URIBuilder(new URI("http://testing.com" + captor.getValue()))
                .getPathSegments();

        assertEquals(2, segments.size());
        assertEquals("1", segments.get(1));
    }

    @Test
    public void getRequest_SendRequestWithIncompletePathParams_handleError() throws URISyntaxException {
        when(responseMock.bodyToFlux(Post.class)).thenReturn(Flux.just(Post.builder().id("1").build()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> restConsumerExample.getRequest("/post/%s/comments/%s", Post.class, "1"));

        assertEquals("Cantidad de parametros incorrecta", exception.getMessage());

    }

}
