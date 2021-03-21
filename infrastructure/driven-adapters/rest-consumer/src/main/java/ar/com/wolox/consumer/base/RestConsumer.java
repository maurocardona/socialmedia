package ar.com.wolox.consumer.base;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

public abstract class RestConsumer {

    private Logger logger = LogManager.getLogger(this.getClass());

    private WebClient webClient;

    public RestConsumer(String baseURL) {
        webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl(baseURL)
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    public <R> Flux<R> getRequest(String uri, Class<R> responseClass, Object... pathParams) {
        return webClient.method(HttpMethod.GET)
                .uri(UriComponentsBuilder.newInstance()
                        .path(uriFormat(uri, pathParams))
                        .build()
                        .toUriString())
                .retrieve()
                .bodyToFlux(responseClass);
    }

    private String uriFormat(String uri, Object... pathParams){
        return Optional.ofNullable(pathParams).map(objects -> {
            if (StringUtils.countMatches(uri, "%s") != objects.length) {
                throw new IllegalArgumentException("Cantidad de parametros incorrecta");
            }
            return String.format(uri, objects);
        }).orElse(uri);
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
}