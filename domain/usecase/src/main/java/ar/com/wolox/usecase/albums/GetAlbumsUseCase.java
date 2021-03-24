package ar.com.wolox.usecase.albums;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
public class GetAlbumsUseCase {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<Album> searchAllAlbums() {
        return socialMediaRepository.searchAllAlbumsFromRemoteSource();
    }

    public Flux<Album> searchAlbumsByUserId(String userId){
        return Optional.ofNullable(userId)
                .filter(s -> !s.isEmpty())
                .map(userIdParam -> socialMediaRepository.searchAUserAlbums(userId))
                .orElseGet(() -> Flux.error(BusinessException.Type.INVALID_USER_ID_VALUE.build()));
    }
}
