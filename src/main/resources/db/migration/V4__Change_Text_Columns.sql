-- Aumenta o tamanho das colunas para aceitar textos e links grandes
ALTER TABLE tb_product ALTER COLUMN description TYPE TEXT;
ALTER TABLE tb_product ALTER COLUMN img_url TYPE TEXT;
ALTER TABLE tb_product ALTER COLUMN affiliate_url TYPE TEXT;