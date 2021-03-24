package ar.com.wolox.api;

import ar.com.wolox.model.socialmedia.media.Album;
import ar.com.wolox.model.socialmedia.media.Comment;
import ar.com.wolox.model.socialmedia.media.Photo;
import ar.com.wolox.model.socialmedia.media.User;
import ar.com.wolox.usecase.albums.GetAlbumsUseCase;
import ar.com.wolox.usecase.comments.GetCommentsUseCase;
import ar.com.wolox.usecase.photos.GetPhotosUseCase;
import ar.com.wolox.usecase.users.GetUsersUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
public class SocialMediaController {

    private GetUsersUseCase getUsersUseCase;
    private GetAlbumsUseCase getAlbumsUseCase;
    private GetPhotosUseCase getPhotosUseCase;
    private GetCommentsUseCase getCommentsUseCase;
    @Autowired
    public SocialMediaController(GetUsersUseCase getUsersUseCase, GetAlbumsUseCase getAlbumsUseCase, GetPhotosUseCase getPhotosUseCase, GetCommentsUseCase getCommentsUseCase) {
        this.getUsersUseCase = getUsersUseCase;
        this.getAlbumsUseCase = getAlbumsUseCase;
        this.getPhotosUseCase = getPhotosUseCase;
        this.getCommentsUseCase = getCommentsUseCase;
    }

    @GetMapping("/users")
    public Flux<User> getUsers(){
        return getUsersUseCase.searchAllUsers();
    }

    @GetMapping("/photos")
    public Flux<Photo> getPhotos(){
        return getPhotosUseCase.searchAllPhotos();
    }

    @GetMapping("/albums")
    public Flux<Album> getAlbums(){
        return getAlbumsUseCase.searchAllAlbums();
    }

    @GetMapping("/user/{userId}/albums")
    public Flux<Album> getAlbums(@PathVariable(name = "userId") String userId){
        return getAlbumsUseCase.searchAlbumsByUserId(userId);
    }

    @GetMapping("/user/{userId}/photos")
    public Flux<Photo> getPhotos(@PathVariable(name = "userId") String userId){
        return getPhotosUseCase.searchPhotosByUser(userId);
    }

    @GetMapping("/comments")
    public Flux<Comment> getComments(@RequestParam Optional<String> name, @RequestParam Optional<String> userId){
        return getCommentsUseCase.getCommentsByCriteria(name, userId);
    }

    @GetMapping("/users/album/{albumId}/grant/{grant}")
    public Flux<User> getUsersByAlbumAndGrant(@PathVariable(name = "albumId") String albumId, @PathVariable(name = "grant") String grant){
        return getUsersUseCase.searchUsersByAlbumIdAndGrant(albumId, grant);
    }

}
