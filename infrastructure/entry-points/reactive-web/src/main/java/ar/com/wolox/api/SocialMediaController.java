package ar.com.wolox.api;

import ar.com.wolox.model.socialmedia.Album;
import ar.com.wolox.model.socialmedia.Comment;
import ar.com.wolox.model.socialmedia.Photo;
import ar.com.wolox.model.socialmedia.User;
import ar.com.wolox.usecase.albums.GetAlbumsUseCase;
import ar.com.wolox.usecase.comments.GetCommentsUseCase;
import ar.com.wolox.usecase.users.GetUsersUseCase;
import ar.com.wolox.usecase.photos.GetPhotosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private GetUsersUseCase getUsersUseCase;
    @Autowired
    private GetAlbumsUseCase getAlbumsUseCase;
    @Autowired
    private GetPhotosUseCase getPhotosUseCase;
    @Autowired
    private GetCommentsUseCase getCommentsUseCase;

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

}
