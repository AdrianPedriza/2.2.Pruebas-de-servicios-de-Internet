package es.codeurjc.anuncios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = AnunciosControllerTest.Initializer.class)
public class AnunciosControllerTest{
	
	@Container
    public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7")
      .withDatabaseName("test")
      .withUsername("user")
      .withPassword("pass");

    @Autowired
    private AnunciosRepository anunciosRepository;

    static class Initializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
              "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
              "spring.datasource.username=" + mySQLContainer.getUsername(),
              "spring.datasource.password=" + mySQLContainer.getPassword(),
              "spring.datasource.driverClassName=com.mysql.jdbc.Driver",
              "spring.jpa.hibernate.ddl-auto=create-drop"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void test() throws InterruptedException {
        List<Anuncio> anuncios = anunciosRepository.findAll();
        assertEquals(anuncios.size(), 2);
    }

}