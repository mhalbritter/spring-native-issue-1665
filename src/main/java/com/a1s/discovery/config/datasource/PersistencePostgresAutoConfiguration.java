package com.a1s.discovery.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "postgresEntityManagerFactory", transactionManagerRef = "postgresTransactionManager", basePackages = { "com.a1s.discovery.repository" })
@AllArgsConstructor
@Slf4j
public class PersistencePostgresAutoConfiguration {

	private final Environment env;

	//    @Order(Ordered.HIGHEST_PRECEDENCE)
	@Primary
	@Bean(name = "postgresDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public HikariDataSource postgresDataSource() {
		log.info(env.toString()); log.info(env.getProperty("spring.datasource.url"));
		return DataSourceBuilder.create().type(HikariDataSource.class).driverClassName("org.postgresql.Driver").url(env.getProperty("spring.datasource.url")).username(env.getProperty("spring.datasource.username")).password(env.getProperty("spring.datasource.password")).build();
	}

	public Map<String, String> hikariConfig() {
		Map<String, String> properties = new HashMap<>();
		properties.put("driverClassName", "org.postgresql.Driver");
		properties.put("jdbcUrl", env.getProperty("spring.datasource.url"));
		properties.put("url", env.getProperty("spring.datasource.url"));
		properties.put("username", env.getProperty("spring.datasource.username"));
		properties.put("password", env.getProperty("spring.datasource.password"));
		properties.put("minimumIdle", "5"); properties.put("maximumPoolSize", "8");
		properties.put("idleTimeout", "30000"); ;
		properties.put("poolName", "SpringBootJPAHikariCP");
		properties.put("maxLifetime", "600000"); properties.put("keepaliveTime", "60000");
		properties.put("connectionTimeout", "30000");
		properties.put("initializationFailTimeout", "10000");
		properties.put("minimumIdle", "10000");

//        properties.put("jpa.properties.hibernate.cache.use_second_level_cache", "true");
//        properties.put("jpa.properties.hibernate.cache.use_query_cache", "false");
//        properties.put("jpa.properties.hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastCacheRegionFactory");
		return properties;
	}

	@Bean(name = "postgresEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("postgresDataSource") DataSource dataSource) {
		return entityManagerFactoryBuilder.dataSource(dataSource).packages("com.a1s.discovery.domain").persistenceUnit("discovery").properties(hikariConfig()).build();
	}

	//    @Primary
	@Bean(name = "postgresTransactionManager")
	public PlatformTransactionManager customerTransactionManager(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
		return new JpaTransactionManager(customerEntityManagerFactory);
	}
}
