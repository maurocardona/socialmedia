package ar.com.wolox.model.socialmedia.gateways;

import ar.com.wolox.model.socialmedia.*;
import reactor.core.publisher.Flux;

public interface SocialMediaRepository {
    Flux<User> searchAllUserFromRemoteSource();
    Flux<Album> searchAllAlbumsFromRemoteSource();
    Flux<Album> searchAUserAlbums(String userId);
    Flux<Photo> searchAllPhotos();
    Flux<Comment> searchAllComments();
    Flux<Post> searchAllPost();
    Flux<Post> searchPostByUserId(String userId);
}
