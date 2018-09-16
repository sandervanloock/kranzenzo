package be.sandervl.kranzenzo.config;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * @author Sander Van Loock
 */
@TestConfiguration
public class DummyS3Configuration {

    @Bean
    public AmazonS3 amazonS3dev() {
        return mock( AmazonS3.class );
    }
}
