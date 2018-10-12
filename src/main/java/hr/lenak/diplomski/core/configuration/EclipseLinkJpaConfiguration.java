package hr.lenak.diplomski.core.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.logging.DefaultSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {

	private Logger log = LoggerFactory.getLogger(this.getClass());

    public EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, properties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.validation.mode", "none");
		props.put("eclipselink.weaving", "true");
		props.put("eclipselink.weaving.changetracking", "false");

		props.put("eclipselink.logging.level", "INFO");
		props.put("eclipselink.logging.level.sql", "FINE");
		props.put("eclipselink.logging.parameters", "true");
		props.put("eclipselink.logging.session", "false");
		props.put("eclipselink.logging.connection", "false");
		props.put("eclipselink.logging.thread", "false");
		props.put("eclipselink.logging.logger", EclipseLinkLog.class.getName());
        return props;
    }
    
	@Bean
	public LoadTimeWeaver loadTimeWeaver() {
		return new DefaultContextLoadTimeWeaver();
	}
	
	public static class EclipseLinkLog extends DefaultSessionLog {

		private Logger log = LoggerFactory.getLogger("EclipseLink");

		@Override
		public void log(SessionLogEntry logEntry) {
			if (this.shouldLog(logEntry.getLevel(), logEntry.getNameSpace())) {
				log.debug(formatMessage(logEntry));
			}
		}
	}
}
