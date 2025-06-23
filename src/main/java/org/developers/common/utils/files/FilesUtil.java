package org.developers.common.utils.files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

public class FilesUtil {

    // Constantes para tipos de archivos permitidos
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("png", "jpg", "jpeg");
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mov", "mpeg", "mpg", "mp4", "mpeg1", "mpeg2", "mpeg4");

    // Tamaños máximos (en bytes)
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    private static final int THUMBNAIL_WIDTH = 150;
    private static final int THUMBNAIL_HEIGHT = 150;


    public String uploadProfileImage(byte[] imageData, String fileName) throws IOException {
        validateImageFile(imageData, fileName);

        String uniqueFileName = generateUniqueFileName(fileName);
        Path originalPath = saveFile(imageData, "profiles/original", uniqueFileName);

        // Generar y guardar miniatura
        BufferedImage thumbnail = createThumbnail(imageData);
        String thumbnailName = "thumb_" + uniqueFileName;
        saveThumbnail(thumbnail, "profiles/thumbnails", thumbnailName);

        return uniqueFileName;
    }

    public String uploadVideo(byte[] videoData, String fileName) throws IOException {
        validateVideoFile(videoData, fileName);

        String uniqueFileName = generateUniqueFileName(fileName);
        saveFile(videoData, "videos", uniqueFileName);

        return uniqueFileName;
    }

    private void validateImageFile(byte[] data, String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Formato de imagen no permitido. Use: " + ALLOWED_IMAGE_EXTENSIONS);
        }
        if (data.length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido de 5MB");
        }
    }

    private void validateVideoFile(byte[] data, String fileName){
        String extension = getFileExtension(fileName).toLowerCase();
        if (!ALLOWED_VIDEO_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Formato de video no permitido. Use: " + ALLOWED_VIDEO_EXTENSIONS);
        }
        if (data.length > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("El video excede el tamaño máximo permitido de 100MB");
        }
    }

    private BufferedImage createThumbnail(byte[] imageData) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        BufferedImage thumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);

        thumbnail.getGraphics().drawImage(
                originalImage.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, java.awt.Image.SCALE_SMOOTH),
                0, 0, null
        );

        return thumbnail;
    }

    private Path saveFile(byte[] data, String directory, String fileName) throws IOException {
        Path directoryPath = Paths.get(directory);
        Files.createDirectories(directoryPath);

        Path filePath = directoryPath.resolve(fileName);
        return Files.write(filePath, data);
    }

    private void saveThumbnail(BufferedImage thumbnail, String directory, String fileName) throws IOException {
        Path directoryPath = Paths.get(directory);
        Files.createDirectories(directoryPath);

        Path thumbnailPath = directoryPath.resolve(fileName);
        ImageIO.write(thumbnail, "JPEG", thumbnailPath.toFile());
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    public long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }

    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}
