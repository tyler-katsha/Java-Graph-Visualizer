import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class ImageService {

    private final static String FOLDER = "./Image";
    private final static String IMG_FORMAT = "png";

    public static void saveImage(int width,int height,CustomPanel panel){

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        panel.paint(img.getGraphics());
        try{

            File file = new File(FOLDER);

            if(!file.exists()) {
                boolean isCreated = file.mkdirs();

                if(!isCreated) {
                    throw new FileNotFoundException();
                }
            }

            String[] item = file.list();
            long count = (item != null) ? item.length : 0;

            ImageIO.write(img,IMG_FORMAT,new File("./Image/graph-" + (count + 1) + ".png"));

            String reason = "Panel saved as image";
            String title = "Image saved";
            Popups.showInfo(reason,title);

        }  catch(FileNotFoundException e){
            String reason = "File path not found";
            String title = "Location not found";
            Popups.showError(reason,title);
        } catch(FileAlreadyExistsException e){

            String reason = "File duplicate found";
            String title = "Duplicate file";
            Popups.showWarning(reason,title);
        }
        catch(Exception e){
            String reason = "Something went wrong";
            String title = "Error";
            Popups.showError(reason,title);
        }
    }
}
