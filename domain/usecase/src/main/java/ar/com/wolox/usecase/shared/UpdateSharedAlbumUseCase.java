package ar.com.wolox.usecase.shared;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.usecase.util.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UpdateSharedAlbumUseCase {

    private final StringValidator stringValidator = (strings) -> Optional.ofNullable(strings).map(values -> values.stream().anyMatch(value -> value == null || value.isEmpty())).filter(aBoolean -> !aBoolean);

    @Autowired
    private SharedAlbumRepository sharedAlbumRepository;

    public Mono<Void> updateAlbumUserGrant(String albumId, String userId, String grant){

        return stringValidator.validStrings(Arrays.asList(albumId, userId, grant))
                .map(valid -> sharedAlbumRepository.findByAlbumId(albumId)
                        .collectList()
                        .flatMap(sharedAlbums -> sharedAlbums.stream()
                                .filter(sharedAlbum -> sharedAlbum.getGuest().getUserId().equals(userId))
                                .findFirst().map(sharedAlbum -> {
                                    sharedAlbum.getGuest().setGrant(grant);
                                    return Mono.just(sharedAlbum);
                                })
                                .orElseGet(() -> Mono.error(BusinessException.Type.SHARED_ALBUM_NO_GRANT_ALBUM_GUEST.build())))
                        .flatMap(sharedAlbum -> sharedAlbumRepository.save(sharedAlbum))
                        .then())
                .orElseGet(() -> Mono.error(BusinessException.Type.SHARED_ALBUM_UPDATE_GRANT_INVALID_DATA_ERROR.build()));
    }
}
