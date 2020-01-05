package data;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: DiscreteItem<br>
 * Definizione della classe DiscreteItem che estende la classe Item e che rappresenta una coppia &ltAttributo discreto-Valore discreto&gt</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DiscreteItem extends Item {
	/**
	 * Questo metodo è il costruttore della classe DiscreteItem che invoca il costruttore della classe madre (Item)
	 * @param attribute Attributo coinvolto nell'item
	 * @param value Valore assegnato all'attributo
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
	
	/**
	 * Questo metodo restituisce 0 se il valore assegnato all'attributo value è uguale ad a, 1 altrimenti
	 * @param a Oggetto da confrontare con value 
	 * @return valore decimale a doppia precisione
	 */
	double distance(Object a) {
		if( getValue().equals(a.toString()) ) return 0.0;
		else return 1.0;
	}
}
