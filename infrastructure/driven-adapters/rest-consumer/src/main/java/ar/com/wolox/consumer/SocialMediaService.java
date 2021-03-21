package ar.com.wolox.consumer;

import ar.com.wolox.consumer.base.RestConsumer;
import ar.com.wolox.model.socialmedia.Album;
import ar.com.wolox.model.socialmedia.Photo;
import ar.com.wolox.model.socialmedia.User;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SocialMediaService extends RestConsumer implements SocialMediaRepository {

    @Value("${remote.service.users-uri}")
    private String usersUri;
    @Value("${remote.service.photos-uri}")
    private String photosUri;
    @Value("${remote.service.albums-uri}")
    private String albumsUri;
    @Value("${remote.service.user-albums-uri}")
    private String userAlbumsUri;

    public SocialMediaService(@Value("${remote.service.url}") String baseURL) {
        super(baseURL);
    }

    @Override
    public Flux<User> searchAllUserFromRemoteSource() {
        return getRequest(usersUri, User.class);
    }

    @Override
    public Flux<Album> searchAllAlbumsFromRemoteSource() {
        return getRequest(albumsUri, Album.class);
    }

    @Override
    public Flux<Album> searchAUserAlbums(String userId) {
        return getRequest(userAlbumsUri, Album.class, userId);
    }

    @Override
    public Flux<Photo> searchAllPhotos() {
        return getRequest(photosUri, Photo.class);
    }

}
