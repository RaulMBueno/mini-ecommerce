-- Prioridade na home (maior = aparece primeiro). Default 0.
ALTER TABLE tb_product ADD COLUMN home_priority INT NOT NULL DEFAULT 0;

-- Data de criação para ordenação "mais recente primeiro"
ALTER TABLE tb_product ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();
UPDATE tb_product SET created_at = now() WHERE created_at IS NULL;
ALTER TABLE tb_product ALTER COLUMN created_at SET NOT NULL;
