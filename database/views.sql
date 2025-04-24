CREATE VIEW vw_clientes_completos AS
SELECT id,
       nome,
       telefone,
       email,
       tipo,
       COALESCE(cpf, passaporte)          AS documento,
       data_cadastro,
       CASE
           WHEN EXISTS (SELECT 1 FROM Pedido WHERE cliente_id = Cliente.id) THEN 'CLIENTE_ATIVO'
           ELSE 'CLIENTE_INATIVO'
           END                            AS status_cliente,
       DATEDIFF(CURDATE(), data_cadastro) AS dias_cadastrado
FROM Cliente;

CREATE VIEW vw_pacotes_com_status AS
SELECT p.id,
       p.nome,
       p.destino,
       p.preco,
       p.tipo,
       p.ativo,
       COUNT(pe.id) AS total_pedidos,
       CASE
           WHEN COUNT(pe.id) > 5 THEN 'POPULAR'
           WHEN COUNT(pe.id) > 0 THEN 'NORMAL'
           ELSE 'SEM_PEDIDOS'
           END      AS status_vendas,
       CASE
           WHEN p.ativo = FALSE THEN 'INDISPONIVEL'
           WHEN p.data_inicio > CURDATE() THEN 'AGUARDANDO_INICIO'
           WHEN p.data_fim < CURDATE() THEN 'ENCERRADO'
           ELSE 'DISPONIVEL'
           END      AS status_disponibilidade
FROM PacoteViagem p
         LEFT JOIN Pedido pe ON p.id = pe.pacote_id
GROUP BY p.id;

CREATE VIEW vw_pedidos_detalhados AS
SELECT pe.id                                                                  AS pedido_id,
       c.id                                                                   AS cliente_id,
       c.nome                                                                 AS cliente_nome,
       c.tipo                                                                 AS cliente_tipo,
       p.id                                                                   AS pacote_id,
       p.nome                                                                 AS pacote_nome,
       p.destino,
       pe.data_contratacao,
       pe.data_viagem,
       pe.valor_total                                                         AS valor_pacote,
       COALESCE(SUM(ps.preco_unitario * ps.quantidade), 0)                    AS valor_servicos,
       (pe.valor_total + COALESCE(SUM(ps.preco_unitario * ps.quantidade), 0)) AS valor_total,
       pe.status,
       DATEDIFF(pe.data_viagem, CURDATE())                                    AS dias_restantes,
       CASE
           WHEN pe.status = 'CANCELADO' THEN 'CANCELADO'
           WHEN pe.data_viagem < CURDATE() THEN 'CONCLUIDO'
           WHEN DATEDIFF(pe.data_viagem, CURDATE()) <= 7 THEN 'PROXIMO'
           ELSE 'AGENDADO'
           END                                                                AS status_viagem
FROM Pedido pe
         JOIN Cliente c ON pe.cliente_id = c.id
         JOIN PacoteViagem p ON pe.pacote_id = p.id
         LEFT JOIN PedidoServico ps ON pe.id = ps.pedido_id
GROUP BY pe.id;