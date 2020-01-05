package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import data.Data;
import data.OutOfRangeSampleSize;
import mining.KmeansMiner;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: ServerOneClient<br>
 * Definizione della classe ServerOneClient che estende la classe Thread, modellando appunto un thread in grado di ascoltare le richieste da 
 * parte di un singolo Client. Questa classe stabilisce con il Client un protocollo di comunicazione che si basa su uno scambio di messaggi 
 * (oggetti istanza della classe String). Infatti per ogni operazione che va a buon fine la classe ServerOneClient scrive sullo stream di 
 * output il messaggio "OK", altrimenti scrive un messaggio relativo all'eccezione. Questa classe si occupa di effettuare particolari 
 * operazioni in base alle richieste da parte del Client. Per la gestione delle richieste viene eseguito il metodo run().</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class ServerOneClient extends Thread {
	/**Socket che connette il Server con il Client*/
	private Socket socket;
	
	/**Stream di input che permette al Server di ricevere le richieste da parte del Client*/
	private ObjectInputStream in;
	
	/**Stream di output che permette al Server di inviare i risultati al Client*/
	private ObjectOutputStream out;
	
	/**Oggetto kmeans istanza della classe KmeansMiner*/
	private KmeansMiner kmeans;
	
	/**Oggetto data istanza della classe Data*/
	private Data data; 
	
	/**
	 * Questo metodo è il costruttore della classe ServerOneClient che inizializza gli attributi socket, in e out e avvia il thread
	 * @param s Oggetto istanza della classe Socket che modella la comunicazione con il Client e permette di ottenere i relativi stream di input e output
	 * @throws IOException Questa eccezione è sollevata nel caso in cui risultano esserci dei problemi nell'ottenere i relativi stream di input e output
	 */
	public ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		super.start();
	}
	
	/**Questo metodo riscrive il metodo run() della superclasse Thread al fine di gestire le richieste del Client*/
	@SuppressWarnings("unused")
	public void run() {
		String tableDb = "";
		int iterFromTable = 0;
		int numIter = 0;
		try {
			while( true ) {
				Object input = in.readObject();
				int sceltaClient = (Integer)input;
				switch( sceltaClient ) {
					case 0:
						tableDb = (String)in.readObject();
						try{
							data = new Data(tableDb);
							System.out.println(data);
						}catch(SQLException e){
							out.writeObject("Errore! Nome Tabella Errato!");
							break;
						}catch (EmptySetException e) {
							out.writeObject(e.getMessage());
							break;
						}catch (DatabaseConnectionException e) {
							out.writeObject(e.getMessage());
							break;
						}catch (NoValueException e) {
							out.writeObject(e.getMessage());
							break;
						}
						out.writeObject("OK");
						break;
					case 1:
						iterFromTable = (Integer)in.readObject();
						System.out.println("Esecuzione KMeans (k = " + iterFromTable + ")");
						kmeans = new KmeansMiner(iterFromTable);
						try{
							numIter = kmeans.kmeans(data);
						}catch (OutOfRangeSampleSize e) {
							out.writeObject(e.getMessage());
							break;
						}
						out.writeObject("OK");
						out.writeObject(iterFromTable);
						out.writeObject(kmeans.getC().toString(data));
						break;
					case 2:
						System.out.println("Salvataggio del file eseguito con successo\n");
						try{
							kmeans.salva(tableDb+iterFromTable);
						}catch(IOException e){
							out.writeObject("Errore! Salvataggio del file non riuscito!");
							break;
						}
						out.writeObject("OK");
						break;
					case 3:
						String filetableName = (String)in.readObject();
						String iterate = (String)in.readObject();
						try{
							kmeans = new KmeansMiner(filetableName+iterate);
							System.out.println("\nEsecuzione KMeans da File (k = " + iterate + ")");
						}
						catch(FileNotFoundException e){
							out.writeObject("Errore! File Non Trovato!");
							break;
						}
						out.writeObject("OK");
						out.writeObject(kmeans.getC().toString());
						break;
				}
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
			return;
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}finally{
			try {
				socket.close();
			}catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}
		}	
	}
}
