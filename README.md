SendGrades
==========

Překlad
-------

k sestavení se používá systém Maven

`mvn assembly:assembly`

vytvoří soubor target/SendGrades-0.1-jar-with-dependencies.jar

`mv target/SendGrades-0.1-jar-with-dependencies.jar SendGrades-0.1.jar`

Použití
-------

###Parametry příkazové řádky

`java -jar SendGrades-0.1.jar --help`

	Usage: <main class> [options]
	Options:
			--action
		Acton to perform. Can be one of: 'grades' to upload grades, or 'file' to
		upload a file to document directory.
			--help
		
		Default: false
			--parameters
		Print description for all additional parameters -Dsomething for a
		selected action. Use together with '--action'.
		Default: false
			--password
		Password to use when logging into IS. It is not encouraged to provide
		password as command line argument because of security concerns.
			--uco
		UCO to use when logging into IS.
		-D
		Additional parameters for action. For example '-Dfile=znamky.csv'. Can be
		specified multiple times if there is more additional parameters to be specified.
		Syntax: -Dkey=value
		Default: {}

###Přihlášení

####příkazová řádka

program umí parametry `--uco` a `--password`. Pokud jsou uvedeny, použijí se přednostně.

####password.txt

ve složce, ze které se program spouští, může být uložen soubor `password.txt`, ukázková verze je uložena zde v repozitáři. Stačí odmazat pomlčku na začátku jména souboru a změnit na prvním řádku učo na vaše učo a na druhém řádku heslo na vaše primární heslo.

####dialogové okno

pokud není možné zjistit kompletní přihlašovací údaje z příkazové řádky ani ze souboru, vyskočí při spuštění programu dialogové okno, které se na potřebné údaje zeptá.

###Odeslání známek do Bloku

	java -jar SendGrades-0.1.jar --action grades -Dfakulta="1456" \
	-Dobdobi="5785" -Dpredmet="705093" -Dnbloku="vstupn%C3%AD%20test" \
	-Dfile="0316.txt" -Dslid="1" -Dslho="2" -Dnerozlisovat_bloky="1" -Dostry="n"

	Feb 10, 2013 10:39:20 PM org.apache.http.impl.client.DefaultRequestDirector tryExecute
	INFO: I/O exception (org.apache.http.NoHttpResponseException) caught when processing request: The target server failed to respond
	Feb 10, 2013 10:39:20 PM org.apache.http.impl.client.DefaultRequestDirector tryExecute
	INFO: Retrying request
	chyba : The file has erroneous contents. The column separator cannot be found.

vezme soubor `0316.txt` a nahraje ho do zvoleného Bloku. V tomto případě má soubor špatný formát, takže v závěrečné hlášce se ukáže chybová zpráva.

####Soupis parametrů

`java -jar SendGrades-0.1.jar --action grades --parameters`

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

	-Dnerozlisovat_bloky="" : 1 -- ignorovat změnu z původně exportovaného bloku na blok importovaný

	When in doubts what values to use for -Dpredmet and -Dnbloku, visit the uploading page in web browser and copy the values from the browser address bar.

###Odeslání souboru do dokumentové složky

	java -jar SendGrades-0.1.jar --action file -Dfile="0316.txt" \
	-Dnazev="mujNazev" -Dpopis="mujPopis" -Dnjmeno="mojeNjmeno" \
	-Dopt="wr" -Dfolder="/www/374368/38786227/"

	Feb 10, 2013 10:40:14 PM org.apache.http.impl.client.DefaultRequestDirector tryExecute
	INFO: I/O exception (org.apache.http.NoHttpResponseException) caught when processing request: The target server failed to respond
	Feb 10, 2013 10:40:14 PM org.apache.http.impl.client.DefaultRequestDirector tryExecute
	INFO: Retrying request
	potvrzeni : Saved successfully.

uloží soubor `0316.txt` do složky `/www/374368/38786227/` v ISu; soubor se tam bude jmenovat `mujNazev` (první, červený), s alternativním názvem `mojeNjmeno` (zobrazuje se modře) a popisem `mujPopis` (zobrazí se po najetí myší na modrou bublinu). Parametr `wr` značí, že pokud soubor s takovým jménem už existuje, bude přepsán.

možnosti pro -Dopt jsou:
`er`: ohlásit chybu, `wr`: přepsat, `re`: nový přejmenovat 
