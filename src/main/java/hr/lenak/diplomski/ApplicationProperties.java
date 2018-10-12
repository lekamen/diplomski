package hr.lenak.diplomski;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

	private int tableLength;

	public int getTableLength() {
		return tableLength;
	}

	public void setTableLength(int tableLength) {
		this.tableLength = tableLength;
	}
	
	
}
