# DESCRIZIONE DEL PROGETTO KMeans
Nel caso di studio denominato **“K-MEANS”** si è progettato e realizzato un sistema **Client-Server**.
Il **Client** consiste in un applicativo Java che consente di usufruire del servizio di scoperta remoto e di visualizzare la conoscenza 
scoperta (cioè cluster di dati). In particolare è stato implementato il metodo `init` attraverso cui il sistema Client è in grado di 
collegarsi al Server tramite l’indirizzo Ip (acquisito tramite il metodo `getParameter`) e il numero di porta su cui il Server è in ascolto. 
Una volta instaurata la connessione l’utente utilizzando l’interfaccia grafica può effettuare due scelte:
* avviare un nuovo processo di clustering (implementazione del metodo `learningFromDBAction`): il Client effettua richieste al Server per 
eseguire un’attività di scoperta dei cluster di dati sulla base di dati, rimanendo in attesa dei risultati da parte del Server ottenuti per 
mezzo dell’algoritmo kmeans. Se tutto procede correttamente verranno visualizzati il risultato dell’algoritmo kmeans con i relativi cluster 
scoperti. Inoltre questi verranno salvati su un file il cui nome deriverà dalla concatenazione tra il nome della tabella che si è scelti 
nel database e il numero di cluster scoperti. In caso contrario verrà visualizzato un messaggio di errore.

* recuperare cluster precedentemente serializzati in un file (implementazione del metodo `learningFromFileAction`): il Client effettua 
richieste al Server per effettuare una lettura da file contenente attività precedenti di scoperta dei cluster di dati sulla base di dati. 
Nel caso in cui tale file esista verrà visualizzato il contenuto corrispondente, altrimenti verrà visualizzato un messaggio di errore.

Il **Server** acquisisce le richieste effettuate da parte del Client e gli invia le rispettive risposte. In particolare è stato 
implementato il metodo `run` che si occupa di gestire le richieste del Client. Inoltre il Server include funzionalità di data mining che 
permettono la scoperta di cluster di dati. Tra le varie funzionalità implementate ci sono: il metodo `initConnection` che impartisce al 
class loader l’ordine di caricare il driver mysql e inizializza la connessione con il database; il metodo `getDistinctTransazioni` avente 
come parametro di input il nome della tabella scelta dall’utente nel Client e che restituisce la lista di transazioni distinte (ottenute 
attraverso un’interrogazione sulla tabella presente nella base di dati) memorizzate nella tabella; il metodo kmeans che esegue l’algoritmo 
del k-means e avente come parametro di input la tabella scelta presente nella base di dati su cui eseguire l’algoritmo del k-means che 
segue i seguenti passi:

1. Scelta casuale di centroidi per k clusters (implementazione del metodo `initializeCentroids` avente come parametro di input la tabella 
in cui scegliere i centroidi)

2. Assegnazione di ciascuna riga della tabella in data al cluster avente centroide più vicino all'esempio (implementazione del metodo 
`nearestCluster` avente come parametro di input il riferimento ad un oggetto Tuple e che restituisce il cluster più vicino alla tupla 
passata e del metodo `currentCluster` avente come parametro di input l’indice di una riga della tabella e che restituisce il cluster a cui 
appartiene la tupla identificata dall’indice di riga)

3. Calcolo dei nuovi centroidi per ciascun cluster (implementazione del metodo `updateCentroids` avente come parametro di input la tabella 
da cui calcolare i centroidi)

4. Ripete i passi 2 e 3 finché due iterazioni consecutive non restituiscono centroidi uguali

Un altro metodo implementato è `salva` avente come parametro di input il nome del file su cui salvare le attività di scoperta. I file 
salvati potranno essere letti successivamente attraverso l’implementazione del costruttore <span style="text-decoration:underline">KmeansMiner</span> avente come 
parametro di input il nome del file da leggere.
Inoltre è stato implementato il metodo `avgDistance` avente come parametri di input il riferimento ad un oggetto Data e un insieme di 
indici di righe presenti nella tabella scelta dall’utente nel Client e che restituisce la media delle distanze tra la tupla corrente e 
quelle ottenibili dalle tuple indicate dagli indici di righe presenti nella tabella. All’interno di questo metodo viene richiamato il 
metodo `getDistance` avente come parametro di input una tupla da confrontare con la tupla corrente e che restituisce la distanza tra la 
tupla passata in input e la tupla corrente.
