package ar.com.wolox.usecase.users;

import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetAllUsersUseCase {
    private final SocialMediaRepository socialMediaRepository;
    public Flux<String> searchAllUsers() {
        return socialMediaRepository.searchAllUserFromRemoteSource();
    }
}
