package com.raulmbueno.mini_ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Salva a logo de uma marca e retorna a URL pública que será gravada no banco.
     * Exemplo de retorno em dev: /uploads/brand-logos/brand-12-123456.png
     */
    String saveBrandLogo(Long brandId, MultipartFile file);

    /**
     * (Opcional) Deleta um arquivo a partir da URL salva no banco.
     * Podemos implementar depois quando você quiser permitir troca/remoção completa.
     */
    void delete(String url);
}
