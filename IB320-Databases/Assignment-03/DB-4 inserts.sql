-- Inserts fuer Supermarkt

insert into Kategorie values (1, 'Kleidung');
insert into Kategorie values (2, 'Lebensmittel');
insert into Kategorie values (3, 'Outdoor');

insert into Lieferant values (1, 'Boss', 'Milano');
insert into Lieferant values (2, 'Schoeffel', 'Frankfurt');
insert into Lieferant values (3, 'Kraft', 'Hannover');
insert into Lieferant values (4, 'Nestle', 'Milano');
insert into Lieferant values (5, 'Vaude', 'Stuttgart');
insert into Lieferant values (6, 'Benetton', 'Milano');

insert into Ware values (1, 'Anorak', 69.95, 50, 3);
insert into Ware values (2, 'Flatterhose', 64.95, 50, 1);
insert into Ware values (3, 'Kakao', 3.45, 50, 2);
insert into Ware values (4, 'Tee', 1.55, 50, 2);
insert into Ware values (5, 'Wurst', 0.99, 50, 2);
insert into Ware values (6, 'Kaese', 1.44, 50, 2);
insert into Ware values (7, 'Pulli', 70.05, 50, 1);
insert into Ware values (8, 'Zelt', 269.00, 50, 3);
insert into Ware values (9, 'Muetze', 4.75, 50, 1);
insert into Ware values (10, 'Butter', 1.12, 50, 2);
insert into Ware values (11, 'Kaffee', 8.35, 50, 2);
insert into Ware values (12, 'Hut', 20.45, 50, 1);
insert into Ware values (13, 'Schuh', 149.99, 50, 1);
insert into Ware values (14, 'Wanderstiefel',234.59, 50, 3);
insert into Ware values (15, 'Regenjacke', 34.55, 50, 3);

insert into Kunde values (1, 'Pythagoras', 'Samos');
insert into Kunde values (2, 'Euklid', 'Athen');
insert into Kunde values (3, 'Sokrates', 'Athen');
insert into Kunde values (4, 'Platon', 'Athen');
insert into Kunde values (5, 'Aristoteles', 'Athen');
insert into Kunde values (6, 'Thales', 'Milet');
insert into Kunde values (7, 'Perikles', 'Athen');
insert into Kunde values (8, 'Alkibiades', 'Athen');
insert into Kunde values (9, 'Thukydides', 'Athen');
insert into Kunde values (10, 'Leonidas', 'Sparta');

insert into Liefert values (1, 1);
insert into Liefert values (1, 2);
insert into Liefert values (1, 9);
insert into Liefert values (1, 12);
insert into Liefert values (1, 13);
insert into Liefert values (2, 1);
insert into Liefert values (2, 8);
insert into Liefert values (2, 14);
insert into Liefert values (2, 15);
insert into Liefert values (3, 3);
insert into Liefert values (3, 4);
insert into Liefert values (3, 6);
insert into Liefert values (3, 10);
insert into Liefert values (4, 3);
insert into Liefert values (4, 5);
insert into Liefert values (4, 6);
insert into Liefert values (4, 11);
insert into Liefert values (5, 1);
insert into Liefert values (5, 8);
insert into Liefert values (5, 13);
insert into Liefert values (5, 14);
insert into Liefert values (5, 15);
insert into Liefert values (6, 2);
insert into Liefert values (6, 9);
insert into Liefert values (6, 12);

-- keine Inserts fuer Bestellung und Enthaelt,
-- denn Bestellungen muessen erst noch getaetigt werden