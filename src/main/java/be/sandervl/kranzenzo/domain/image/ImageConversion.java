package be.sandervl.kranzenzo.domain.image;

import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
/**
 * @author Sander Van Loock
 */
public class ImageConversion{
    public static BufferedImage convertToBufferedImage(byte[] input) throws IOException{
        return ImageIO.read(new ByteArrayInputStream(input));
    }
    public static File convertToFile(byte[] input, String name, String contentType) throws IOException{
        File temp = File.createTempFile(name, "." + contentType);
        FileUtils.writeByteArrayToFile(temp, input);
        return temp;
    }
    public static byte[] convertToByteArray(String contentType, BufferedImage transformedImage) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(transformedImage, contentType, os);
        return os.toByteArray();
    }
}
