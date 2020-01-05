package database;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: Example<br>
 * Definizione della classe Example che modella una transazione letta dalla base di dati</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class Example implements Comparable<Example> {
	/**Array di Object che rappresentano la singola transazione (o riga di una tabella)*/
	private List<Object> example = new ArrayList<Object>();
	
	/**
	 * Questo metodo aggiunge l'oggetto o in coda alla lista example
	 * @param o Oggetto da aggiungere alla transazione
	 */
	public void add(Object o) {
		example.add(o);
	}
	
	/**
	 * Questo metodo restituisce l'i-esimo oggetto presente nella tupla
	 * @param i Indice che verrà utilizzato per considerare un elemento della transazione rappresentata dalla lista
	 * @return oggetto di istanza  della classe Object appartenente alla transazione letta dalla base di dati
	 */
	public Object get(int i) {
		return example.get(i);
	}
	
	/**
	 * Questo metodo confronta due transazioni lette dal database
	 * @param ex Transazioni lette dalla base di dati
	 * @return 0 se i due esempi includono gli stessi valori, altrimenti il valore del metodo compareTo(...) invocato sulla prima coppia di valori
	 * in disaccordo
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compareTo(Example ex) {
		int i = 0;
		for(Object o : ex.example) {
			if( !o.equals(this.example.get(i)) )
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	
	/**
	 * Questo metodo crea una stringa equivalente al risultato della chiamata del metodo toString dei singoli oggetti appartenenti alla lista
	 * @return str
	 */
	public String toString() {
		String str = "";
		for(Object o : example)
			str+= o.toString() + " ";
		return str;
	}
}