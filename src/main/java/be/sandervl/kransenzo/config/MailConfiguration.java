package be.sandervl.kransenzo.config;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

@Configuration
@ConfigurationProperties("spring.mail")
@Profile("prod")
public class MailConfiguration
{
    public static final String CREDENTIALS_PATH = "/tmp/beanstalk-credentials.json";
    private static Logger LOG = LoggerFactory.getLogger( MailConfiguration.class );
    private String host;
    private Integer port;
    private String protocol;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost( host );
        javaMailSender.setPort( port );
        javaMailSender.setProtocol( protocol );
        try {
            StringBuilder sb = new StringBuilder();
            Files.readLines( new File( CREDENTIALS_PATH ), Charset.defaultCharset() ).forEach( sb::append );
            Map<String, Object> objects = new JacksonJsonParser().parseMap( sb.toString() );
            javaMailSender.setUsername( (String) objects.get( "spring.mail.username" ) );
            javaMailSender.setPassword( (String) objects.get( "spring.mail.password" ) );
        }
        catch ( IOException e ) {
            LOG.error( "Unable to find credentials file for MailSender", e );
        }

        Properties props = new Properties();
        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.ssl.enable", "true" );
        props.put( "mail.transport.protocol", "smtps" );
        javaMailSender.setJavaMailProperties( props );
        return javaMailSender;
    }

    public String getHost() {
        return host;
    }

    public void setHost( String host ) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort( Integer port ) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol( String protocol ) {
        this.protocol = protocol;
    }
}
