package ar.com.wolox.model.socialmedia.gateways;

import reactor.core.publisher.Flux;

public interface SocialMediaRepository {
    Flux<String> searchAllUserFromRemoteSource();
}
