SendGrades
==========

Překlad
-------

k sestavení se používá systém Maven

`mvn assembly:assembly`

vytvoří soubor target/SendGrades-0.1-jar-with-dependencies.jar

Použití
-------

###Přihlášení

####příkazová řádka

program umí parametry `--uco` a `--password`. Pokud jsou uvedeny, použijí se přednostně.

####password.txt

ve složce, ze které se program spouští, může být uložen soubor `password.txt`, ukázková verze je uložena zde v repozitáři. Stačí odmazat pomlčku ze začátku jména souboru a změnit na prvním řádku učo na vaše učo a heslo na vaše heslo.

####dialogové okno

pokud není možné kompletní přihlašovací údaje z příkazové řádky ani ze souboru, vyskočí při spuštění programu dialogové okno, které se na potřebné údaje zeptá.

###Odeslání známek do Bloku

`java -jar SendGrades --action grades -Dfakulta="1456" -Dobdobi="5785" -Dpredmet=705093"" -Dnbloku="vstupn%C3%AD%20test" -Dfile="0316.csv" -Dslid="1" -Dslho="2" -Dnerozlisovat_bloky="1" -Dostry="n"`

vezme soubor 0316.csv a nahraje ho do zvoleného Bloku.

`java -jar SendGrades --action grades --parameters'

	REQUIRED:
	
	-Dfakulta="" : use 1456 for ESF
	-Dobdobi="" : use 5785 for fall2012, 5786 for spring2013
	-Dpredmet="" : e.g, '705093'
	-Dnbloku="" : e.g. 'vstupn%C3%AD%20test'
	-Dfile="" : path to csv file with grades to upload
	-Dslid="" : pořadí sloupce s identifikátorem studia (čísl. od 1)
	-Dslho="" : pořadí sloupce nebo sloupců s obsahem bloku (více hodnot oddělujte mezerou
	-Dostry="" : 'n' or 'a'; n -- import pouze na zkoušku, pro kontrolu chyb, a -- import naostro
	OPTIONAL:

	-Dnerozlisovat_bloky="" : 1 -- Ignorovat změnu z původně exportovaného bloku na blok importovaný

	When in doubts what values to use for -Dpredmet and -Dnbloku, visit the uploading page in web browser

###Odeslání souboru do dokumentové složky

`java -jar SendGrades --action file -Dfile="0316.txt" -Dnazev="mujNazev" -Dpopis="mujPopis" -Dnjmeno="mojeNjmeno" -Dopt="wr" -Dfolder="/www/374368/38786227/"

uloží soubor 0316.txt do složky /www/374368/38786227/ v ISu; soubor se tam bude jmenovat mujNazev (první, červený), s alternativním názvem mojeNjmeno (zobrazuje se modře) a popisem "mujPopis" (zobrazí se po najetí myší na modrou bublinu). Pokud soubor s takovým jménem už existuje, přepíše ho.

možnosti pro -Dopt jsou:
er: ohlásit chybu, wr: přepsat, re: nový přejmenovat 
