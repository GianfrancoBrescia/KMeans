package database;

import java.sql.*;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: DbAccess<br>
 * Definizione della classe DbAccess che realizza l'accesso alla base di dati sfruttando i driver di connessione mysql. La connessione è 
 * effettuata in locale attraverso una url del tipo: "DBMS://SERVER:PORT/DATABASE" dove PORT sarà la porta in cui è disponibile il servizio 
 * mysql. Inoltre per la connessione saranno necessari uno userid e una password.</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class DbAccess {
	/**Nome del driver utilizzato per la connessione al database*/
	private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	
	/**Tipo di DBMS*/
	private final String DBMS = "jdbc:mysql";
	
	/**Identificativo del server su cui risiede la base di dati. (Indirizzo necessario per la connessione al database)*/
	private final String SERVER = "localhost";
	
	/**Nome del database*/
	private final String DATABASE = "MapDB";
	
	/**Porta su cui il DBMS MySQL accetta le connessioni*/
	private final String PORT;
	
	/**Nome dell'utente per l'accesso al database*/
	private final String USER_ID;
	
	/**Password di autenticazione per l'utente identificato da USER_ID*/
	private final String PASSWORD;
	
	/**Oggetto istanza della classe Connection che gestisce una connessione*/
	private Connection conn;
	
	/**
	 * Questo metodo è il costruttore della classe DbAccess che inizializza i membri PORT, USER_ID, PASSWORD
	 */
	public DbAccess() {
		PORT = "3306";
		USER_ID = "MapUser";
		PASSWORD = "map";
	}
	
	/**
	 * Questo metodo impartisce al class loader l'ordine di caricare il driver mysql e inizializza la connessione riferita da conn. Inoltre 
	 * questo metodo solleva e propaga un'eccezione di tipo DatabaseConnectionException in caso di fallimento nella connessione al database.
	 * @throws DatabaseConnectionException Questa eccezione è sollevata e propagata nel caso di fallimento nella connessione al database
	 * @throws ClassNotFoundException Questa eccezione è sollevata e propagata nel caso in cui non sia stata definita una classe 
	 */
	public void initConnection() throws DatabaseConnectionException, ClassNotFoundException {
		String url = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
		Class.forName(DRIVER_CLASS_NAME);
		try {
			conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
		}catch(SQLException e) {
			throw new DatabaseConnectionException("Errore! Impossibile connettersi al database!");
		}
	}
	
	/**
	 * Questo metodo restituisce l'oggetto istanza della classe Connection che gestisce una connessione
	 * @return conn
	 */
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 * Questo metodo chiude la connessione
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 */
	public void closeConnection() throws SQLException {
		conn.close();
	}
}
