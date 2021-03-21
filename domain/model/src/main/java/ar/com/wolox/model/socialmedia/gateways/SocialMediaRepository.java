package ar.com.wolox.model.socialmedia.gateways;

import ar.com.wolox.model.socialmedia.Album;
import ar.com.wolox.model.socialmedia.Photo;
import ar.com.wolox.model.socialmedia.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SocialMediaRepository {
    Flux<User> searchAllUserFromRemoteSource();
    Flux<Album> searchAllAlbumsFromRemoteSource();
    Flux<Album> searchAUserAlbums(String userId);
    Flux<Photo> searchAllPhotos();
}
