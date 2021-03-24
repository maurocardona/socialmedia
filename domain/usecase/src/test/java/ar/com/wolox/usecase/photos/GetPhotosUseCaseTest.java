package ar.com.wolox.usecase.photos;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Album;
import ar.com.wolox.model.socialmedia.media.Photo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetPhotosUseCaseTest {
    @Mock
    private SocialMediaRepository socialMediaRepository;

    @InjectMocks
    private GetPhotosUseCase getPhotosUseCase;

    @Test
    public void searchPhotosByUser_getUserPhotos_isOk(){
        when(socialMediaRepository.searchAllPhotos()).thenReturn(Flux.fromIterable(Arrays.asList(
                Photo.builder().id("1").albumId("1").thumbnailUrl("").url("").title("").build(),
                Photo.builder().id("2").albumId("1").thumbnailUrl("").url("").title("").build(),
                Photo.builder().id("3").albumId("2").thumbnailUrl("").url("").title("").build(),
                Photo.builder().id("4").albumId("2").thumbnailUrl("").url("").title("").build()
        )));
        
        when(socialMediaRepository.searchAUserAlbums(anyString())).thenReturn(Flux.fromIterable(Arrays.asList(
           Album.builder().id("1").userId("1").title("").build(),
           Album.builder().id("2").userId("1").title("").build(),
           Album.builder().id("3").userId("1").title("").build()
        )));

        Flux<Photo> result = getPhotosUseCase.searchPhotosByUser("1");

        StepVerifier.create(result)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void searchPhotosByUser_getUserPhotosWithUserIdNull_returnError(){

        Flux<Photo> result = getPhotosUseCase.searchPhotosByUser(null);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void searchPhotosByUser_getUserPhotosWithUserIdEmpty_returnError(){

        Flux<Photo> result = getPhotosUseCase.searchPhotosByUser("");

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

}
