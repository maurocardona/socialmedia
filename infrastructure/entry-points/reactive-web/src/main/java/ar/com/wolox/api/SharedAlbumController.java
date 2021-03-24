package ar.com.wolox.api;

import ar.com.wolox.model.socialmedia.shared.SharedAlbum;
import ar.com.wolox.usecase.shared.GetSharedAlbumUseCase;
import ar.com.wolox.usecase.shared.NewSharedAlbumUseCase;
import ar.com.wolox.usecase.shared.UpdateSharedAlbumUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/shared-album", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedAlbumController {

    private NewSharedAlbumUseCase newSharedAlbumUseCase;
    private GetSharedAlbumUseCase getSharedAlbumUseCase;
    private UpdateSharedAlbumUseCase updateSharedAlbumUseCase;

    @Autowired
    public SharedAlbumController(NewSharedAlbumUseCase newSharedAlbumUseCase, GetSharedAlbumUseCase getSharedAlbumUseCase, UpdateSharedAlbumUseCase updateSharedAlbumUseCase) {
        this.newSharedAlbumUseCase = newSharedAlbumUseCase;
        this.getSharedAlbumUseCase = getSharedAlbumUseCase;
        this.updateSharedAlbumUseCase = updateSharedAlbumUseCase;
    }

    @PostMapping
    public Mono<String> createSharedAlbum(@RequestBody SharedAlbum sharedAlbum){
        return newSharedAlbumUseCase.shareAlbum(sharedAlbum);
    }

    @GetMapping(value = "/owner-id/{ownerId}")
    public Flux<SharedAlbum> getSharedAlbumByOwnerId(@PathVariable(name = "ownerId") String ownerId){
        return getSharedAlbumUseCase.getSharedAlbumsByOwnerId(ownerId);
    }

    @PutMapping(value = "/update-grant/album-id/{albumId}/user-id/{userId}/grant/{grant}")
    public ResponseEntity<Mono<String>> updateUserGrants(@PathVariable(name = "albumId") String albumId, @PathVariable(name = "userId") String userId, @PathVariable(name = "grant") String grant){
        return new ResponseEntity<>(updateSharedAlbumUseCase.updateAlbumUserGrant(albumId, userId, grant).map(unused -> "Permiso modificado con exito"), HttpStatus.ACCEPTED);
    }
}
