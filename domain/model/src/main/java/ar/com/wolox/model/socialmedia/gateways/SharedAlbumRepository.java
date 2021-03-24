package ar.com.wolox.model.socialmedia.gateways;

import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SharedAlbumRepository {
    Mono<SharedAlbum> save(SharedAlbum sharedAlbum);
    Flux<SharedAlbum> findByOwnerId(String ownerId);
    Flux<SharedAlbum> findByAlbumId(String albumId);
}
