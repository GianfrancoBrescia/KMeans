package data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: Data<br>
 * Definizione della classe Data che modella l'insieme di transazioni (o tuple)</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Data implements Serializable {
	/**Tabella contenente le varie transazioni ottenute dalla base di dati*/
	private List<Example> data;
	
	/**Cardinalità dell'insieme di transazioni (numero di righe in data)*/
	private int numberOfExamples;
	
	/**Lista degli attributi in ciascuna tupla (schema della tabella di dati)*/
	private List<Attribute> explanatorySet;
	
	/**Numero di transazioni distinte memorizzate nella matrice data*/
	private int distinctTuples;
	
	/**
	 * Questo metodo è il costruttore della classe Data che: inizializza numberOfExamples; inizializza la tabella data con transazioni 
	 * di esempio; inizializza explanatorySet
	 * @param tableName Nome della tabella da cui caricare i dati 
	 * @throws SQLException Questa eccezione è sollevata e propagata in presenza di errori nell'esecuzione della query
	 * @throws EmptySetException Questa eccezione è sollevata e propagata se il resultset è vuoto
	 * @throws DatabaseConnectionException Questa eccezione è sollevata e propagata nel caso di fallimento nella connessione al database
	 * @throws ClassNotFoundException Questa eccezione è sollevata e propagata nel caso in cui non sia stata definita una classe 
	 * @throws NoValueException Questa eccezione è sollevata e propagata nel caso in cui sia assente un valore all'interno di un resultset
	 */
	public Data(String tableName) throws SQLException, EmptySetException, DatabaseConnectionException, ClassNotFoundException, NoValueException {
		DbAccess dba = new DbAccess();
		dba.initConnection();
		TableData tbd = new TableData(dba);
		TableSchema tbs = new TableSchema(dba, tableName);
		this.data = tbd.getDistinctTransazioni(tableName);
		this.numberOfExamples = this.data.size();
		this.explanatorySet = new LinkedList<Attribute>();
		for(int i=0; i<tbs.getNumberOfAttributes(); i++) {
			String columnName = tbs.getColumn(i).getColumnName();
			if( tbs.getColumn(i).isNumber() ) {
				double max = ((Float)tbd.getAggregateColumnValue(tableName, tbs.getColumn(i), QUERY_TYPE.MAX)).doubleValue();
				double min = ((Float)tbd.getAggregateColumnValue(tableName, tbs.getColumn(i), QUERY_TYPE.MIN)).doubleValue();
				explanatorySet.add(new ContinuousAttribute(columnName, i, min, max));
			}else {
				Set<Object> resultValue = tbd.getDistinctColumnValues(tableName, tbs.getColumn(i));
				TreeSet<String> attributeValues = new TreeSet<String>();
				for(Object item : resultValue)
					attributeValues.add((String)item);
				explanatorySet.add(new DiscreteAttribute(columnName, i, attributeValues));
			}
		}
		distinctTuples = this.data.size();
	}
	
	/**
	 * Questo metodo restituisce la cardinalità dell'insieme di transazioni
	 * @return numberOfExamples
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	
	/**
	 * Questo metodo restituisce la cardinalità dell'insieme degli attributi
	 * @return explanatorySet.size()
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Questo metodo restituisce lo schema dei dati
	 * @return explanatorySet
	 */
	List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}
	
	/**
	 * Questo metodo restituisce il valore assunto in data dall'attributo in posizione (exampleIndex, attributeIndex)
	 * @param exampleIndex Indice di riga in riferimento alla matrice memorizzata in data
	 * @param attributeIndex Indice di colonna in riferimento alla matrice memorizzata in data
	 * @return data.get(exampleIndex).get(attributeIndex)
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex){
		return data.get(exampleIndex).get(attributeIndex);
	}
	
	/**
	 * Questo metodo crea e restituisce un'istanza di Tuple che modella come sequenza di coppie Attributo-Valore la i-esima riga in data
	 * @param index Indice di riga in data
	 * @return tuple
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(getNumberOfExplanatoryAttributes());
		for(int i=0; i<getNumberOfExplanatoryAttributes(); i++) 
			if( getAttributeValue(index,i) instanceof Double )
				tuple.add(new ContinuousItem((ContinuousAttribute)explanatorySet.get(i), (Double)getAttributeValue(index,i)), i);
			else
				tuple.add(new DiscreteItem((DiscreteAttribute)explanatorySet.get(i), (String)getAttributeValue(index,i)), i);
		return tuple;
	}
	
	/**
	 * Questo metodo restituisce un array di k interi rappresentanti gli indici di riga in data per le tuple inizialmente scelte come centroidi (passo 1 dell'algoritmo del k-means)
	 * @param k Numero di cluster da generare
	 * @return centroidIndexes
	 * @throws OutOfRangeSampleSize Questa eccezione è sollevata e propagata quando si inserisce un numero k maggiore del numero massimo di centroidi che si possono generare
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		int centroidIndexes[] = new int[k];
		//choose k random different centroids in data.
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		if( k<=0 || k>this.distinctTuples ) throw new OutOfRangeSampleSize("Errore! - Numero di iterate inserito non valido!\nIntervallo valori accettati per k: 1 - " + this.distinctTuples); {
			for(int i=0; i<k; i++) {
				boolean found = false;
				int c;
				do {
					found = false;
					c = rand.nextInt(getNumberOfExamples());
					//verify that centroid[c] is not equal to a centroide already stored in centroidIndexes
					for(int j=0; j<i; j++) {
						if( compare(centroidIndexes[j], c) ) {
							found = true;
							break;
						}
					}
				}while( found );
				centroidIndexes[i] = c;
			}
		}
		return centroidIndexes;
	}
	
	/**
	 * Questo metodo restituisce vero se le due righe in data contengono gli stessi valori, falso altrimenti
	 * @param i Indice della prima riga in data 
	 * @param j Indice della seconda riga in data 
	 * @return esito
	 */
	private boolean compare(int i, int j) {
		int index = 0;
		boolean cond = true;
		Object first = new Object();
		Object second = new Object();
		while( cond && index<getNumberOfExplanatoryAttributes() ) {
			first = getAttributeValue(i, index);
			second = getAttributeValue(j, index);
			if( !first.equals(second) )
				cond = false;
			index++;
		}
		return cond;
	}
	
	/**
	 * Questo metodo restituisce il valore del centroide rispetto ad attribute.
	 * @param idList Insieme di indici di riga 
	 * @param attribute Attributo rispetto al quale calcolare il prototipo (centroide) 
	 * @return centroide rispetto ad attribute
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if( attribute instanceof ContinuousAttribute )
			return computePrototype(idList, (ContinuousAttribute)attribute);
		else
			return computePrototype(idList, (DiscreteAttribute)attribute);
	}
	
	/**
	 * Questo metodo determina e restituisce il valore che occorre più frequentemente per attribute nel sottoinsieme di dati individuato da idList
	 * @param idList Insieme degli indici delle righe di data appartenenti ad un cluster 
	 * @param attribute Attributo discreto rispetto al quale calcolare il prototipo (centroide) 
	 * @return centroide rispetto ad attribute
	 */
	String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		int maxFrequency=0, attributeFrequency=0;
		String maxValue = new String("");
		for(String val : attribute) {
			attributeFrequency = attribute.frequency(this, idList, val);
			if( attributeFrequency > maxFrequency ) {
				maxFrequency = attributeFrequency; 
				maxValue = val;
			}
		}
		return maxValue;
	}
	
	/**
	 * Questo metodo determina il valore prototipo come media dei valori osservati per attribute nelle transazioni di data aventi indice 
	 * di riga in idList
	 * @param idList Indice di riga delle varie transazioni presenti in data
	 * @param attribute Attributo continuo (numerico) da cui calcolare il valore prototipo
	 * @return media
	 */
	Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double somma = 0.0;
		double media = 0.0;
		for(Integer value : idList)
			somma = somma + (Double)(data.get(value).get(1));
		media = somma/idList.size();
		return media;
	}
	
	/**
	 * Questo metodo crea una stringa in cui viene memorizzato lo schema della tabella e le transazioni memorizzate in data, opportunamente enumerate
	 * @return stringa
	 */
	public String toString() {
		String stringa = new String("");
		for(int i=0; i<getNumberOfExamples(); i++) {
			stringa+= i + ": ";
			for(int j=0; j<getNumberOfExplanatoryAttributes(); j++) {
				stringa+= getAttributeValue(i, j);
				if( j<getNumberOfExplanatoryAttributes()-1 ) stringa+= ", ";
			}
			stringa+= "\n";
		}
		return stringa;
	}
}
