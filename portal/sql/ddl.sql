CREATE TABLE usuario (
  email VARCHAR(150) NOT NULL,
  nome VARCHAR(150) NOT NULL,
  senha VARCHAR(50) NOT NULL,
  PRIMARY KEY(email)
);

/*
alter table usuario 
modify nome varchar(150)
 */

CREATE TABLE endereco_usuario (
  id BIGINT NOT NULL AUTO_INCREMENT primary key,
  usuario_email VARCHAR(150) NOT NULL,
  logradouro VARCHAR(100) NULL,
  bairro VARCHAR(50) NULL,
  numero INTEGER UNSIGNED NULL,
  complemento VARCHAR(20) NULL,
  cidade VARCHAR(50) NULL,
  estado VARCHAR(50) NULL,
  cep VARCHAR(20) NULL,
  INDEX (usuario_email),
  FOREIGN KEY (usuario_email)
  REFERENCES usuario(email)  
);

