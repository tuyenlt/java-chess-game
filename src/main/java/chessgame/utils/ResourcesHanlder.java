package chessgame.utils;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ResourcesHanlder {

    public static File convertToJPG(File inputFile) throws IOException {
        if (!inputFile.exists()) {
            throw new IOException("File does not exist: " + inputFile.getPath());
        }

        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            throw new IOException("Invalid image format for file: " + inputFile.getPath());
        }

        BufferedImage jpgImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        jpgImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        String outputFilePath = inputFile.getParent() + File.separator 
                                + inputFile.getName().replaceAll("\\.[^.]+$", "") + ".jpg";
        File outputFile = new File(outputFilePath);

        boolean result = ImageIO.write(jpgImage, "jpg", outputFile);
        if (!result) {
            throw new IOException("Failed to write the JPG image.");
        }

        return outputFile;
    }

    public static File selectFile(Stage stage) throws Exception {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            long fileSizeInBytes = selectedFile.length(); 
            long maxFileSizeInBytes = 2 * 1024 * 1024;

            if (fileSizeInBytes > maxFileSizeInBytes) {
                throw new Exception("File size exceeds 2MB: " + selectedFile.getName());
            }

            System.out.println("File selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection canceled.");
        }

        return selectedFile; 
    }


    public static Image cropImageToSquare(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();

        double size = Math.min(width, height);

        double x = (width - size) / 2;
        double y = (height - size) / 2;

        return new WritableImage(image.getPixelReader(), (int) x, (int) y, (int) size, (int) size);
    }
}
