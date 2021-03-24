package ar.com.wolox.usecase.shared;

import ar.com.wolox.model.socialmedia.gateways.SharedAlbumRepository;
import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetSharedAlbumUseCase {

    @Autowired
    private SharedAlbumRepository sharedAlbumRepository;

    public Flux<SharedAlbum> getSharedAlbumsByOwnerId(String ownerId){
        return sharedAlbumRepository.findByOwnerId(ownerId);
    }
}
