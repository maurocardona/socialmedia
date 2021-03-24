package ar.com.wolox.consumer.base;

import ar.com.wolox.model.socialmedia.common.ApplicationException;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.prometheus.PrometheusRenameFilter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.reactive.client.DefaultWebClientExchangeTagsProvider;
import org.springframework.boot.actuate.metrics.web.reactive.client.MetricsWebClientFilterFunction;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
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

    @Setter
    private WebClient webClient;

    public RestConsumer(String baseURL) {

        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        prometheusRegistry.config().meterFilter(new PrometheusRenameFilter());
        MetricsWebClientFilterFunction metricsWebClientFilterFunction = new MetricsWebClientFilterFunction(prometheusRegistry, new DefaultWebClientExchangeTagsProvider(), "webClientMetrics", AutoTimer.DISABLED);

        webClient = WebClient.builder()
                .filter(metricsWebClientFilterFunction)
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
        return getRequest(uri, responseClass, null, pathParams);
    }

    public <R> Flux<R> getRequest(String uri, Class<R> responseClass, MultiValueMap<String, String> queryParams, Object... pathParams) {
        return webClient.method(HttpMethod.GET)
                .uri(UriComponentsBuilder.newInstance()
                        .path(uriFormat(uri, pathParams))
                        .queryParams(queryParams)
                        .build()
                        .toUriString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleError(response, "Error en los datos de entrada del servicio", "000"))
                .onStatus(HttpStatus::is5xxServerError, response -> handleError(response, "Error en el servicio", "001"))
                .bodyToFlux(responseClass);
    }

    private Mono<Throwable> handleError(ClientResponse response, String errorMessage, String errorCode) {
        return response.bodyToMono(String.class).flatMap(er -> {
            logger.error(er);
            return Mono.error(new ApplicationException(response.statusCode(), errorMessage, errorCode));
        });
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