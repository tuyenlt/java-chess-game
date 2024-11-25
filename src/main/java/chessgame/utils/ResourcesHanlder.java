package chessgame.utils;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ResourcesHanlder {

    public static File convertToJPG(File inputFile) throws IOException {
        // Check if the input file exists
        if (!inputFile.exists()) {
            throw new IOException("File does not exist: " + inputFile.getPath());
        }

        // Read the input image
        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            throw new IOException("Invalid image format for file: " + inputFile.getPath());
        }

        // Create a new BufferedImage in RGB format
        BufferedImage jpgImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        jpgImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        // Generate the output file path with .jpg extension
        String outputFilePath = inputFile.getParent() + File.separator 
                                + inputFile.getName().replaceAll("\\.[^.]+$", "") + ".jpg";
        File outputFile = new File(outputFilePath);

        // Write the new image to the output file
        boolean result = ImageIO.write(jpgImage, "jpg", outputFile);
        if (!result) {
            throw new IOException("Failed to write the JPG image.");
        }

        return outputFile;
    }

    public static File selectFile(Stage stage) throws Exception {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();

        // Set initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Set file extension filters (optional)
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            long fileSizeInBytes = selectedFile.length(); // Get file size in bytes
            long maxFileSizeInBytes = 2 * 1024 * 1024; // 2MB in bytes

            // Check if the file size exceeds the limit
            if (fileSizeInBytes > maxFileSizeInBytes) {
                throw new Exception("File size exceeds 2MB: " + selectedFile.getName());
            }

            System.out.println("File selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection canceled.");
        }

        return selectedFile; 
    }
}
