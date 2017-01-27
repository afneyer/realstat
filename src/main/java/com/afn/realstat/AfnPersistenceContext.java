package com.afn.realstat;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.afn.realstat")
@EnableAutoConfiguration
class AfnPersistenceContext {
	
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Bean
	public DataSource afn1DataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("admin");
        return datasource;
	}

	@Bean( name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean factory = 
				new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(afn1DataSource());
		factory.setPackagesToScan(getClass().getPackage().getName());
	    HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(hjva);
	    // factory.setJpaDialect(new HibernateJpaDialect());
	    factory.setJpaProperties(additionalProperties());
	   
	    // HibernatePersistenceProvider p = new HibernatePersistenceProvider();
	    // factory.setPersistenceProvider( p );
	    // factory.setLoadTimeWeaver(new ReflectiveLoadTimeWeaver());
	     
		// factory.afterPropertiesSet(); // It will initialize EntityManagerFactory object otherwise below will return null
		
		log.info( "AFN: completed creation of the factory " + factory.toString() );
				
		return factory;
		
	}
	
	Properties additionalProperties() {
        Properties properties = new Properties();
//      "create-drop"
        properties.setProperty("hibernate.hbm2ddl.auto","update" );
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        // properties.setProperty("spring.jpa.hibernate.naming.strategy","org.hibernate.cfg.ImprovedNamingStrategy");
        // properties.setProperty("spring.jpa.hibernate.naming.physical-strategy","org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        // properties.setProperty("hibernate.archive.autodetection", "class");
        return properties;
     }
	

	/*
	@Bean
	public PersistenceAnnotationBeanPostProcessor postProcessor() {
		return new PersistenceAnnotationBeanPostProcessor();
	}
	// <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />
	*/
	
	

//	@Bean
//	PlatformTransactionManager transactionManager() {
//		@Autowired 
//		EntityManagerFactory emf;
//		JpaTransactionManager txManager = new JpaTransactionManager( emf );
//		return txManager;
//	}
	
	
	/**
	   * PersistenceExceptionTranslationPostProcessor is a bean post processor
	   * which adds an advisor to any bean annotated with Repository so that any
	   * platform-specific exceptions are caught and then rethrown as one
	   * Spring's unchecked data access exceptions (i.e. a subclass of 
	   * DataAccessException).
	   */
	  /* @Bean
	  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
	    return new PersistenceExceptionTranslationPostProcessor();
	  }*/
	  
	
}
