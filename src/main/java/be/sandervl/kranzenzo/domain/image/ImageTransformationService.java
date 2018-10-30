package be.sandervl.kranzenzo.domain.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * @author Sander Van Loock
 */
@Service
public class ImageTransformationService{
    private static Logger log = LoggerFactory.getLogger(ImageTransformationService.class);
    public byte[] rotateAccordingExif(byte[] image, String name, String contentType){
        try{
            BufferedImage bufferedImage = ImageConversion.convertToBufferedImage(image);
            if (bufferedImage == null){
                log.warn("Given image can not be converted to BufferedImage, not doing any transformations");
                return image;
            }
            File file = ImageConversion.convertToFile(image, name, contentType);
            BufferedImage transformedImage = ImageTransformation.rotateFromExif(bufferedImage, file);
            return ImageConversion.convertToByteArray(contentType, transformedImage);
        }
        catch (IOException e){
            log.error("Unable to transform image before uploading it to S3", e);
        }
        return image;
    }
}
