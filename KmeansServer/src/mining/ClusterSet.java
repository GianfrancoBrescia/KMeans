package mining;

import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: ClusterSet<br>
 * Definizione della classe ClusterSet che rappresenta un insieme di cluster (determinati dall'algoritmo del k-means)</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ClusterSet implements Serializable {
	/**Vettore di Cluster*/
	private Cluster C[];
	
	/**Posizione valida per la memorizzazione di un nuovo cluster in C*/
	private int i = 0;
	
	/**
	 * Questo metodo è il costruttore della classe ClusterSet che crea l'oggetto array riferito da C
	 * @param k Numero di cluster da generare (k-means)
	 */
	public ClusterSet(int k) {
		C = new Cluster[k];
	}
	
	/**
	 * Questo metodo aggiunge un cluster al vettore C e incrementa l'indice per successive aggiunte
	 * @param c Cluster da aggiungere al vettore
	 */
	void add(Cluster c) {
		C[i] = c;
		i++;
	}
	
	/**
	 * Questo metodo restituisce un cluster presente nella posizione i-esima del vettore
	 * @param i Indice corrispondente al cluster che si vuole ottenere
	 * @return C[i]
	 */
	public Cluster get(int i) {
		return C[i];
	}
	
	/**
	 * Questo metodo sceglie i centroidi, crea un cluster per ogni centroide e lo memorizza nel vettore C
	 * @param data Tabella da cui scegliere i centroidi
	 * @throws OutOfRangeSampleSize Questa eccezione è sollevata e propagata quando si inserisce un numero k maggiore del numero massimo di centroidi che si possono generare
	 */
	void initializeCentroids(Data data) throws OutOfRangeSampleSize {
		int centroidIndexes[] = data.sampling(C.length);
		for(int i=0; i<centroidIndexes.length; i++) {
			Tuple centroidI = data.getItemSet(centroidIndexes[i]);
			add(new Cluster(centroidI));
		}
	}
	
	/**
	 * Questo metodo calcola la distanza tra la tupla riferita da tuple ed il centroide di ciascun cluster presente nel vettore C e  
	 * restituisce il cluster più vicino 
	 * @param tuple Riferimento ad un oggetto Tuple
	 * @return cluster più "vicino" alla tupla passata
	 */
	Cluster nearestCluster(Tuple tuple) {
		int i, j=0;
		double minDistance = tuple.getDistance(get(0).getCentroid());
		for(i=1; i<C.length; i++) {
			double currentDistance = tuple.getDistance(get(i).getCentroid());
			if( currentDistance < minDistance ) {
				minDistance = currentDistance;
				j = i;
			}
		}
		return get(j);
	}
	
	/**
	 * Questo metodo identifica e restituisce il cluster a cui la tupla appartiene. Se la tupla non è inclusa in nessun cluster questo 
	 * metodo restituisce null
	 * @param id Indice di una riga della tabella in Data
	 * @return temp Cluster a cui appartiene la tupla identificata da id
	 */
	public Cluster currentCluster(int id) {
		int i, j=0;
		Cluster temp = null;
		for(i=0; i<C.length; i++) {
			if( this.get(i).contain(id) ) {
				j = i;
				temp = get(j);
			}
		}
		return temp;
	}
	
	/**
	 * Questo metodo calcola il nuovo centroide per ciascun cluster in C 
	 * @param data Tabella da cui calcolare il centroide
	 */
	void updateCentroids(Data data) {
		for(int i=0; i<C.length; i++) {
			get(i).computeCentroid(data);
		}
	}
	
	/**
	 * Questo metodo restituisce una stringa fatta da ciascun centroide dell'insieme dei cluster
	 * @return centroid
	 */
	public String toString() {
		String centroid = new String("");
		for(int i=0; i<C.length; i++) 
			centroid+= get(i).toString();
		return centroid;
	}
	
	/**
	 * Questo metodo restituisce una stringa che descrive lo stato di ciascun cluster presente nel vettore C
	 * @param data Tabella contenente transazioni di esempio 
	 * @return str
	 */
	public String toString(Data data) {
		String str = "";
		for(int i=0; i<C.length; i++) {
			if( get(i) != null ) {
				str+= i + ": " + get(i).toString(data) + "\n";
			}
		}
		return str;
	}
}
