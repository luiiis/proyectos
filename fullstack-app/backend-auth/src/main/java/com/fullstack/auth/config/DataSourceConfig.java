package com.fullstack.auth.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración de múltiples DataSources.
 * - MySQL: Usuarios y Autenticación (Primary)
 * - Oracle: Productos e Inventario
 * - SQL Server: Logs y Auditoría
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    // ==================== MySQL (Primary) ====================

    @Configuration
    @EnableJpaRepositories(
            basePackages = "com.fullstack.auth.repository.mysql",
            entityManagerFactoryRef = "mysqlEntityManagerFactory",
            transactionManagerRef = "mysqlTransactionManager"
    )
    public static class MysqlConfig {

        @Primary
        @Bean
        @ConfigurationProperties("spring.datasource.mysql")
        public DataSourceProperties mysqlDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Primary
        @Bean
        public DataSource mysqlDataSource() {
            return mysqlDataSourceProperties()
                    .initializeDataSourceBuilder()
                    .type(HikariDataSource.class)
                    .build();
        }

        @Primary
        @Bean
        public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
                EntityManagerFactoryBuilder builder) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

            return builder
                    .dataSource(mysqlDataSource())
                    .packages("com.fullstack.auth.entity.mysql")
                    .persistenceUnit("mysql")
                    .properties(properties)
                    .build();
        }

        @Primary
        @Bean
        public PlatformTransactionManager mysqlTransactionManager(
                @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }
    }

    // ==================== Oracle ====================

    @Configuration
    @EnableJpaRepositories(
            basePackages = "com.fullstack.auth.repository.oracle",
            entityManagerFactoryRef = "oracleEntityManagerFactory",
            transactionManagerRef = "oracleTransactionManager"
    )
    public static class OracleConfig {

        @Bean
        @ConfigurationProperties("spring.datasource.oracle")
        public DataSourceProperties oracleDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        public DataSource oracleDataSource() {
            return oracleDataSourceProperties()
                    .initializeDataSourceBuilder()
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
                EntityManagerFactoryBuilder builder) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");

            return builder
                    .dataSource(oracleDataSource())
                    .packages("com.fullstack.auth.entity.oracle")
                    .persistenceUnit("oracle")
                    .properties(properties)
                    .build();
        }

        @Bean
        public PlatformTransactionManager oracleTransactionManager(
                @Qualifier("oracleEntityManagerFactory") EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }
    }

    // ==================== SQL Server ====================

    @Configuration
    @EnableJpaRepositories(
            basePackages = "com.fullstack.auth.repository.sqlserver",
            entityManagerFactoryRef = "sqlserverEntityManagerFactory",
            transactionManagerRef = "sqlserverTransactionManager"
    )
    public static class SqlServerConfig {

        @Bean
        @ConfigurationProperties("spring.datasource.sqlserver")
        public DataSourceProperties sqlserverDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        public DataSource sqlserverDataSource() {
            return sqlserverDataSourceProperties()
                    .initializeDataSourceBuilder()
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean sqlserverEntityManagerFactory(
                EntityManagerFactoryBuilder builder) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");

            return builder
                    .dataSource(sqlserverDataSource())
                    .packages("com.fullstack.auth.entity.sqlserver")
                    .persistenceUnit("sqlserver")
                    .properties(properties)
                    .build();
        }

        @Bean
        public PlatformTransactionManager sqlserverTransactionManager(
                @Qualifier("sqlserverEntityManagerFactory") EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }
    }
}
