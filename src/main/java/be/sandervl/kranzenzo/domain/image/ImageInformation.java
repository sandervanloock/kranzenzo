package be.sandervl.kranzenzo.domain.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
/**
 * https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
 */
public class ImageInformation{
    private static Logger log = LoggerFactory.getLogger(ImageInformation.class);
    public final int orientation;
    public final int width;
    public final int height;
    public ImageInformation(int orientation, int width, int height){
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }
    public static ImageInformation readImageInformation(File imageFile) throws IOException, MetadataException, ImageProcessingException{
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        if (directory == null || jpegDirectory == null){
            throw new MetadataException("Unable to get correct metadata from imageFile");
        }
        int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();
        return new ImageInformation(orientation, width, height);
    }
    // Look at http://chunter.tistory.com/143 for information
    public static AffineTransform getExifTransformation(ImageInformation info){
        AffineTransform t = new AffineTransform();
        switch (info.orientation){
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.width, 0);
                break;
            case 3: // PI rotation
                t.translate(info.width, info.height);
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.height);
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(info.height, 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.height, 0);
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
        }
        return t;
    }
    public String toString(){
        return String.format("%dx%d,%d", this.width, this.height, this.orientation);
    }
}
