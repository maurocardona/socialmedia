package ar.com.wolox.usecase.shared;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
public class NewSharedAlbumUseCase {
    @Autowired
    private SharedAlbumRepository sharedAlbumRepository;

    public Mono<String> shareAlbum(SharedAlbum sharedAlbum){
       return Optional.ofNullable(sharedAlbum)
                .map(sharedAlbum1 -> {
                    if (sharedAlbum1.getOwnerUserId().equalsIgnoreCase(sharedAlbum.getGuest().getUserId())) {
                        return Mono.<String>error(BusinessException.Type.SHARED_ALBUM_USER_GUEST_ERROR.build());
                    } else {
                        sharedAlbum1.setId(UUID.randomUUID().toString());
                        return sharedAlbumRepository.save(sharedAlbum).map(SharedAlbum::getId);
                    }
                }).orElseGet(() -> Mono.error(BusinessException.Type.SHARED_ALBUM_INVALID_DATA.build()));
    }

}
