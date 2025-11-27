CREATE TABLE beneficio (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  descricao TEXT,
  valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
  ativo BOOLEAN DEFAULT TRUE,
  versao BIGINT
);
