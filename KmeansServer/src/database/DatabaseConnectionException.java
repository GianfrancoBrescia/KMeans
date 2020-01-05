package database;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: DatabaseConnectionException<br>
 * Definizione della classe DatabaseConnectionException che estende Exception per modellare il fallimento nella connessione al database</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {
	/**
	 * Questo metodo è il costruttore della classe DatabaseConnectionException che richiama il costruttore della classe madre (Exception) per
	 * creare l'oggetto eccezione corrispondente
	 * @param message Messaggio da mostrare in caso di eccezione
	 */
	DatabaseConnectionException(String message) {
		super(message);
	}
}
