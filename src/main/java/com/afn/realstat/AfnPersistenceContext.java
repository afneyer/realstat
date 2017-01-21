package com.afn.realstat;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
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
		// TODO existing dataSource ??
		DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("admin");
        return datasource;
	}

	@Bean( name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
		HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
	    hjva.setDatabase(Database.MYSQL);
	    // hjva.setGen

		LocalContainerEntityManagerFactoryBean factory = 
				new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(afn1DataSource());
		// DefaultPersistenceUnitManager pm = new DefaultPersistenceUnitManager();
		// pm.setPackagesToScan(packagesToScan);
		// factory.setPersistenceUnitManager(pm);
		// factory.setPersistenceUnitPostProcessors(postProcessors);
	    // factory.setPersistenceUnitName("default");
	    factory.setPackagesToScan(getClass().getPackage().getName(),"com.afn.realstat");
	    // HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(hjva);
	    factory.setJpaDialect(new HibernateJpaDialect());
	    factory.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);  
	    factory.setPackagesToScan(getClass().getPackage().getName());
	    factory.setJpaProperties(additionalProperties());
	   //  HibernatePersistenceProvider p = new HibernatePersistenceProvider();
	    // factory.setPersistenceProvider( p );
	    // factory.setLoadTimeWeaver(new ReflectiveLoadTimeWeaver());
	     
		// factory.afterPropertiesSet(); // It will initialize EntityManagerFactory object otherwise below will return null
		
		log.info( "AFN: completed creation of the factory " + factory.toString() );
				
		return factory;
		
	}
	

	/*
	@Bean
	public PersistenceAnnotationBeanPostProcessor postProcessor() {
		return new PersistenceAnnotationBeanPostProcessor();
	}
	// <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />
	*/
	
	Properties additionalProperties() {
        Properties properties = new Properties();
//      "create-drop"
        properties.setProperty("hibernate.hbm2ddl.auto","update" );
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        // properties.setProperty("hibernate.archive.autodetection", "class");
        return properties;
     }

	/*
	@Bean
	PlatformTransactionManager transactionManager( EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager( emf );
		return txManager;
	}
	*/
	
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
