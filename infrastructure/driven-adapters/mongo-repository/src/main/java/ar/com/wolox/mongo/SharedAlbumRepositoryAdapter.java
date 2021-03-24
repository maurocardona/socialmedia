package ar.com.wolox.mongo;

import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SharedAlbumRepositoryAdapter implements SharedAlbumRepository {

    @Autowired
    private SharedAlbumDataRepository sharedAlbumDataRepository;

    @Override
    public Mono<SharedAlbum> save(SharedAlbum sharedAlbum) {
        return sharedAlbumDataRepository.save(sharedAlbum);
    }

    @Override
    public Flux<SharedAlbum> findByOwnerId(String ownerId) {
        return sharedAlbumDataRepository.findByOwnerUserId(ownerId);
    }

    @Override
    public Flux<SharedAlbum> findByAlbumId(String albumId) {
        return sharedAlbumDataRepository.findByAlbumId(albumId);
    }
}
