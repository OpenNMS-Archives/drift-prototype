package org.opennms.drift;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.elasticsearch.client.Client;
import org.opennms.drift.listener.NetflowListener;
import org.opennms.drift.repository.NetflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@SpringBootApplication
public class DriftApplication implements CommandLineRunner {

	@Autowired
	private ElasticsearchOperations es;

	@Autowired
	private NetflowRepository netflowRepository;

	public static void main(String[] args) throws SocketException, UnknownHostException {
		SpringApplication.run(DriftApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		System.out.println("--ElasticSearch--");
		Client client = es.getClient();
		Map<String, String> asMap = client.settings().getAsMap();
		asMap.forEach((k, v) -> {
			System.out.println(k + " = " + v);
		});
		System.out.println("--ElasticSearch--");


		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			try {
				new NetflowListener(netflowRepository).listen();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
