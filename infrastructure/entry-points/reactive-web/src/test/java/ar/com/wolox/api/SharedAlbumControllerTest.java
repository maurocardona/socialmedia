package ar.com.wolox.api;

import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import ar.com.wolox.usecase.shared.GetSharedAlbumUseCase;
import ar.com.wolox.usecase.shared.NewSharedAlbumUseCase;
import ar.com.wolox.usecase.shared.UpdateSharedAlbumUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(SharedAlbumController.class)
public class SharedAlbumControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NewSharedAlbumUseCase newSharedAlbumUseCase;
    @MockBean
    private GetSharedAlbumUseCase getSharedAlbumUseCase;
    @MockBean
    private UpdateSharedAlbumUseCase updateSharedAlbumUseCase;

    @Before
    public void setUp() {
        webTestClient = WebTestClient.bindToController(new SharedAlbumController(
                newSharedAlbumUseCase,
                getSharedAlbumUseCase,
                updateSharedAlbumUseCase)
        ).build();
    }

    @Test
    public void createSharedAlbum_saveNewSharedAlbum_IsOk(){
        when(newSharedAlbumUseCase.shareAlbum(any(SharedAlbum.class)))
                .thenReturn(Mono.just("1"));

        webTestClient.post().uri("/shared-album")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(SharedAlbum.builder().build()), SharedAlbum.class))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getSharedAlbumByOwnerId_GetUserSharedAlbums_IsOk(){
        when(getSharedAlbumUseCase.getSharedAlbumsByOwnerId(anyString()))
                .thenReturn(Flux.fromIterable(Arrays.asList(SharedAlbum.builder().build())));

        webTestClient.get().uri("/shared-album/owner-id/{ownerId}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void updateUserGrants_saveNewGrantForAGuestUser_IsOk(){
        when(updateSharedAlbumUseCase.updateAlbumUserGrant(anyString(), anyString(), anyString()))
                .thenReturn(Mono.empty().then());

        webTestClient.put().uri("/shared-album/update-grant/album-id/{albumId}/user-id/{userId}/grant/{grant}", "1", "2", "write")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted();
    }
}
