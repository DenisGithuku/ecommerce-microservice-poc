package com.ecommerce.order.productserviceclient;

import com.netflix.discovery.converters.Auto;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private Propagator propagator;

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        RestClient.Builder builder = RestClient.builder();
        if (observationRegistry != null) {
            builder.requestInterceptor(createTracingInterceptor());
        }
        return builder;
    }

    @Bean
    @Primary
    public RestClient.Builder primaryRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public UserServiceClient restClient(@LoadBalanced RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("http://user-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (((request, response) -> Optional.empty())))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserServiceClient.class);
    }

    private ClientHttpRequestInterceptor createTracingInterceptor() {
        return (((request, body, execution) -> {
            if (tracer != null && propagator != null && tracer.currentSpan() != null) {
                propagator.inject(tracer.currentTraceContext().context(), request.getHeaders(), (carrier, key, value) -> carrier.add(key, value));
            }
            return execution.execute(request, body);
        }));
    }
}
