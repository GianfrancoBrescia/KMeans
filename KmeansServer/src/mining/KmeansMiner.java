package mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: KMeansMiner<br>
 * Definizione della classe KMeansMiner che si occupa di effettuare la scoperta dei cluster attraverso l'algoritmo del k-means. Inoltre 
 * questa classe si occupa sia di salvare su file le attività di scoperta dei cluster e sia di leggere da file le attività precedentemente 
 * effettuate. La logica adottata nel salvataggio (e quindi nella lettura) dei file utilizzati è la seguente: nome della tabella presente nel
 * database + numero di cluster da scoprire. </p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class KmeansMiner implements Serializable {
	/**Insieme di cluster che saranno risultato dell'attività di scoperta*/ 
	private ClusterSet C;
	
	/**
	 * Questo metodo è il costruttore della classe KmeansMiner che crea l'oggetto array riferito da C
	 * @param k Numero di cluster da generare
	 */
	public KmeansMiner(int k) {
		C = new ClusterSet(k);
	}
	
	/**
	 * Questo metodo è il costruttore della classe KmeansMiner che inizializza il ClusterSet. In questo caso l'inizializzazione avviene 
	 * leggendo il file. Il ClusterSet conterrà l'insieme di cluster dovuto a precedenti attività di salvataggio. 
	 * @param fileName Nome del file da leggere (concatenazione tra il nome della tabella e il numero di cluster scoperti in precedenza)
	 * @throws FileNotFoundException Questa eccezione è sollevata e propagata se il file è inesistente oppure se il percorso del file è errato
	 * @throws IOException Questa eccezione è sollevata e propagata nel caso di eccezione di input/output
	 * @throws ClassNotFoundException Questa eccezione è sollevata e propagata nel caso in cui non sia stata definita una classe 
	 */
	public KmeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream inputFile = new FileInputStream(fileName);
		ObjectInputStream objectInput = new ObjectInputStream(inputFile);
		C = (ClusterSet)objectInput.readObject();
		inputFile.close();
	}
	
	/**
	 * Questo metodo restituisce il ClusterSet C
	 * @return C
	 */
	public ClusterSet getC() {
		return C;
	}
	
	/**
	 * Questo metodo esegue l'agoritmo del k-means eseguendo i passi dello pseudo-codice:<br>
	 * 1. Scelta casuale di centroidi per k clusters <br>
	 * 2. Assegnazione di ciascuna riga della tabella in data al cluster avente centroide più vicino all'esempio <br>
	 * 3. Calcolo dei nuovi centroidi per ciascun cluster <br>
	 * 4. Ripete i passi 2 e 3 finchè due iterazioni consecutive non restituiscono centroidi uguali
	 * @param data Tabella che costituisce l'insieme di dati su cui eseguire l'algoritmo del k-means
	 * @return numberOfIterations Numero di iterazioni eseguite
	 * @throws OutOfRangeSampleSize Questa eccezione è sollevata e propagata quando si inserisce un numero k maggiore del numero massimo di centroidi che si possono generare
	 */
	public int kmeans(Data data) throws OutOfRangeSampleSize {
		int numberOfIterations = 0;
		//STEP 1
		C.initializeCentroids(data);
		boolean changedCluster = false;
		do {
			numberOfIterations++;
			//STEP 2
			changedCluster = false;
			for(int i=0; i<data.getNumberOfExamples(); i++) {
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster = C.currentCluster(i);
				boolean currentChange = nearestCluster.addData(i);
				if( currentChange )
					changedCluster = true;
				//rimuovo la tupla dal vecchio cluster
				if( currentChange && oldCluster!=null )
					//il nodo va rimosso dal suo vecchio cluster
					oldCluster.removeTuple(i);
			}
			//STEP 3
			C.updateCentroids(data);
		}while( changedCluster );
		return numberOfIterations;
	}
	
	/**
	 * Questo metodo effettua il salvataggio su file delle attività di scoperta. Il salvataggio risulterà utile nel caso in cui si 
	 * vorranno ottenere precedenti attività di scoperta senza interrogare la base di dati.
	 * @param fileName Nome del file su cui salvare l'insieme di cluster (concatenazione tra il nome della tabella e il numero di cluster scoperti in precedenza)
	 * @throws FileNotFoundException Questa eccezione è sollevata e propagata se il file è inesistente oppure se il percorso del file è errato
	 * @throws IOException Questa eccezione è sollevata e propagata nel caso di eccezione di input/output
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream outputFile = new FileOutputStream(fileName);
		ObjectOutputStream objectOutput = new ObjectOutputStream(outputFile);
		objectOutput.writeObject(C);
		objectOutput.flush();
		outputFile.close();
	}
}
