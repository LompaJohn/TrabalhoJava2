CREATE DATABASE IF NOT EXISTS agencia_viagens;
USE agencia_viagens;

CREATE TABLE Cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    cpf VARCHAR(14),
    passaporte VARCHAR(20),
    tipo ENUM('NACIONAL', 'ESTRANGEIRO') NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_cpf UNIQUE (cpf),
    CONSTRAINT uk_passaporte UNIQUE (passaporte),
    
    CHECK (
        (tipo = 'NACIONAL' AND cpf IS NOT NULL AND passaporte IS NULL) OR
        (tipo = 'ESTRANGEIRO' AND passaporte IS NOT NULL AND cpf IS NULL)
    )
);

CREATE TABLE PacoteViagem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,

    CHECK (preco > 0),
    CHECK (data_fim > data_inicio)
);

CREATE TABLE ServicoAdicional (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    
    CHECK (preco >= 0)
);

CREATE TABLE Pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    pacote_id INT NOT NULL,
    data_contratacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_viagem DATE,
    valor_total DECIMAL(10,2) NOT NULL,
    status ENUM('PENDENTE', 'CONFIRMADO', 'CANCELADO') DEFAULT 'PENDENTE',
    
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    FOREIGN KEY (pacote_id) REFERENCES PacoteViagem(id),
    
    CHECK (valor_total >= 0)
);

CREATE TABLE PedidoServico (
    pedido_id INT NOT NULL,
    servico_id INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    preco_unitario DECIMAL(10,2) NOT NULL,
    
    PRIMARY KEY (pedido_id, servico_id),
    FOREIGN KEY (pedido_id) REFERENCES Pedido(id),
    FOREIGN KEY (servico_id) REFERENCES ServicoAdicional(id),
    
    CHECK (quantidade > 0),
    CHECK (preco_unitario >= 0)
);

CREATE INDEX idx_cliente_nome ON Cliente(nome);
CREATE INDEX idx_pacote_destino ON PacoteViagem(destino);
CREATE INDEX idx_pedido_data ON Pedido(data_viagem);