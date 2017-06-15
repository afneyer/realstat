package com.afn.realstat.framework;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.AbstractEntityRepository;

/**
	 * Wrapper to always return a reference to the Spring Application Context from
	 * within non-Spring enabled beans. Unlike Spring MVC's WebApplicationContextUtils
	 * we do not need a reference to the Servlet context for this. All we need is
	 * for this bean to be initialized during application startup.
	 */
	// TODO delete
	@Deprecated
	public class AppContext implements ApplicationContextAware {

	  private static ApplicationContext CONTEXT;

	  /**
	   * This method is called from within the ApplicationContext once it is 
	   * done starting up, it will stick a reference to itself into this bean.
	   * @param context a reference to the ApplicationContext.
	   */
	  @Override
	  public void setApplicationContext(ApplicationContext context) throws BeansException {
	    CONTEXT = context;
	  }

	  /**
	   * This is about the same as context.getBean("beanName"), except it has its
	   * own static handle to the Spring context, so calling this method statically
	   * will give access to the beans by name in the Spring application context.
	   * As in the context.getBean("beanName") call, the caller must cast to the
	   * appropriate target class. If the bean does not exist, then a Runtime error
	   * will be thrown.
	   * @param beanName the name of the bean to get.
	   * @return an Object reference to the named bean.
	   */
	  public static Object getRepo(String repoName) {
	    return CONTEXT.getBean(repoName);
	  }
	  
	  public static Connection getConnection() {
		  Connection connect = null;
		  DataSource ds = getDataSource();
		  try {
			connect = ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("AppContext: Cannot get connection from datasource 'dataSource'", e);
		}
		  return connect;
		  
	  }

	  public static DataSource getDataSource() {
		DataSource ds = (DataSource) CONTEXT.getBean("dataSource");
		return ds;
	}

	public static EntityManager getEntityManager() {
		  EntityManager em = (EntityManager) CONTEXT.getBean("entityManager");
		  return em;
	  }
	  
	  public static Metamodel getMetaModel() {
		  Metamodel mm = getEntityManager().getMetamodel();
		  return mm;
	  }
	  
	  public static <T extends AbstractEntity> List<T> getStringFieldSetters( Class c ) {
		  Metamodel mm = getMetaModel();
		  javax.persistence.metamodel.EntityType<T> type2 = mm.entity(c);
		  Set<Attribute<T, ?>> attributes = type2.getDeclaredAttributes();
		  for ( Attribute<T,?> attribute : attributes ) {
			  attribute.getName();
			  
			 
		  }
		  return null;
	  }
	  
	  // TODO check whether this is useful
	  public static <T extends AbstractEntity> AbstractEntityRepository<T> getRepo1( String repoName, Class<T> reqType ) {
		@SuppressWarnings("unchecked")
		AbstractEntityRepository<T> repo = (AbstractEntityRepository<T>) CONTEXT.getBean(repoName, reqType);
		  return repo;
	  }
	  
}
