package demo.cdcnpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"demo.cdcnpm"})
public class CdcnpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdcnpmApplication.class, args);
	}

}
