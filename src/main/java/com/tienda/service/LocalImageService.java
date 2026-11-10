package com.tienda.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalImageService {
    private static final long MAX_BYTES = 2 * 1024 * 1024;
    private static final Set<String> EXTENSIONES = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private final Path uploadRoot;

    public LocalImageService(@Value("${techshop.upload-dir:./uploads}") String uploadDir) {
        uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String guardar(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) return null;
        if (imagen.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("La imagen debe pesar menos de 2 MB.");
        }
        String fileName = imagen.getOriginalFilename();
        String extension = fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT) : "";
        if (!EXTENSIONES.contains(extension)
                || imagen.getContentType() == null
                || !imagen.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Usá una imagen JPG, PNG, GIF o WEBP.");
        }
        String nombreSeguro = UUID.randomUUID() + "." + extension;
        try {
            Files.createDirectories(uploadRoot);
            Path destino = uploadRoot.resolve(nombreSeguro);
            Files.copy(imagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + nombreSeguro;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar la imagen en la carpeta local.", ex);
        }
    }
}
