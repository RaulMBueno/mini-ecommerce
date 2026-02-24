-- Regra: produto afiliado não possui preço. Limpa dados antigos.
UPDATE tb_product SET price = NULL WHERE type = 'AFFILIATE';
