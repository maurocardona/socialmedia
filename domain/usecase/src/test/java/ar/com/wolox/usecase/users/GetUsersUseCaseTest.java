package ar.com.wolox.usecase.users;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.User;
import ar.com.wolox.model.socialmedia.shared.Guest;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
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
public class GetUsersUseCaseTest {
    @Mock
    private SocialMediaRepository socialMediaRepository;

    @Mock
    private SharedAlbumRepository sharedAlbumRepository;

    @InjectMocks
    private GetUsersUseCase getUsersUseCase;

    @Test
    public void searchUsersByAlbumIdAndGrant_getUserPhotos_isOk(){
        when(socialMediaRepository.searchAllUsers()).thenReturn(Flux.fromIterable(Arrays.asList(
                User.builder().id("1").build(),
                User.builder().id("2").build(),
                User.builder().id("3").build()
        )));
        when(sharedAlbumRepository.findByAlbumId(anyString())).thenReturn(Flux.fromIterable(Arrays.asList(
                SharedAlbum.builder().albumId("1").id("1111").ownerUserId("1").guest(Guest.builder().userId("2").grant("write").build()).build(),
                SharedAlbum.builder().albumId("2").id("1234").ownerUserId("1").guest(Guest.builder().userId("2").grant("read").build()).build(),
                SharedAlbum.builder().albumId("1").id("2345").ownerUserId("1").guest(Guest.builder().userId("3").grant("write").build()).build()
        )));

        Flux<User> result = getUsersUseCase.searchUsersByAlbumIdAndGrant("1", "write");

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void searchUsersByAlbumIdAndGrant_getUserPhotosWhenAlbumIdIsNull_returnError(){

        Flux<User> result = getUsersUseCase.searchUsersByAlbumIdAndGrant(null, "write");

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void searchUsersByAlbumIdAndGrant_getUserPhotosWhenGrantValueIsNull_returnError(){

        Flux<User> result = getUsersUseCase.searchUsersByAlbumIdAndGrant("1", null);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }
}
