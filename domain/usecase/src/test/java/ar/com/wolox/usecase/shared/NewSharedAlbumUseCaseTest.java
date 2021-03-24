package ar.com.wolox.usecase.shared;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.shared.Guest;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewSharedAlbumUseCaseTest {

    @Mock
    private SharedAlbumRepository sharedAlbumRepository;

    @InjectMocks
    private NewSharedAlbumUseCase newSharedAlbumUseCase;

    @Test
    public void shareAlbum_CreateNewSharedAlbum_IsOk(){
        when(sharedAlbumRepository.save(any(SharedAlbum.class))).thenReturn(Mono.just(SharedAlbum.builder().id(UUID.randomUUID().toString()).build()));

        Mono<String> result = newSharedAlbumUseCase.shareAlbum(SharedAlbum.builder().ownerUserId("1234").guest(Guest.builder().userId("2345").build()).build());

        StepVerifier.create(result)
                .assertNext(s -> assertThat(s).isNotEmpty())
                .verifyComplete();
    }

    @Test
    public void shareAlbum_AlbumOwnerIdSameGuestUserId_ReturnError(){
        Mono<String> result = newSharedAlbumUseCase.shareAlbum(SharedAlbum.builder().ownerUserId("1").guest(Guest.builder().userId("1").build()).build());

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void shareAlbum_NoSharedAlbumData_ReturnError(){

        Mono<String> result = newSharedAlbumUseCase.shareAlbum(null);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }
}
