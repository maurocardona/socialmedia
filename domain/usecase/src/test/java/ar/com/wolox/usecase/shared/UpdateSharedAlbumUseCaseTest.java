package ar.com.wolox.usecase.shared;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.shared.Guest;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSharedAlbumUseCaseTest {

    @Mock
    private SharedAlbumRepository sharedAlbumRepository;

    @InjectMocks
    private UpdateSharedAlbumUseCase updateSharedAlbumUseCase;

    @Test
    public void updateAlbumUserGrant_changeGrantToUser_IsOk(){
        when(sharedAlbumRepository.findByAlbumId(anyString())).thenReturn(Flux.just(SharedAlbum.builder().albumId("1").guest(Guest.builder().userId("2").grant("read").build()).build()));
        when(sharedAlbumRepository.save(any(SharedAlbum.class))).thenReturn(Mono.just(SharedAlbum.builder().id(UUID.randomUUID().toString()).build()));

        ArgumentCaptor<SharedAlbum> captor = ArgumentCaptor.forClass(SharedAlbum.class);

        Mono<Void> result = updateSharedAlbumUseCase.updateAlbumUserGrant("1", "2", "write");

        StepVerifier.create(result)
                .verifyComplete();

        verify(sharedAlbumRepository).save(captor.capture());

        SharedAlbum saved = captor.getValue();
        assertEquals("write", saved.getGuest().getGrant());
    }

    @Test
    public void updateAlbumUserGrant_NoSharedAlbumWithUser_returnError(){
        when(sharedAlbumRepository.findByAlbumId(anyString())).thenReturn(Flux.just(SharedAlbum.builder().albumId("1").guest(Guest.builder().userId("2").grant("read").build()).build()));

        Mono<Void> result = updateSharedAlbumUseCase.updateAlbumUserGrant("1", "3", "write");

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void updateAlbumUserGrant_changeGrantToUser_returnError(){

        Mono<Void> result = updateSharedAlbumUseCase.updateAlbumUserGrant("", "2", "write");

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }
}
