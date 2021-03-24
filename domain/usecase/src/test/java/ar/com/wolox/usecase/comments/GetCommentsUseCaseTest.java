package ar.com.wolox.usecase.comments;

import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Comment;
import ar.com.wolox.model.socialmedia.media.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetCommentsUseCaseTest {
    @Mock
    private SocialMediaRepository socialMediaRepository;

    @InjectMocks
    private GetCommentsUseCase getCommentsUseCase;

    @Test
    public void getCommentsByCriteria_getAllComment_isOk(){
        when(socialMediaRepository.searchAllComments()).thenReturn(Flux.just(
                Comment.builder().id("1").build(),
                Comment.builder().id("2").build()
        ));

        Flux<Comment> result = getCommentsUseCase.getCommentsByCriteria(Optional.empty(), Optional.empty());

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getCommentsByCriteria_getCommentsByName_isOk(){
        when(socialMediaRepository.searchAllComments()).thenReturn(Flux.just(
                Comment.builder().id("1").name("test").build(),
                Comment.builder().id("2").name("other").build()
        ));

        Flux<Comment> result = getCommentsUseCase.getCommentsByCriteria(Optional.of("test"), Optional.empty());

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getCommentsByCriteria_getCommentsByUserId_isOk(){
        when(socialMediaRepository.searchAllComments()).thenReturn(Flux.just(
                Comment.builder().id("1").name("test").postId("1").build(),
                Comment.builder().id("2").name("other").postId("2").build()
        ));

        when(socialMediaRepository.searchPostByUserId(anyString())).thenReturn(Flux.just(
                Post.builder()
                        .id("1")
                        .userId("2")
                        .body("body")
                        .title("title")
                        .build()));

        Flux<Comment> result = getCommentsUseCase.getCommentsByCriteria(Optional.empty(), Optional.of("2"));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
