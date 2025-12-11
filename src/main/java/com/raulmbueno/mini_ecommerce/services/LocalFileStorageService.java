package com.raulmbueno.mini_ecommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class LocalFileStorageService implements FileStorageService {

    // Pasta base onde os arquivos serão salvos em DEV
    private final Path rootLocation;

    public LocalFileStorageService() {
        // uploads/brand-logos (relativo à raiz do projeto)
        this.rootLocation = Paths.get("uploads/brand-logos").toAbsolutePath().normalize();

        try {
            // Garante que a pasta exista
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar diretório de upload: " + rootLocation, e);
        }
    }

    @Override
    public String saveBrandLogo(Long brandId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo de logo vazio");
        }

        try {
            // Descobre a extensão (png/jpg)
            String originalName = file.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            } else {
                // fallback: se não tiver extensão, usa .png
                extension = ".png";
            }

            // Nome do arquivo: brand-12-1699999999999.png
            String filename = "brand-" + brandId + "-" + System.currentTimeMillis() + extension;

            Path destinationFile = this.rootLocation.resolve(filename);

            // Salva / sobrescreve o arquivo
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // URL pública que o front vai usar.
            // Depois faremos o mapeamento /uploads/** no Spring
            return "/uploads/brand-logos/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo de logo", e);
        }
    }

    @Override
    public void delete(String url) {
        // Implementamos depois, quando você quiser realmente apagar arquivos antigos
    }
}
