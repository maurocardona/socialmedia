package ar.com.wolox.api;

import ar.com.wolox.model.socialmedia.media.Album;
import ar.com.wolox.model.socialmedia.media.Comment;
import ar.com.wolox.model.socialmedia.media.Photo;
import ar.com.wolox.model.socialmedia.media.User;
import ar.com.wolox.usecase.albums.GetAlbumsUseCase;
import ar.com.wolox.usecase.comments.GetCommentsUseCase;
import ar.com.wolox.usecase.photos.GetPhotosUseCase;
import ar.com.wolox.usecase.users.GetUsersUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(SocialMediaController.class)
public class SocialMediaControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private GetUsersUseCase getUsersUseCase;
    @MockBean
    private GetAlbumsUseCase getAlbumsUseCase;
    @MockBean
    private GetPhotosUseCase getPhotosUseCase;
    @MockBean
    private GetCommentsUseCase getCommentsUseCase;

    @Before
    public void setUp() {
        webTestClient = WebTestClient.bindToController(new SocialMediaController(
                getUsersUseCase,
                getAlbumsUseCase,
                getPhotosUseCase,
                getCommentsUseCase)
        ).build();
    }

    @Test
    public void getUsers_ShouldGetAllUserFromRemoteService_IsOk(){
        when(getUsersUseCase.searchAllUsers())
                .thenReturn(Flux.fromIterable(Arrays.asList(User.builder()
                        .id(UUID.randomUUID().toString())
                        .build())));

        webTestClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getPhotos_ShouldGetAllPhotosFromRemoteService_IsOk(){
        when(getPhotosUseCase.searchAllPhotos())
                .thenReturn(Flux.fromIterable(Arrays.asList(Photo.builder()
                        .albumId("1")
                        .id("1")
                        .thumbnailUrl("")
                        .title("title")
                        .url("url")
                        .build())));

        webTestClient.get().uri("/photos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getAlbums_ShouldGetAllAlbumsFromRemoteService_IsOk(){
        when(getAlbumsUseCase.searchAllAlbums())
                .thenReturn(Flux.fromIterable(Arrays.asList(Album.builder()
                        .id("1")
                        .title("")
                        .userId("1")
                        .build())));

        webTestClient.get().uri("/albums")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getAlbums_ShouldGetUserAlbums_IsOk(){
        when(getAlbumsUseCase.searchAllAlbums())
                .thenReturn(Flux.fromIterable(Arrays.asList(Album.builder()
                        .id("1")
                        .title("")
                        .userId("1")
                        .build())));

        webTestClient.get().uri("/user/{userId}/albums", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getPhotos_ShouldGetUserPhotos_IsOk(){
        when(getPhotosUseCase.searchPhotosByUser(anyString()))
                .thenReturn(Flux.fromIterable(Arrays.asList(Photo.builder()
                        .albumId("1")
                        .id("1")
                        .thumbnailUrl("")
                        .title("title")
                        .url("url")
                        .build())));

        webTestClient.get().uri("/user/{userId}/photos", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getComments_ShouldGetAllComments_IsOk(){
        when(getCommentsUseCase.getCommentsByCriteria(any(Optional.class), any(Optional.class)))
                .thenReturn(Flux.fromIterable(Arrays.asList(Comment.builder()
                        .body("body")
                        .email("email")
                        .id("1")
                        .name("name")
                        .postId("1")
                        .build())));

        webTestClient.get().uri("/comments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getUsers_ShouldGetUserByAlbumsAndGrant_IsOk(){
        when(getUsersUseCase.searchUsersByAlbumIdAndGrant(anyString(), anyString()))
                .thenReturn(Flux.fromIterable(Arrays.asList(User.builder()
                        .id(UUID.randomUUID().toString())
                        .build())));

        webTestClient.get().uri("/users/album/{albumId}/grant/{grant}", "1", "read")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
