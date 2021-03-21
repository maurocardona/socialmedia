package ar.com.wolox.usecase.users;

import ar.com.wolox.model.socialmedia.User;
import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetUsersUseCase {

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<User> searchAllUsers() {
        return socialMediaRepository.searchAllUserFromRemoteSource();
    }
}
