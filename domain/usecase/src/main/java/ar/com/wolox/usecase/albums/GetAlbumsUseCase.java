package ar.com.wolox.usecase.albums;

import ar.com.wolox.model.socialmedia.Album;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetAlbumsUseCase {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<Album> searchAllAlbums() {
        return socialMediaRepository.searchAllAlbumsFromRemoteSource();
    }

    public Flux<Album> searchAlbumsByUserId(String userId){
        return socialMediaRepository.searchAUserAlbums(userId);
    }
}
