package be.sandervl.kranzenzo.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.monitoring.ProfileCsmConfigurationProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Sander Van Loock
 */
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsConfiguration{

    private static Logger log = LoggerFactory.getLogger(AwsConfiguration.class);

    private AwsS3Configuration s3;

    public AwsS3Configuration getS3(){
        return s3;
    }

    public void setS3(AwsS3Configuration s3){
        this.s3 = s3;
    }

    @Bean
    @Profile("prod")
    public AmazonS3 amazonS3(){
        return AmazonS3ClientBuilder.defaultClient();
    }

    @Bean
    @Profile("dev")
    public AmazonS3 amazonS3dev(){
        return AmazonS3ClientBuilder.standard()
                                    .withCredentials(new ProfileCredentialsProvider("jhipster"))
                                    .withRegion(Regions.EU_WEST_1)
                                    .withClientSideMonitoringConfigurationProvider( new ProfileCsmConfigurationProvider( "jhipster" ) )
                                    .build();
    }

    @ConfigurationProperties(prefix = "s3")
    public static class AwsS3Configuration{
        private String endpointUrl;
        private String bucketName;

        public String getEndpointUrl(){
            return endpointUrl;
        }

        public void setEndpointUrl(String endpointUrl){
            this.endpointUrl = endpointUrl;
        }

        public String getBucketName(){
            return bucketName;
        }

        public void setBucketName(String bucketName){
            this.bucketName = bucketName;
        }

        @Override
        public String toString(){
            return "AwsS3Configuration{" +
                "endpointUrl='" + endpointUrl + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
        }
    }
}
