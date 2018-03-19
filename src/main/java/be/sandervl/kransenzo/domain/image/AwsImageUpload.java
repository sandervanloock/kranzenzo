package be.sandervl.kransenzo.domain.image;

import be.sandervl.kransenzo.config.AwsConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * @author Sander Van Loock
 */
@Component
public class AwsImageUpload{

    private static Logger LOG = LoggerFactory.getLogger(AwsImageUpload.class);

    private final AmazonS3 s3client;
    private final AwsConfiguration.AwsS3Configuration s3;

    public AwsImageUpload(AmazonS3 s3client, AwsConfiguration awsConfiguration){
        this.s3client = s3client;
        this.s3 = awsConfiguration.getS3();
    }

    public Optional<String> uploadToS3(byte[] data, String name, String contentType){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        String generateFileName = generateFileName(name);
        LOG.debug("Uploading file {} with generated name {} to ", s3);
        PutObjectRequest putObjectRequest = new PutObjectRequest(s3.getBucketName(),
                                                                 generateFileName,
                                                                 new ByteArrayInputStream(data),
                                                                 metadata);
        try{
            s3client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
            return Optional.of(s3.getEndpointUrl() + "/" + s3.getBucketName() + "/" + generateFileName);
        }
        catch (SdkClientException e){
            LOG.error("Unable to upload {} to S3 due to {}", generateFileName, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean deleteFromS3(String endpoint){
        if (StringUtils.isBlank(endpoint)){
            return false;
        }
        int lastIndexOf = endpoint.lastIndexOf("/");
        if (lastIndexOf == -1){
            return false;
        }
        String fileName = endpoint.substring(lastIndexOf + 1);
        try{
            s3client.deleteObject(new DeleteObjectRequest(s3.getBucketName(), fileName));
        }
        catch (SdkClientException e){
            LOG.error("Unable to delete {} from S3 due to {}", endpoint, e.getMessage(), e);
            return false;
        }
        return true;
    }

    private String generateFileName(String fileName){
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "-" +
            fileName.replace(" ", "_");
    }

}
