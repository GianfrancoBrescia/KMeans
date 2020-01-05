package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: TableData<br>
 * Definizione della classe TableData che modella l'insieme di transazioni collezionate in una tabella. La singola transazione è modellata 
 * dalla classe Example. In particolare la classe TableData si occupa di ottenere in maniera appropriata le tuple dalla base di dati e di 
 * renderle disponibili al sistema.</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class TableData {
	/**Oggetto istanza della classe DbAccess che permette l'accesso al database*/
	DbAccess db;
	
	/**
	 * Questo metodo è il costruttore della classe TableData che inizializza il membro db in modo da rendere disponibile l'accesso alla
	 * base di dati all'intera classe
	 * @param db Oggetto istanza della classe DbAccess che permette l'accesso al database
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
	 * Questo metodo ricava lo schema della tabella con nome table. Inoltre esegue un'interrogazione per estrarre le tuple distinte da 
	 * tale tabella. Per ogni tupla del resultset, si crea un oggetto, istanza della classe Example, il cui riferimento va incluso nella
	 * lista da restituire. In particolare, per la tupla corrente nel resultset, si estraggono i valori dei singoli campi e li si aggiungono 
	 * all'oggetto istanza della classe Example che si sta costruendo.
	 * @param table Nome della tabella nel database su cui effettuare la query
	 * @return data Lista di transazioni distinte memorizzate nella tabella table
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 * @throws EmptySetException Questa eccezione è sollevata e propagata se il resultset è vuoto
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
		Example ex;
		List<Example> data = new ArrayList<Example>();
		String query = "SELECT DISTINCT * FROM " + table + ";";
		Statement s = db.getConnection().createStatement();
		ResultSet r = s.executeQuery(query);
		TableSchema tbs = new TableSchema(this.db, table);
		if( r.next() ) {
			do {
				ex = new Example();
				for(int i=0; i<tbs.getNumberOfAttributes(); i++) {
					String currentColumn = tbs.getColumn(i).getColumnName();
					if( tbs.getColumn(i).isNumber() )
						ex.add(r.getDouble(currentColumn));
					else
						ex.add(r.getString(currentColumn));	
				}
				data.add(ex);
			}while( r.next() );
		}else
			throw new EmptySetException("Errore! Risultato della query vuoto! Nessuna tupla è presente nella tabella.");
		r.close();
		return data;
	}
	
	/**
	 * Questo metodo formula ed esegue un'interrogazione SQL per estrarre i valori distinti ordinati di column e popolare un insieme da
	 * restituire
	 * @param table Nome della tabella sulla quale effettuare l'interrogazione SQL
	 * @param column Nome della colonna da considerare per la tabella specificata
	 * @return res Insieme di valori distinti e ordinati in modalità ascendente che l'attributo identificato da column assume in table
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<Object> res = new TreeSet<Object>();
		Statement s = db.getConnection().createStatement();
		String query = "SELECT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + " ASC;";
		ResultSet r = s.executeQuery(query);
		while( r.next() ) 
			res.add(r.getObject(column.getColumnName()));
		r.close();
		return res;
	}

	/**
	 * Questo metodo formula ed esegue un'interrogazione SQL per estrarre il valore aggregato (valore minimo o valore massimo) cercato nella
	 * colonna column della tabella table
	 * @param table Nome della tabella sulla quale effettuare l'interrogazione SQL
	 * @param column Nome della colonna da considerare per la tabella specificata
	 * @param aggregate Operatore SQL di aggregazione utilizzato per determinare se effettuare una query per estrarre il valore minimo o il valore massimo
	 * @return valore minimo o valore massimo calcolato per la colonna column
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 * @throws NoValueException Questa eccezione è sollevata e propagata se il resultset è vuoto oppure se il valore calcolato è pari a null 
	 */
	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
		Object res = new Object();
		Statement s = db.getConnection().createStatement();
		String query = "SELECT " + aggregate + "(" + column.getColumnName() + ") FROM " + table + ";";
		ResultSet r = s.executeQuery(query);
		if( r.next() && res!=null ) {
			do {
				res = r.getObject(1);
			}while( r.next() );
		}else if( res == null ) {
			String aggregateValue = "";
			aggregateValue = (aggregate == QUERY_TYPE.MAX) ? "Massimo" : "Minimo";
			throw new NoValueException("Errore! Impossibile calcolare " + aggregateValue + " sulla colonna " + column.getColumnName());
		}else
			throw new NoValueException("Errore! Risultato della query vuoto. Nessuna tupla presente nella tabella!");
		r.close();
		return res;
	}
}
