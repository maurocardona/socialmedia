package ar.com.wolox.consumer;

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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SocialMediaServiceTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private SocialMediaService socialMediaService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(requestBodyUriMock.uri(anyString())).thenReturn(requestBodyMock);
        when(webClientMock.method(HttpMethod.GET)).thenReturn(requestBodyUriMock);
        when(requestBodyMock.headers(any())).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(), any())).thenReturn(responseMock);

        socialMediaService = new SocialMediaService("");
        socialMediaService.setAlbumsUri("");
        socialMediaService.setCommentsUri("");
        socialMediaService.setPhotosUri("");
        socialMediaService.setPostsUri("");
        socialMediaService.setUsersUri("");
        socialMediaService.setUserAlbumsUri("%s");

        socialMediaService.setWebClient(webClientMock);
    }

    @Test
    public void searchPostByUserId() throws URISyntaxException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(responseMock.bodyToFlux(Post.class)).thenReturn(Flux.just(Post.builder().id("1").build()));

        Flux<Post> result = socialMediaService.searchPostByUserId("1");

        verify(requestBodyUriMock).uri(captor.capture());

        StepVerifier.create(result)
                .assertNext(post -> assertThat(post.getId()).isEqualTo("1"))
                .verifyComplete();

        Map<String, String> params = new URIBuilder(new URI("http://testing.com" + captor.getValue()))
                .getQueryParams()
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

        assertEquals("1", params.get("userId"));
    }
}
