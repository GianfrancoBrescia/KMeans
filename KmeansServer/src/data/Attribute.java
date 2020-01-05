package data;

import java.io.Serializable;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: Attribute<br>
 * Definizione della classe astratta Attribute che modella l'entità attributo</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class Attribute implements Serializable {
	/**Nome simbolico dell'attributo */
	protected String name;
	
	/**Identificativo numerico dell'attributo */
	protected int index;
	
	/**
	 * Questo metodo è il costruttore della classe Attribute che inizializza i valori dei membri name e index
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo (primo, secondo, ... attributo della tupla)
	 */
	Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Questo metodo restituisce il nome dell'attributo
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Questo metodo restituisce l'identificativo numerico dell'attributo
	 * @return index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Questo metodo sovrascrive il metodo ereditato dalla superclasse e restituisce la stringa rappresentante lo stato dell'oggetto
	 * @return this.name
	 */
	public String toString() {
		return this.name;
	}
}
