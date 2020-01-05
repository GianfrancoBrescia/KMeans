package data;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: Tuple<br>
 * Definizione della classe Tuple che rappresenta una tupla come sequenza di coppie attributo-valore</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Tuple implements Serializable {
	/**Sequenza di coppie attributo-valore*/
	private Item[] tuple;
	
	/**
	 * Questo metodo è il costruttore della classe Tuple che costruisce l'oggetto riferito da tuple
	 * @param size Numero di item che costituirà la tupla
	 */
	Tuple(int size) {
		tuple = new Item[size];
	}
	
	/**
	 * Questo metodo restituisce il numero di item che costituisce la tupla
	 * @return tuple.length
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * Questo metodo restituisce l'item presente nella tupla in posizione i-esima
	 * @param i Posizione in cui è presente l'item desiderato
	 * @return tuple[i]
	 */
	public Item get(int i) {
		return tuple[i];
	}
	
	/**
	 * Questo metodo memorizza il valore dell'item c nella posizione i-esima della tupla
	 * @param c Item da memorizzare nella posizione i-esima della tupla
	 * @param i Posizione in cui memorizzare l'item c
	 */
	void add(Item c, int i) {
		tuple[i] = c;
	}
	
	/**
	 * Questo metodo determina la distanza tra la tupla riferita da obj e la tupla corrente. La distanza è ottenuta
	 * come la somma delle distanze tra gli item in posizioni eguali nelle due tuple
	 * @param obj Tupla da confrontare con la tupla corrente
	 * @return distance Distanza tra la tupla obj e la tupla corrente
	 */
	public double getDistance(Tuple obj) {
		double distance = 0.0;
		for(int i=0; i<obj.getLength(); i++) 
			distance+= this.get(i).distance(obj.get(i));
		return distance;
	}
	
	/**
	 * Questo metodo restituisce la media delle distanze tra la tupla corrente e quelle ottenibili dalle righe della tabella in data aventi indice in clusteredData
	 * @param data Tabella dalle cui righe è possibile ottenere le distanze tra item
	 * @param clusteredData Indici delle righe presenti nella tabella data
	 * @return p Media delle distanze tra la tupla corrente e quelle ottenibili dalle righe di data 
	 */
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		double p=0.0, sumD=0.0;
		for(Integer i : clusteredData) {
			double d = getDistance(data.getItemSet(i));
			sumD+= d;
		}
		p = sumD/clusteredData.size();
		return p;
	}
}
