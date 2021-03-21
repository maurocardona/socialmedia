package ar.com.wolox.usecase.photos;

import ar.com.wolox.model.socialmedia.Photo;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class GetPhotosUseCase {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<Photo> searchAllPhotos() {
        return socialMediaRepository.searchAllPhotos();
    }

    public Flux<Photo> searchPhotosByUser(String userId){
        return Mono.zip(
                socialMediaRepository.searchAllPhotos().collectList(),
                socialMediaRepository.searchAUserAlbums(userId).collectList(),
                (photos, albums) -> photos.stream()
                        .filter(photo -> albums.stream().anyMatch(album -> album.getId().equals(photo.getAlbumId())))
                        .collect(Collectors.toList()))
                .flatMapMany(Flux::fromIterable);
    }
}
