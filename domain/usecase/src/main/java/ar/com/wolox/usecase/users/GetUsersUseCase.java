package ar.com.wolox.usecase.users;

import ar.com.wolox.model.socialmedia.common.ApplicationException;
import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GetUsersUseCase {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private SharedAlbumRepository sharedAlbumRepository;

    public Flux<User> searchAllUsers() {
        return socialMediaRepository.searchAllUsers();
    }

    public Flux<User> searchUsersByAlbumIdAndGrant(String albumId, String grant){
        Optional<String> albumIdOpt = Optional.ofNullable(albumId);
        Optional<String> grantOpt = Optional.ofNullable(grant);

        Function<Optional<String>, Optional<String>> filterFunction = stringOptional -> stringOptional.filter(value -> !value.isEmpty());

        return filterFunction.apply(albumIdOpt)
                .map(albumIdValue -> filterFunction.apply(grantOpt)
                    .map(grantValue -> Mono.zip(
                        socialMediaRepository.searchAllUsers().collectList(),
                        sharedAlbumRepository.findByAlbumId(albumId).collectList(),
                        (allUsers, albums) -> {
                            List<String> idUsersByGrant = albums.stream()
                                    .filter(sharedAlbum -> sharedAlbum.getGuest().getGrant().equalsIgnoreCase(grant))
                                    .map(sharedAlbum -> sharedAlbum.getGuest().getUserId())
                                    .collect(Collectors.toList());
                            return allUsers.stream()
                                    .filter(user -> idUsersByGrant.contains(user.getId()))
                                    .collect(Collectors.toList());
                        })
                        .onErrorMap(throwable -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar medios compartidos, intente de nuevo mas tarde."))
                        .flatMapMany(Flux::fromIterable))
                        .orElseGet(() -> Flux.error(BusinessException.Type.INVALID_GRANT_VALUE.build()))
                )
                .orElseGet(() -> Flux.error(BusinessException.Type.INVALID_ALBUM_VALUE.build()));
    }
}
