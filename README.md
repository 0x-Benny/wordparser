# Progetto Finale EIS - Wordparser

---

## Autori

- [Bernardo Simonetto](mailto:bernardo.simonetto@studenti.unipd.it)
- [Francesco Elia Rizzo](mailto:francescoelia.rizzo@studenti.unipd.it)
- [Christian Poli](mailto:christian.poli.1@studenti.unipd.it)
- [Andres Donadi](mailto:andres.donadi@studenti.unipd.it)

## Panoramica ad alto livello

Il programma ha il compito di scaricare articoli di giornale ottenuti da varie sorgenti (online e non), i quali 
vengono processati dagli ```Adapter``` e vengono trasformati dal formato originale (JSON, CSV...) ad un oggetto custom ```BasicArticle```.
Una volta ottenuto l'array di ```BasicArticle``` ci si serve di alcune classi "tool" (```Serializer``` e ```Deserializer```) 
per costruire il Database e analizzare le parole grazie agli ```WordCountStrategy```.

## Istruzioni per installare ed eseguire

Il progetto consegnato su moodle è pronto per essere analizzato ed eseguito subito.
Si precisa però che viene utilizzata una chiave di test momentanea per comunicare con le API di TheGuardian.
È necessario quindi registrare una key all'indirizzo https://open-platform.theguardian.com/access/.

Inserire la chiave nel ```guardian.properties``` il cui path è:
```./src/main/resources/guardian.properties```

Seguono le istruzioni per compilare ed eseguire ex-novo.

Per compilare il progetto e generare il file .jar:

```
mvn package
```

Spostare dunque nella directory principale il jar, necessario per avere il corretto accesso alla cartella assets
contenente sorgenti e stoplist, che possono così essere aggiornati agevolmente:

```
mv ./target/wordparser-1.0-jar-with-dependencies.jar ./
```

Infine il comando per eseguire l'applicazione:

```
java -jar wordparser-1.0-jar-with-dependencies.jar -{d,de,e,h} <download-query>
```

Le opzioni disponibili sono:

```
-d,--download-articles <query>       Download articles from all sources.
-de,--download-and-extract <query>   Download articles and extract terms sequentially.
-e,--extract-terms                 Extract terms from internal database.
-h,--help                          Print the help.
```

Per generare il site del progetto:

```
mvn site
```

## Indicazioni sulle librerie

Per i dettagli esaustivi sulla [scelta delle librerie](./site/dependencies.html) e il loro [utilizzo all'interno
delle classi](./site/apidocs/index.html) si rimanda alla documentazione ufficiale prodotta con JavaDoc.

Come si evince dal [POM](./pom.xml) sono state usate:

- ```commons-cli | v1.5.0``` per la gestione delle opzioni della command line;
- ```com.fasterxml.jackson.core | v2.15.1``` per la serializzazione e lettura di JSON, CSV e produzione del Database;
- ```com.squareup.okhttp3 | v4.11.0``` per la gestione delle chiamate alle web API;
- ```org.jsoup | v1.16.1``` per sanitizzare e ripulire il codice HTML;
- ```org.apache.commons | v3.12.0``` per avvalersi del suo comodo ArrayUtils.addAll();
- ```com.github.stefanbirkner | v1.2.1``` per intercettare il SecurityManager di Java nei test;
- ```org.junit.jupiter | v5.9.2``` per testare il codice con la versione più recente di JUnit.