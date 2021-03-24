package ar.com.wolox.usecase.albums;

import ar.com.wolox.model.socialmedia.common.ApplicationException;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Album;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAlbumsUseCaseTest {

    @Mock
    private SocialMediaRepository socialMediaRepository;

    @InjectMocks
    private GetAlbumsUseCase getAlbumsUseCase;

    @Test
    public void searchAlbumsByUserId_withAValidUserId_IsOk(){

        when(socialMediaRepository.searchAUserAlbums(anyString())).thenReturn(Flux.just(Album.builder().build()));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Flux<Album> result = getAlbumsUseCase.searchAlbumsByUserId("1");

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(socialMediaRepository).searchAUserAlbums(captor.capture());
        assertThat(captor.getValue()).isEqualTo("1");
    }

    @Test
    public void searchAlbumsByUserId_withUserIdIsNull_returnError(){

        Flux<Album> result = getAlbumsUseCase.searchAlbumsByUserId(null);

        StepVerifier.create(result)
                .expectError(ApplicationException.class)
                .verify();
    }

    @Test
    public void searchAlbumsByUserId_withUserIdIsEmpty_returnError(){

        Flux<Album> result = getAlbumsUseCase.searchAlbumsByUserId("");

        StepVerifier.create(result)
                .expectError(ApplicationException.class)
                .verify();
    }
}
