SELECT * FROM bank;

SELECT * FROM bank WHERE geschaeftssitz = 'Hamburg';

SELECT mname, wohnort, anstellungsdatum FROM mitarbeiter WHERE anstellungsdatum >= '1998-04-28' ORDER BY mname;

SELECT mname, wohnort, bname, blz FROM mitarbeiter, bank WHERE monatsgehalt < 2500 AND arbeitgeber_bank = bname;

SELECT mname, wohnort, bname, blz FROM mitarbeiter JOIN bank ON arbeitgeber_bank = bname WHERE monatsgehalt < 2500;

SELECT kontonr, kontoart FROM konto, unterhaelt, kunde WHERE kontonr = kontonummer AND inhaber = kname AND betreuer = 'Olafson';

SELECT kname, kontonummer, guthaben FROM konto, unterhaelt, kunde, mitarbeiter, bank 
WHERE kontoart = 'Tagesgeld' AND kontonr = kontonummer AND geschaeftssitz = 'Frankfurt' 
AND inhaber = kname AND betreuer = mname AND arbeitgeber_bank = bname ORDER BY kname, kontonummer;

SELECT kname, kontonr, guthaben FROM kunde, konto, unterhaelt, filiale, bank
WHERE inhaber = kname AND kontoart = 'Tagesgeld' AND mutterbank = bname AND geschaeftssitz = 'Frankfurt' 
AND kontonr = kontonummer AND zustaendige_Filiale = fname
ORDER BY kname, kontonr;

SELECT COUNT (*) AS anz_mitarbeiter_aus_Hamburg FROM mitarbeiter WHERE wohnort = 'Hamburg';

SELECT mname, monatsgehalt FROM mitarbeiter WHERE monatsgehalt > (SELECT AVG(monatsgehalt) FROM mitarbeiter);

SELECT mname, arbeitgeber_bank FROM mitarbeiter WHERE mname IN (SELECT betreuer FROM kunde WHERE wohnort = 'Leipzig');

SELECT kontonr, guthaben FROM konto WHERE kontoart = 'Giro' 
AND anlage_Filiale IN (SELECT fname FROM filiale WHERE mutterbank = 'BlackBank') ORDER BY kontonr DESC;

SELECT kontonr, guthaben FROM konto, filiale WHERE kontoart = 'Giro' AND anlage_Filiale = fname 
AND mutterbank = 'BlackBank' ORDER BY kontonr DESC;