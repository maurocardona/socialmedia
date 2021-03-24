package ar.com.wolox.mongo;

import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SharedAlbumDataRepository extends ReactiveMongoRepository<SharedAlbum, String> {

    Flux<SharedAlbum> findByOwnerUserId(String ownerId);
    Flux<SharedAlbum> findByAlbumId(String albumId);
}
