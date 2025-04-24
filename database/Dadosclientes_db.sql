INSERT INTO Cliente (nome, telefone, email, cpf, passaporte, tipo)
VALUES ('Carlos Oliveira', '(31) 98765-4321', 'carlos.oliveira@email.com', '111.222.333-44', NULL, 'NACIONAL'),
       ('Ana Beatriz Costa', '(51) 99876-5432', 'ana.costa@email.com', '555.666.777-88', NULL, 'NACIONAL'),
       ('Robert Johnson', '(1) 555-1234', 'robert.j@email.com', NULL, 'US98765432', 'ESTRANGEIRO'),
       ('Luisa Mendes', '(11) 91234-5678', 'luisa.m@email.com', '999.888.777-66', NULL, 'NACIONAL'),
       ('Yuki Tanaka', '(81) 90-1234-5678', 'yuki.t@email.com', NULL, 'JP12345678', 'ESTRANGEIRO');


INSERT INTO PacoteViagem (nome, destino, data_inicio, data_fim, preco, tipo)
VALUES ('Exploração Pantanal', 'Mato Grosso do Sul', '2025-02-01', '2025-02-10', 2800.00, 'AVENTURA'),
       ('Resort Premium em Gramado', 'Gramado, RS', '2025-03-15', '2025-03-20', 8500.00, 'LUXO'),
       ('Rota das Cidades Históricas', 'Ouro Preto, Tiradentes, Mariana', '2025-04-10', '2025-04-16', 4200.00,
        'CULTURAL'),
       ('Paraíso nas Bahamas', 'Nassau, Bahamas', '2025-05-01', '2025-05-09', 22000.00, 'ROMANTICO'),
       ('Trilhas no Monte Roraima', 'Roraima', '2025-06-20', '2025-06-27', 3800.00, 'AVENTURA'),
       ('Gastronomia Italiana', 'Roma, Florença, Bolonha', '2025-07-05', '2025-07-15', 18000.00, 'CULTURAL');

INSERT INTO ServicoAdicional (nome, preco, descricao)
VALUES ('Seguro Viagem', 120.00, 'Cobertura básica para imprevistos'),
       ('Massagem Relaxante', 250.00, 'Sessão de 60 minutos no spa do hotel'),
       ('City Tour Privativo', 400.00, 'Passeio personalizado pela cidade'),
       ('Upgrade de Quarto', 300.00, 'Diária para suíte superior'),
       ('Wi-Fi Internacional', 50.00, 'Chip com 5GB de internet no exterior');

INSERT INTO Pedido (cliente_id, pacote_id, data_viagem, valor_total, status)
VALUES (1, 2, '2024-03-15', 8500.00, 'CONFIRMADO'),
       (2, 6, '2024-05-10', 18000.00, 'PENDENTE'),
       (3, 4, '2024-07-22', 22000.00, 'CONFIRMADO'),
       (4, 1, '2024-04-05', 2800.00, 'CANCELADO'),
       (5, 3, '2024-09-12', 4200.00, 'CONFIRMADO');

INSERT INTO PedidoServico (pedido_id, servico_id, quantidade, preco_unitario)
VALUES (1, 4, 5, 300.00),
       (1, 2, 2, 250.00),
       (2, 5, 1, 50.00),
       (2, 3, 3, 400.00),
       (3, 1, 2, 120.00),
       (5, 3, 1, 400.00);