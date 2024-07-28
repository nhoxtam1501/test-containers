package dev.ducku.testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ducku.testcontainers.controllers.CourseController;
import dev.ducku.testcontainers.entity.Course;
import dev.ducku.testcontainers.repositories.CourseRepository;
import dev.ducku.testcontainers.services.CourseService;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(proxyBeanMethods = false)
public class TestContainersApplicationTests {

    //if this is not static then it will be created and stop by every test
    private static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest").withExposedPorts(3306);
    //withExposedPorts() to tell the test wait for this particular port(intelliJ testcontainers ytb)

    @Autowired
    private CourseService courseService;


    @DynamicPropertySource //override the properties value from application.properties file
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        //testcontainers by default will give a random port for this particular db, so you cannot ensure the number of the port itself
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        mySQLContainer.stop();
    }


    @Test
    public void test1() {
        Course course = new Course();
        course.setName("Spring MVC");
        courseService.save(course);
        int size = courseService.findAll().size();
        Assertions.assertThat(size).isEqualTo(1);
    }

    @Test
    public void test2() {
        Course course = courseService.findById(1);
        Assertions.assertThat(course.getName()).isEqualTo("Spring MVC");
    }

    public void someUsefulCommand() {
        mySQLContainer.withClasspathResourceMapping("application.properties",
                "/tmp/application.properties", BindMode.READ_ONLY);
        mySQLContainer.withFileSystemBind("C:\\dev\\application.properties","/tmp/application.properties", BindMode.READ_ONLY);

        //exec command in container
        /*mySQLContainer.execInContainer("ps", "-la");
        mySQLContainer.getLogs(OutputFrame.OutputType.STDOUT);
        mySQLContainer.getLogConsumers(new Slf4jLogConsumer(log));*/

        Integer onYourMachine = mySQLContainer.getMappedPort(3306);

    }


    private String asJsonString(Object object) throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
