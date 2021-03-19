package ar.com.wolox.consumer;

import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SocialMediaService extends RestConsumer implements SocialMediaRepository {

    public SocialMediaService(String baseURL) {
        super(baseURL);
    }

    @Override
    public Flux<String> searchAllUserFromRemoteSource() {
        return null;
    }

    @Override
    public void configureHeaders(HttpHeaders httpHeaders) {

    }
}
