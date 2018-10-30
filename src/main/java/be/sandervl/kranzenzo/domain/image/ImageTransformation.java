package be.sandervl.kranzenzo.domain.image;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * @author Sander Van Loock
 */
public class ImageTransformation{
    private static Logger log = LoggerFactory.getLogger(ImageTransformation.class);
    public static BufferedImage rotateFromExif(BufferedImage input,
                                               File file){
        try{
            ImageInformation imageInformation = ImageInformation.readImageInformation(file);
            AffineTransform exifTransformation = ImageInformation.getExifTransformation(imageInformation);
            return transformImage(input, exifTransformation);
        }
        catch (MetadataException e){
            log.debug("This image could not be rotated because metadata could not be retrieved {}", input);
        }
        catch (IOException | ImageProcessingException e){
            log.error("Unable to rotate {} according to exif due to {}", input, e.getCause(), e);
        }
        return input;
    }
    private static BufferedImage transformImage(BufferedImage bsrc, AffineTransform at){
        BufferedImage bdest = new BufferedImage(bsrc.getWidth(), bsrc.getHeight(), bsrc.getType());
        Graphics2D g = bdest.createGraphics();
        g.drawRenderedImage(bsrc, at);
        return bdest.getSubimage(0, 0, bsrc.getWidth(), bsrc.getHeight());
    }
}
