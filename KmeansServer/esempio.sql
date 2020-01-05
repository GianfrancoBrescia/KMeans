# 1)creare un database con nome MapDB
CREATE DATABASE MapDB;
commit;

# 2)creare l'utente "MapUser" con password "map" che sia dotato dei diritti di accesso in lettura e #scrittura per mapDB
CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT CREATE, SELECT, INSERT, DELETE ON MapDB.* TO MapUser@localhost IDENTIFIED BY 'map';

# 3)creare e popolare la tabella denominata esempio
CREATE TABLE MapDB.esempio(outlook varchar(10), temperature varchar(10), umidity varchar(10), wind varchar(10), play varchar(10));

insert into MapDB.esempio values('sunny', 'hot', 'high', 'weak', 'no');
insert into MapDB.esempio values('sunny', 'hot', 'high', 'strong', 'no');
insert into MapDB.esempio values('overcast', 'hot', 'high', 'weak', 'yes');
insert into MapDB.esempio values('rain', 'mild', 'high', 'weak', 'yes');
insert into MapDB.esempio values('rain', 'cool', 'normal', 'weak', 'yes');
insert into MapDB.esempio values('rain', 'cool', 'normal', 'strong', 'no');
insert into MapDB.esempio values('overcast', 'cool', 'normal', 'strong', 'yes');
insert into MapDB.esempio values('sunny', 'mild', 'high', 'weak', 'no');
insert into MapDB.esempio values('sunny', 'cool', 'normal', 'weak', 'yes');
insert into MapDB.esempio values('rain', 'mild', 'normal', 'weak', 'yes');
insert into MapDB.esempio values('sunny', 'mild', 'normal', 'strong', 'yes');
insert into MapDB.esempio values('overcast', 'mild', 'high', 'strong', 'yes');
insert into MapDB.esempio values('overcast', 'hot', 'normal', 'weak', 'yes');
insert into MapDB.esempio values('rain', 'mild', 'high', 'strong', 'no'); 
commit; 