package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>Title: KmeansServer</p>
 * <p>Description: Il progetto KmeansServer realizza un Server in grado di acquisire le richieste effettuate da parte del Client e di inviare 
 * le rispettive risposte. Inoltre il Server colleziona le classi per l'esecuzione dell'algoritmo kmeans (scoperta di cluster, 
 * (de)serializzazione)).</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: MultiServer<br>
 * Definizione della classe MultiServer che modella un Server in grado di ascoltare differenti richieste da parte di differenti Client</p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
public class MultiServer {
	/**Porta su cui il server sarà in ascolto*/
	private int PORT;
	
	/**
	 * Questo metodo è il costruttore della classe MultiServer che inizializza la porta su cui il Server sarà in ascolto e invoca il metodo run()
	 * @param port Porta su cui il Server sarà in ascolto
	 */
	public MultiServer(int port) {
		PORT = port;
		run();
	}
	
	/**
	 * Questo metodo istanzia un oggetto istanza della classe ServerSocket che si occuperà di gestire le richieste da parte del Client. 
	 * Ad ogni nuova richiesta di connessione si istanzia ServerOneClient. 
	 */
	void run() {
		ServerSocket s = null;
		try {
			s = new ServerSocket(PORT);
			try {
				while( true ) {
					Socket socket = s.accept();
					try {
						new ServerOneClient(socket);
					}catch(IOException e) {
						System.out.println(e);
						return;
					}
				}
			}catch(IOException e) {
				System.out.println(e);
				return;
			}finally {
				try {
					s.close();
				}catch(IOException e) {
					System.out.println(e);
					return;
				}
			}
		}catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
	
	/**
	 * Questo metodo istanzia un oggetto di tipo MultiServer
	 * @param args Argomento passato in input
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		MultiServer multiServer = new MultiServer(8080);
	}
}
