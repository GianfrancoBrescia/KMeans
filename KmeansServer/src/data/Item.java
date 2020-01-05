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
 * <p>Class description: Item<br>
 * Definizione della classe astratta Item che modella un generico item (coppia attributo-valore)</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class Item implements Serializable {
	/**Attributo coinvolto nell'item*/
	protected Attribute attribute;
	
	/**Valore assegnato all'attributo*/
	protected Object value;
	
	/**
	 * Questo metodo è il costruttore della classe Item che inizializza i valori dei membri attributi
	 * @param attribute Attributo coinvolto nell'item 
	 * @param value Valore assegnato all'attributo
	 */
	public Item(Attribute attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * Questo metodo restituisce un attributo coinvolto nell'item
	 * @return attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Questo metodo restituisce il valore assegnato ad un attributo
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Questo metodo restituisce il valore assegnato ad un attributo
	 * @return this.value.toString()
	 */
	public String toString() {
		return this.value.toString();
	}
	
	/**
	 * L'implementazione di questo metodo sarà diversa per item discreto e item continuo
	 * @param a Oggetto che corrisponderà o a un attributo discreto (categorico) o a un attributo continuo (numerico)
	 * @return distanza tra item
	 */
	abstract double distance(Object a);
	
	/**
	 * Questo metodo modifica il membro value, assegnandogli il valore restituito da data.computePrototype(clusteredData, attribute)
	 * @param data Riferimento ad un oggetto della classe Data
	 * @param clusteredData Insieme di indici delle righe della matrice in data che formano il cluster
	 */
	public void update(Data data, Set<Integer> clusteredData) {
		value = data.computePrototype(clusteredData, attribute);
	}
}
