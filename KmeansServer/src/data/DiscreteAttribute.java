package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: DiscreteAttribute<br>
 * Definizione della classe concreta DiscreteAttribute che estende la classe Attribute e che rappresenta un attributo discreto (categorico)</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Iterable<String> {
	/**Contenitore generics di tipo TreeSet&ltString&gt, uno per ciascun valore del dominio discreto. I valori del dominio sono memorizzati in values seguendo un ordine lessicografico*/
	private TreeSet<String> values;
	
	/**
	 * Questo metodo è il costruttore della classe DiscreteAttribute che invoca il costruttore della classe madre (Attribute) e inizializza il membro values con il parametro in input corrispondente
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo (primo, secondo, ... attributo della tupla)
	 * @param values TreeSet di stringhe rappresentante il dominio dell'attributo
	 */
	public DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name, index);
		this.values = values;
	}
	
	/**
	 * Questo metodo restituisce il numero di valori discreti nel dominio dell'attributo
	 * @return dimensione di values
	 */
	public int getNumberOfDistinctValues() {
		return values.size();
	}
	
	/**
	 * Questo metodo restituisce l'iteratore del contenitore generics di tipo TreeSet
	 * @return values.iterator()
	 */
	public Iterator<String> iterator() {
		return values.iterator();
	}
	
	/**
	 * Questo metodo determina il numero di volte che il valore v compare in corrispondenza dell'attributo corrente (indice di colonna) negli esempi 
	 * memorizzati in data e indicizzate (per riga) da idList
	 * @param data Tabella di transazioni di esempio in cui andare a calcolare v
	 * @param idList Riferimento ad un oggetto Set che mantiene l'insieme degli indici di riga di alcune tuple memorizzate in data
	 * @param v Valore discreto
	 * @return occurenceNumber Numero di occorrenze del valore discreto (intero)
	 */
	int frequency(Data data, Set<Integer> idList, String v) {
		int occurenceNumber=0, attributeIndex=this.getIndex();
		for(Object o : idList)
			if( v.equals(data.getAttributeValue((Integer)o, attributeIndex)) )
				occurenceNumber++;
		return occurenceNumber;
	}
}
