package me.andidroid.testwar;

import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.output.MigrateResult;
import org.flywaydb.core.api.configuration.ClassicConfiguration;

public class MicroshedSharedContainerConfiguration implements SharedContainerConfiguration
{
        
        public static DockerImageName image = new DockerImageName("postgres", "13");
        
        @Container
        public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.0-alpine") // "postgres:latest"
                        .withNetworkAliases("mypostgres").withExposedPorts(5432).withUsername("postgres").withPassword("postgres").withInitScript("database/import.sql") // inside
                        // src/test/resources
                        .withDatabaseName("test");
        
        @Container
        public static MockServerContainer mockServer = new MockServerContainer().withNetworkAliases("mockserver");
        
        @Container
        public static ApplicationContainer app = new ApplicationContainer()
                        
                        .withEnv("POSTGRESQL_JNDI", "java:jboss/datasources/PostgreSQLDS").withEnv("POSTGRESQL_HOST", "mypostgres").withEnv("POSTGRESQL_PORT", "5432").withEnv("POSTGRESQL_USER", "postgres").withEnv("POSTGRESQL_PASSWORD", "postgres")
                        .withEnv("POSTGRESQL_DATABASE", "test").withEnv("message", "Hello World from MicroShed Testing").withEnv("JGROUPS_CLUSTER_PASSWORD", "test").withReadinessPath("/testservice/hello/hello").dependsOn(postgres);
        // .withAppContextRoot("/testwar")
        ;
        // .withMpRestClient(QuoteRestClient.class,"http://mockserver:"+MockServerContainer.PORT);
        
        @Override
        public void startContainers()
        {
                
                System.out.println("startContainers");
                postgres.start();
                System.out.println("postgres started");
                Flyway flyway = Flyway.configure().dataSource("jdbc:tc:postgres:14.0-alpine://mypostgres:5432/test", "postgres", "postgres").load();
                MigrateResult result = flyway.migrate();
                
                System.out.println("flyway migrated " + result.migrationsExecuted + " to " + result.targetSchemaVersion);
                app.start();
                System.out.println("app started");
                System.out.println("startContainers finished");
                
                // super.startContainers();
        }
        
}
