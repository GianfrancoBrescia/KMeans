package mining;

import java.io.Serializable;
import java.util.*;

import data.Data;
import data.Tuple;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: Cluster<br>
 * Definizione della classe Cluster che modella un cluster</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Cluster implements Serializable {
	/**Centroide di un cluster*/
	private Tuple centroid;
	
	/**Insieme delle righe della tabella Data appartenenti al Cluster*/
	private Set<Integer> clusteredData; 
	
	/**
	 * Questo metodo è il costruttore della classe Cluster che modella un cluster 
	 * @param centroid Centroide di un cluster
	 */
	Cluster(Tuple centroid) {
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();
	}
		
	/**
	 * Questo metodo restituisce il centroide di un cluster
	 * @return centroid
	 */
	public Tuple getCentroid() {
		return centroid;
	}
	
	/**
	 * Questo metodo calcola il centroide di un cluster 
	 * @param data Tabella da cui ottenere il centroide
	 */
	void computeCentroid(Data data) {
		for(int i=0; i<centroid.getLength(); i++){
			centroid.get(i).update(data, clusteredData);
		}
	}
	
	/**
	 * Questo metodo restituisce il valore vero se la tupla modifica il cluster, falso altrimenti
	 * @param id Valore da inserire 
	 * @return valore booleano
	 */
	boolean addData(int id) {
		return clusteredData.add(id);
	}
	
	/**
	 * Questo metodo verifica se una transazione è clusterizzata nell'array corrente
	 * @param id Indice della transazione da verificare  
	 * @return valore booleano: vero se la transazione è clusterizzata nell'array corrente, falso altrimenti
	 */
	boolean contain(int id) {
		boolean cond = false;
		Iterator<Integer> clusterIterator = clusteredData.iterator();
		while( clusterIterator.hasNext() && cond==false ) {
			Object item = clusterIterator.next();
			if( item instanceof Integer && item.equals(id) )
				cond = true;
		}
		return cond;
	}
	
	/**
	 * Questo metodo rimuove la tupla che ha modificato il cluster
	 * @param id Indice della tupla da rimuovere
	 */
	void removeTuple(int id) {
		clusteredData.remove(id);
	}
	
	/**
	 * Questo metodo restituisce una stringa contenente i centroidi di un cluster
	 * @return str
	 */
	public String toString() {
		String str = "Centroid = (";
		for(int i=0; i<centroid.getLength(); i++) {
			str+= centroid.get(i);
			if( i < centroid.getLength()-1 ) 
				str+= " ";
		}
		str+= ")\n";
		return str;
	}
	
	/**
	 * Questo metodo restituisce una stringa contenente il centroide di un cluster, mostrando inoltre tutte le tuple in esso clusterizzate
	 * con la relativa distanza
	 * @param data Tabella da cui si ricavano le tuple clusterizzate in un cluster
	 * @return str
	 */
	public String toString(Data data) {
		String str= "Centroid = (";
		for(int i=0; i<centroid.getLength(); i++) {
			str+= centroid.get(i);
			if( i < centroid.getLength()-1 ) str+= " ";
		}
		str+= ")\nExamples:\n";
		for(Integer integer : clusteredData) {
			str+= "[";
			for(int j=0; j<data.getNumberOfExplanatoryAttributes(); j++) {
				str+= data.getAttributeValue(integer, j);
				if( j < data.getNumberOfExplanatoryAttributes()-1 ) str+= " ";
			}
			str+= "] dist = " + getCentroid().getDistance(data.getItemSet(integer)) + "\n";
		}
		str+= "AvgDistance = " + getCentroid().avgDistance(data, clusteredData) + "\n";
		return str;
	}
}
