package src.main.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class CatalogoProdutosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoProdutosServiceApplication.class, args);
	}

}
