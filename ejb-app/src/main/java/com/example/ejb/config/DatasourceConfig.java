package com.example.ejb.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
@DataSourceDefinition(
        name = "java:jboss/datasources/BipDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        serverName = "db",
        portNumber = 5432,
        databaseName = "bip",
        user = "bip",
        password = "bip"
)
public class DatasourceConfig {
}
