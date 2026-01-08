-- Creates fuer Supermarkt

create sequence bestellid;

create table Kategorie (
   katid int,
   kname char(15),
   primary key (katid)
);
 
create table Lieferant (
   liefid int,
   lname char(15),
   standort char(15),
   primary key (liefid)
);

create table Ware (
   warenid int,
   bezeichnung char(15),
   preis decimal (6,2),
   vorrat int,
   katid int not null,
   primary key (warenid),
   foreign key (katid) references Kategorie
);

create table Kunde (
   kundeid int,
   kname char(15),
   wohnort char(15),
   primary key (kundeid)
);

create table Bestellung (
   bestid int,
   datum date,
   status char(2),
   kundid int,
   primary key (bestid),
   foreign key (kundid) references kunde
);

create table Liefert (
   liefid int,
   warenid int,
   primary key (liefid, warenid),
   foreign key (liefid) references Lieferant,
   foreign key (warenid) references Ware
);

create table Enthaelt (
   bestid int,
   warenid int,
   anzahl int,
   primary key (bestid, warenid),
   foreign key (bestid) references Bestellung,
   foreign key (warenid) references Ware
);   