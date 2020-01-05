package data;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: ContinuousItem<br>
 * Definizione della classe concreta ContinuousItem che estende la classe Item e che modella una coppia &ltAttributo continuo - Valore numerico&gt</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ContinuousItem extends Item {
	/**
	 * Questo metodo è il costruttore della classe ContinuousItem che invoca il costruttore della classe madre (Item)
	 * @param attribute Attributo continuo (numerico) coinvolto nell'item corrente
	 * @param value Valore da assegnare all'item
	 */
	ContinuousItem(Attribute attribute, Double value) {
		super(attribute, value);
	}
	
	/**
	 * Questo metodo determina la distanza (in valore assoluto) tra il valore scalato memorizzato nell'item corrente e quello scalato
	 * associato al parametro a
	 * @param a Valore da confrontare con il valore scalato memorizzato nell'item corrente
	 */
	double distance(Object a) {
		Attribute objAttribute = ((ContinuousItem)a).getAttribute();
		double currentValue = ((ContinuousAttribute)this.getAttribute()).getScaledValue((Double)this.getValue());
		double tupleValue = ((ContinuousAttribute)objAttribute).getScaledValue((Double)((ContinuousItem)a).getValue());
		return Math.abs(currentValue-tupleValue);
	}
}
