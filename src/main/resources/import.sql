INSERT INTO CLIENTE  (codigo,nome)	VALUES (1,'Pedro Silva');
INSERT INTO CLIENTE  (codigo,nome)	VALUES (2,'Mariane Silveira');

INSERT INTO LOJA     (codigo,nome) VALUES (1,'Nome da Loja 1');
INSERT INTO LOJA     (codigo,nome) VALUES (2,'Nome da Loja 2');

INSERT INTO VENDEDOR (codigo,nome,loja_id,tipo)	VALUES (1,'master',1,'COMISSIONADO');
INSERT INTO VENDEDOR (codigo,nome,loja_id,tipo)	VALUES (2,'maria',1,'DEFAULT');
INSERT INTO VENDEDOR (codigo,nome,loja_id,tipo)	VALUES (3,'joao',1,'COMISSIONADO');

INSERT INTO USUARIO  (codigo,nome,perfil,senha,email)	VALUES (1,'master','ADMINISTRADOR','123456','chmulato@hotmail.com');
INSERT INTO USUARIO  (codigo,nome,perfil,senha,email)	VALUES (2,'maria','USUARIO','1234','chmulato@hotmail.com');
INSERT INTO USUARIO  (codigo,nome,perfil,senha,email)	VALUES (3,'joao','VISITANTE','1234','chmulato@hotmail.com');

INSERT INTO PRODUTO  (codigo,descricao,valor) VALUES (1,'CamisaPolo',80.9);
INSERT INTO PRODUTO  (codigo,descricao,valor) VALUES (2,'CamisaSocial',134.9);
INSERT INTO PRODUTO  (codigo,descricao,valor) VALUES (3,'ShortEsporte',40.9);

INSERT INTO VINHO    (codigo,nome,tipo,valor) VALUES (1,'Coracao_De_Leao','TINTO',18.9);
INSERT INTO VINHO    (codigo,nome,tipo,valor) VALUES (2,'Argentino','ROSE',14.9);

UPDATE VENDEDOR SET usuario_id=2 WHERE codigo=2;

