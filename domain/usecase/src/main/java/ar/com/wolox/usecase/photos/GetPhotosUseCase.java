package ar.com.wolox.usecase.photos;

import ar.com.wolox.model.socialmedia.common.BusinessException;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetPhotosUseCase {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<Photo> searchAllPhotos() {
        return socialMediaRepository.searchAllPhotos();
    }

    public Flux<Photo> searchPhotosByUser(String userId){
        return Optional.ofNullable(userId)
                .filter(userIdeValue -> !userIdeValue.isEmpty())
                .map(userIdValue -> Mono.zip(
                        socialMediaRepository.searchAllPhotos().collectList(),
                        socialMediaRepository.searchAUserAlbums(userIdValue).collectList(),
                        (photos, albums) -> photos.stream()
                                    .filter(photo -> albums.stream()
                                            .anyMatch(album -> album.getId().equalsIgnoreCase(photo.getAlbumId())))
                                    .collect(Collectors.toList()))
                        .flatMapMany(Flux::fromIterable))
                .orElseGet(() -> Flux.error(BusinessException.Type.INVALID_USER_ID_VALUE.build()));
    }
}
