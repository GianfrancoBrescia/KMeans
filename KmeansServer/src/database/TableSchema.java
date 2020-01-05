package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: TableSchema<br>
 * Definizione della classe tableSchema che modella lo schema di una tabella nel database relazionale</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class TableSchema {
	/**Oggetto istanza della classe DbAccess che permette l'accesso al database*/
	DbAccess db;
	
	/**
	 * <p>Title: KmeansServer</p>
	 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
	 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
	 * (de)serializzazione)).</p>
	 * <p>Copyright: Copyright (c) 2018</p>
	 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
	 * <p>Class description: Column<br>
	 * Definizione della inner class Column (inner in TableSchema) che modella una colonna di una tabella presente nel database relazionale</p>
	 * @author Gianfranco Brescia
	 * @version 1.0
	 */
	public class Column {
		/**Nome di una colonna di una tabella presente nel database relazionale */
		private String name;
		
		/**Tipo di valore che può essere inserito nella colonna*/
		private String type;
		
		/**
		 * Questo metodo è il costruttore della classe Column che inizializza i valori dei membri name e type
		 * @param name Nome di una colonna di una tabella presente nel database relazionale
		 * @param type Tipo di valore che può essere inserito nella colonna
		 */
		Column(String name, String type) {
			this.name = name;
			this.type = type;
		}
		
		/**
		 * Questo metodo restituisce il nome di una colonna di una tabella presente nel database relazionale
		 * @return name
		 */
		public String getColumnName() {
			return name;
		}
		
		/**
		 * Questo metodo verifica se il tipo di valore che la colonna può assumere è un numero
		 * @return valore booleano: vero se l'uguaglianza è verificata, falso altrimenti
		 */
		public boolean isNumber() {
			return type.equals("number");
		}
		
		/**
		 * Questo metodo restituisce il nome di una colonna di una tabella presente nel database relazionale con il tipo di valore che 
		 * può essere inserito in quella colonna
		 * @return stringa contenente nome e tipo della colonna
		 */
		public String toString() {
			return name + ":" + type;
		}
	}
	
	/**Lista contenente le colonne della tabella presente nel database*/
	List<Column> tableSchema = new ArrayList<Column>();
	
	/**
	 * Questo metodo è il costruttore della classe tableSchema che modella lo schema di una tabella presente nel database relazionale
	 * @param db Oggetto istanza della classe DbAccess che permette l'accesso al database
	 * @param tableName Nome della tabella di cui modellare lo schema
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		this.db = db;
		HashMap<String,String> mapSQL_JAVATypes = new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
	    ResultSet res = meta.getColumns(null, null, tableName, null);  
	    while( res.next() ) {
	    	if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	    		tableSchema.add(new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
	    }
	    res.close();
	}
	
	/**
	 * Questo metodo restituisce il numero di attributi (colonne) che compongono la tabella
	 * @return tableSchema.size()
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}
	
	/**
	 * Questo metodo restituisce la index-esima colonna della tabella
	 * @param index Indice di colonna
	 * @return index-esima colonna della tabella
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}	
}

		     


