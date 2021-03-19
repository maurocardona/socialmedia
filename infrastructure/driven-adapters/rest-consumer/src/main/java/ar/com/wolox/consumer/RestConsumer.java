package ar.com.wolox.consumer;

import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

public abstract class RestConsumer {

    private Logger logger = (Logger) LogManager.getLogger(this.getClass());

    private WebClient webClient;


    public RestConsumer(String baseURL) {
        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl(baseURL)
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    public abstract void configureHeaders(HttpHeaders httpHeaders);

    public <B, R> Mono<R> postRequest(String uri, B body, Class<R> responseClass) {
        return executeRequest(webClient.method(HttpMethod.POST)
                .uri(uri)
                .body(BodyInserters.fromPublisher(Mono.just(body), (Class) body.getClass())), responseClass, Optional.of(body));
    }

    public <R> Mono<R> getRequest(String uri, Class<R> responseClass) {
        return executeRequest(webClient.method(HttpMethod.GET)
                .uri(uri), responseClass, Optional.empty());
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Sending: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponseStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Received Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    private <R, B> Mono<R> executeRequest(WebClient.RequestHeadersSpec requestHeadersSpec, Class<R> responseClass, Optional<B> body) {
        return requestHeadersSpec.headers(httpHeaders -> configureHeaders((HttpHeaders) httpHeaders))
                .retrieve()
                .bodyToMono(responseClass);
    }
}