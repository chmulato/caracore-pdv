INSERT INTO caracore_pdv.cliente  (codigo,cpf,nome,email)	VALUES (1,NULL,'Pedro Silva',NULL);
INSERT INTO caracore_pdv.cliente  (codigo,cpf,nome,email)	VALUES (2,NULL,'Mariane Silveira',NULL);
INSERT INTO caracore_pdv.cliente  (codigo,cpf,nome,email)	VALUES (3,'84901705920','Christian Mulato','chmulato@hotmail.com');

INSERT INTO caracore_pdv.loja     (codigo,nome) VALUES (1,'Nome da Loja 1');
INSERT INTO caracore_pdv.loja     (codigo,nome) VALUES (2,'Nome da Loja 2');

INSERT INTO caracore_pdv.vendedor (codigo,nome,loja_id,tipo)	VALUES (1,'master',1,'COMISSIONADO');
INSERT INTO caracore_pdv.vendedor (codigo,nome,loja_id,tipo)	VALUES (2,'maria',1,'DEFAULT');
INSERT INTO caracore_pdv.vendedor (codigo,nome,loja_id,tipo)	VALUES (3,'joao',1,'COMISSIONADO');

INSERT INTO caracore_pdv.operador (codigo,nome,perfil,senha,email,loja_id)	VALUES (1,'master','ADMINISTRADOR','123456','chmulato@hotmail.com',1);
INSERT INTO caracore_pdv.operador (codigo,nome,perfil,senha,email,loja_id)	VALUES (2,'maria','OPERADOR','1234','chmulato@hotmail.com',1);
INSERT INTO caracore_pdv.operador (codigo,nome,perfil,senha,email,loja_id)	VALUES (3,'joao','VISITANTE','1234','chmulato@hotmail.com',1);

INSERT INTO caracore_pdv.produto  (codigo,descricao,valor,unidade) VALUES (1,'CamisaPolo',80.9,'UNITARIO');
INSERT INTO caracore_pdv.produto  (codigo,descricao,valor,unidade) VALUES (2,'CamisaSocial',134.9,'UNITARIO');
INSERT INTO caracore_pdv.produto  (codigo,descricao,valor,unidade) VALUES (3,'ShortEsporte',40.9,'UNITARIO');