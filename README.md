# Telefonski imenik #

### Namen repozitorija ###

Telefonski imenik je projekt v Javi pri predmetu Programiranje 2, š.leto 2014/2015. Cilj projekta je ustvariti program, ki bo uporabniku pomagal pri shranjevanju osebnih telefonskih številk. Imenik bo podpiral tudi vnos naslova in kraja prebivališča. Uporabljena bo Java 7, za shranjevanje podatkov pa SQLite.


### Vsebina repozitorija ###

Repozitorij vsebuje: 

* src
	* baza: vsebuje `Baza.java`, ki skrbi za komunikacijo z bazo
	* grafika
		* `GlavnoOkno.java`: lupina glavnega okna programa
		* `VsebinskoPodrocje.java`: vsa vsebina glavnega okna
		* `NovKontakt.java`: pomožno okno, ki se odpre ob dodajanju/urejanju kontakta
	* `TelefonskiImenik.java`: glavna datoteka, s pomočjo katere lahko poženemo program
* lib: vsebuje knjižnico za sqlite
* `imenik.db`: baza, v kateri so shranjeni uporabnikovi podatki