package be.brickbit.lpm.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "be.brickbit.lpm.core.repository", entityManagerFactoryRef =
        "coreEntityManagerFactory", transactionManagerRef = "coreTransactionManager")
@EnableTransactionManagement
public class DefaultRepoConfig {
    @Bean
    PlatformTransactionManager coreTransactionManager(@Qualifier("coreEntityManagerFactory") final EntityManagerFactory
                                                                  factory) {
        return new JpaTransactionManager(factory);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean coreEntityManagerFactory(final EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource()).packages("be.brickbit.lpm.core.domain").persistenceUnit
                ("core").build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
