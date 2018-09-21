package io.mahmood.adam.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ClientApplication {

	@Autowired
	private EurekaClient client;

	@Autowired
	private RestTemplateBuilder restTemplatebuilder;

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@RequestMapping("/")
	public String callservice() {

		// So the EurekaClient is calling out to the Discovery Server, and it's getting
		// information about a service ID called service, and it's returning it back to
		// us as an instanceInfo. And then from that instanceInfo, we're getting the
		// HomePageUrl, which is the base URL of our service, and then we're using our
		// restTemplate to call that service, specifically a GET on that service, and it
		// returns a string back to us
		RestTemplate restTemplate = restTemplatebuilder.build();
		InstanceInfo instanceinfo = client.getNextServerFromEureka("service", false);
		String baseUrl = instanceinfo.getHomePageUrl();

		ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);
		return response.getBody();
	}
}
