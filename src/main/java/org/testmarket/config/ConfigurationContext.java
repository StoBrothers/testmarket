package org.testmarket.config;

import java.util.Properties;

import javax.activation.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
public class ConfigurationContext {
    
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String[] ENTITYMANAGER_PACKAGES_TO_SCAN = {"a.b.c.entities", "a.b.c.converters"};

//    @Autowired
//    private Environment env;

//     @Bean(destroyMethod = "close")
//     public DataSource dataSource() {
//         BasicDataSource dataSource = new BasicDataSource();
//         dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
//         dataSource.setUrl(env.getProperty("jdbc.url"));
//         dataSource.setUsername(env.getProperty("jdbc.username"));
//         dataSource.setPassword(env.getProperty("jdbc.password"));
//         return dataSource;
//     }

//     @Bean
//     public JpaTransactionManager jpaTransactionManager() {
//         JpaTransactionManager transactionManager = new JpaTransactionManager();
//         transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
//         return transactionManager;
//     }

//    private HibernateJpaVendorAdapter vendorAdaptor() {
//         HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//         vendorAdapter.setShowSql(true);
//         return vendorAdapter;
//    }

    
//   @Autowired (required=true)
//   LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
//
//         LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//         entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
////         entityManagerFactoryBean.setDataSource(dataSource());
//         entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//         entityManagerFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);             
//         entityManagerFactoryBean.setJpaProperties(jpaHibernateProperties());
//
//         return entityManagerFactoryBean;
//     }

//     private Properties jpaHibernateProperties() {
//
//         Properties properties = new Properties();
//
//         properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, env.getProperty(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH));
//         properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE));
//         properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE));
//         properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
//
//         properties.put(AvailableSettings.SCHEMA_GEN_DATABASE_ACTION, "none");
//         properties.put(AvailableSettings.USE_CLASS_ENHANCER, "false");      
//         return properties;       ` 
//     }

    
//    @Bean
//    public HibernateTransactionManager transactionManager(
//            SessionFactory sessionFactory) {
//        HibernateTransactionManager tm = new HibernateTransactionManager(
//                sessionFactory);
//        return tm;
//    }
//    
//    
//    
//
//    @Bean
//    DataSource dataSource() {
//        return new SimpleDriverDataSource() {
//             {  setDriverClass(org.h2.Driver.class);
//                setUsername("sa");
//                setUrl("jdbc:h2:mem");
//                setPassword("");
//            }
//        };
//    }
//
//    @Bean
//    JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        log.info("Creating tables");
//        jdbcTemplate.execute("drop table BOOKINGS if exists");
//        jdbcTemplate.execute("create table BOOKINGS("
//                + "ID serial, FIRST_NAME varchar(5) NOT NULL)");
//        return jdbcTemplate;
//    }

}
