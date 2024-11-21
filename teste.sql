SELECT * FROM users;
SELECT * FROM eletro;

CREATE TABLE eletro (
id_item NUMBER PRIMARY KEY,
nome_item VARCHAR2(100) NOT NULL,
consumo_hora NUMBER NOT NULL,
valor NUMBER NOT NULL);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (1, 'Fogao', 1.2, 3.81);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (2, 'Geladeira', 0.16, 0.10);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (3, 'Forno', 2, 1.27);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (4, 'Cafeteira', 1, 0.53);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (5, 'Microondas', 2, 1.15);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (6, 'Ar', 2, 0.57);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (7, 'Som', 1, 0.10);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (8, 'Televisao', 1, 0.15);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (9, 'Lampada', 1, 0.57);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (10, 'Chuveiro', 5, 3.50);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (11, 'Secador', 2, 0.95);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (12, 'Computador', 1, 0.19);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (13, 'Aqucedor', 2, 1.27);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (14, 'Maquina de Lavar', 1, 0.63);

INSERT INTO eletro (id_item, nome_item, consumo_hora, valor)
VALUES (15, 'Maquina de Secar', 2, 1.27);


ALTER TABLE eletro
ADD pontos NUMBER DEFAULT 0;

DECLARE
    CURSOR c_itens IS
        SELECT id_item, nome_item, consumo_hora, valor
        FROM eletro;
    pontos NUMBER; -- Variável para calcular os pontos
BEGIN
    FOR item IN (SELECT id_item, valor FROM eletro) LOOP
        -- Calcular os pontos com base no valor
        UPDATE eletro
        SET pontos = valor * 10
        WHERE id_item = item.id_item;
    END LOOP;
    COMMIT; -- Salva as alterações
END;
/    









