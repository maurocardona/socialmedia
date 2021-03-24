package ar.com.wolox.consumer;

import ar.com.wolox.consumer.base.RestConsumer;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.*;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

@Service
@Setter
public class SocialMediaService extends RestConsumer implements SocialMediaRepository {

    private static final String USER_ID_QUERY_PARAM = "userId";
    @Value("${remote.service.users-uri}")
    private String usersUri;
    @Value("${remote.service.photos-uri}")
    private String photosUri;
    @Value("${remote.service.albums-uri}")
    private String albumsUri;
    @Value("${remote.service.user-albums-uri}")
    private String userAlbumsUri;
    @Value("${remote.service.comments-uri}")
    private String commentsUri;
    @Value("${remote.service.posts-uri}")
    private String postsUri;

    public SocialMediaService(@Value("${remote.service.url}") String baseURL) {
        super(baseURL);
    }

    @Override
    public Flux<User> searchAllUsers() {
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

    @Override
    public Flux<Comment> searchAllComments() {
        return getRequest(commentsUri, Comment.class);
    }

    @Override
    public Flux<Post> searchAllPost() {
        return getRequest(postsUri, Post.class);
    }

    @Override
    public Flux<Post> searchPostByUserId(String userId) {
        return getRequest(postsUri, Post.class, buildRiskSearchQueryParams(userId));
    }

    public static MultiValueMap<String, String> buildRiskSearchQueryParams(String userId){
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(USER_ID_QUERY_PARAM, userId);
        return queryParams;
    }
}
