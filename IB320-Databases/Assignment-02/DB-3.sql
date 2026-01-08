SELECT DISTINCT biergarten_ort FROM besucht, gast 
WHERE nname = besucher_nname AND vname = besucher_vname AND lieblingsgetraenk = 'Pils';

SELECT biergarten_ort, round(AVG(preisproliter), 2) AS avg FROM getraenk, schenkt_aus WHERE getraenke_name = gname AND alkogehalt > 0 GROUP BY biergarten_ort;

SELECT biergarten_ort, round(AVG(preisproliter), 2) AS avg FROM getraenk, schenkt_aus WHERE getraenke_name = gname AND alkogehalt > 0 GROUP BY biergarten_ort
HAVING SUM(ausschankprotag) > 1500;

SELECT bname FROM biergarten WHERE umsatzprojahr > (SELECT umsatzprojahr FROM biergarten WHERE bname = 'Zur letzten Instanz' AND ort = 'Vilsbiburg') ORDER BY bname DESC;

SELECT DISTINCT vname || ' ' || nname AS name, lieblingsgetraenk FROM gast, besucht WHERE besucher_vname = vname AND besucher_nname = nname AND biergarten_name > 'Firmstube'
ORDER BY name;

SELECT DISTINCT vname || ' ' || nname AS name, lieblingsgetraenk FROM gast, besucht WHERE besucher_vname = vname AND besucher_nname = nname 
AND biergarten_name > ALL (SELECT biergarten_name FROM schenkt_aus WHERE getraenke_name = 'Lauwasser') ORDER BY name;

SELECT DISTINCT biergarten_name FROM schenkt_aus WHERE biergarten_name NOT IN (SELECT biergarten_name FROM getraenk, schenkt_aus WHERE gname = getraenke_name AND alkogehalt > 0)
ORDER BY biergarten_name;

SELECT DISTINCT biergarten_name FROM schenkt_aus, getraenk WHERE gname = getraenke_name AND alkogehalt = 0 ORDER BY biergarten_name;

SELECT DISTINCT biergarten_name FROM schenkt_aus WHERE biergarten_name NOT IN 
(SELECT biergarten_name FROM getraenk, schenkt_aus, firma WHERE gname = getraenke_name AND hersteller = fname AND standort = 'Neuenahr') ORDER BY biergarten_name;

SELECT DISTINCT biergarten_name FROM schenkt_aus, firma, getraenk WHERE gname = getraenke_name AND hersteller = fname AND standort != 'Neuefahr' ORDER BY biergarten_name;

SELECT gname FROM getraenk EXCEPT SELECT lieblingsgetraenk FROM gast;

SELECT gname FROM getraenk WHERE gname NOT IN (SELECT lieblingsgetraenk FROM gast);

SELECT gname FROM getraenk WHERE NOT EXISTS (SELECT lieblingsgetraenk FROM gast WHERE gname = lieblingsgetraenk);

/*CREATE TABLE ortsstatistik (ortsname char(20), anz_biergaerten int, avgumsatz decimal(8, 2), primary key(ortsname));*/

/*INSERT INTO ortsstatistik (SELECT ort, COUNT(bname), avg(umsatzprojahr) FROM biergarten GROUP BY ort);*/

SELECT * FROM ortsstatistik;

/*UPDATE biergarten SET anzahlplaetze = (SELECT MAX(anzahlplaetze) FROM biergarten) WHERE bname IN 
(SELECT biergarten_name FROM besucht, gast WHERE besucher_nname = nname AND besucher_vname = vname AND gebdatum < '1945-01-01');*/

SELECT * FROM biergarten;

/*DELETE FROM schenkt_aus WHERE getraenke_name IN (SELECT gname FROM getraenk WHERE hersteller NOT IN (SELECT DISTINCT hersteller from getraenk, gast WHERE gname = lieblingsgetraenk));*/

/*DELETE FROM getraenk WHERE hersteller NOT IN (SELECT DISTINCT hersteller from getraenk, gast WHERE gname = lieblingsgetraenk);*/

/*DELETE FROM firma where fname NOT IN (SELECT DISTINCT hersteller FROM getraenk);*/

SELECT * FROM firma;