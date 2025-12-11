package com.raulmbueno.mini_ecommerce.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.raulmbueno.mini_ecommerce.entities.Brand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class BrandLogoStorageService {

    // Lidos do application-*.properties ou de variáveis de ambiente
    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    /**
     * Faz upload da logo da marca para o Cloudinary e devolve a URL segura (https).
     */
    public String storeBrandLogo(Brand brand, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de logo vazio.");
        }

        // Garante que as credenciais foram configuradas
        if (isBlank(cloudName) || isBlank(apiKey) || isBlank(apiSecret)) {
            throw new IllegalStateException(
                "Cloudinary não configurado. Defina as propriedades " +
                "cloudinary.cloud-name, cloudinary.api-key e cloudinary.api-secret."
            );
        }

        try {
            // Cria o client do Cloudinary
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
            ));

            // public_id fixo por marca → se reenviar, substitui a logo
            String publicId = "brand-" + brand.getId();

            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "remakeupstore/brands", // pasta lógica no Cloudinary
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
                )
            );

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new RuntimeException("Cloudinary não retornou secure_url ao fazer upload da logo.");
            }

            return secureUrl.toString();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar logo para o Cloudinary.", e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
