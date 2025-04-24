DELIMITER //

CREATE PROCEDURE sp_cadastrar_cliente(
    IN p_nome VARCHAR(100),
    IN p_telefone VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_tipo ENUM('NACIONAL', 'ESTRANGEIRO'),
    IN p_documento VARCHAR(20)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    IF p_nome IS NULL OR p_telefone IS NULL OR p_email IS NULL OR p_tipo IS NULL OR p_documento IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Todos os parâmetros são obrigatórios';
    END IF;
    
    IF p_email NOT REGEXP '^[^@]+@[^@]+\.[^@]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Formato de e-mail inválido';
    END IF;
    
    IF p_tipo = 'NACIONAL' THEN
        IF p_documento NOT REGEXP '^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Formato de CPF inválido. Use XXX.XXX.XXX-XX';
        END IF;
        
        INSERT INTO Cliente (nome, telefone, email, cpf, passaporte, tipo)
        VALUES (p_nome, p_telefone, p_email, p_documento, NULL, p_tipo);
    ELSE
        IF LENGTH(p_documento) < 5 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Passaporte deve ter pelo menos 5 caracteres';
        END IF;
        
        INSERT INTO Cliente (nome, telefone, email, cpf, passaporte, tipo)
        VALUES (p_nome, p_telefone, p_email, NULL, p_documento, p_tipo);
    END IF;
    
    COMMIT;
END //

CREATE PROCEDURE sp_criar_pedido(
    IN p_cliente_id INT,
    IN p_pacote_id INT,
    IN p_data_viagem DATE,
    IN p_servicos_json JSON,
    OUT p_pedido_id INT
)
BEGIN
    DECLARE v_pacote_preco DECIMAL(10,2);
    DECLARE v_pacote_ativo BOOLEAN;
    DECLARE v_servico_count INT;
    DECLARE i INT DEFAULT 0;
    DECLARE v_servico_id INT;
    DECLARE v_quantidade INT;
    DECLARE v_servico_ativo BOOLEAN;
    DECLARE v_servico_preco DECIMAL(10,2);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    IF p_data_viagem < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data da viagem não pode ser no passado';
    END IF;
    
    SELECT preco, ativo INTO v_pacote_preco, v_pacote_ativo
    FROM PacoteViagem
    WHERE id = p_pacote_id;
    
    IF v_pacote_preco IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Pacote de viagem não encontrado';
    END IF;
    
    IF NOT v_pacote_ativo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Pacote de viagem não está disponível';
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM Cliente WHERE id = p_cliente_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cliente não encontrado';
    END IF;
    
    INSERT INTO Pedido (cliente_id, pacote_id, data_viagem, valor_total, status)
    VALUES (p_cliente_id, p_pacote_id, p_data_viagem, v_pacote_preco, 'PENDENTE');
    
    SET p_pedido_id = LAST_INSERT_ID();
    
    IF p_servicos_json IS NOT NULL THEN
        SET v_servico_count = JSON_LENGTH(p_servicos_json);
        
        WHILE i < v_servico_count DO
            SET v_servico_id = JSON_EXTRACT(p_servicos_json, CONCAT('$[', i, '].servico_id'));
            SET v_quantidade = JSON_EXTRACT(p_servicos_json, CONCAT('$[', i, '].quantidade'));
            
            SELECT preco, ativo INTO v_servico_preco, v_servico_ativo
            FROM ServicoAdicional
            WHERE id = v_servico_id;
            
            IF v_servico_preco IS NULL THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Serviço não encontrado';
            END IF;
            
            IF NOT v_servico_ativo THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Serviço não disponível';
            END IF;
            
            IF v_quantidade < 1 THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Quantidade deve ser pelo menos 1';
            END IF;
            
            INSERT INTO PedidoServico (pedido_id, servico_id, quantidade, preco_unitario)
            VALUES (p_pedido_id, v_servico_id, v_quantidade, v_servico_preco);
            
            SET i = i + 1;
        END WHILE;
        
        UPDATE Pedido
        SET valor_total = (
            SELECT v_pacote_preco + COALESCE(SUM(ps.preco_unitario * ps.quantidade), 0)
            FROM PedidoServico ps
            WHERE ps.pedido_id = p_pedido_id
        )
        WHERE id = p_pedido_id;
    END IF;
    
    COMMIT;
END //

DELIMITER ;