package hr.lenak.diplomski.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.server.SpringVaadinServlet;

@Configuration
public class VaadinConfiguration {

	@Bean
	public VaadinServlet vaadinServlet() {
		return new SpringVaadinServlet();
	}
}
