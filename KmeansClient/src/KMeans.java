import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * <p>Title: KmeansClient</p>
 * <p>Description: Il progetto KmeansClient realizza un sistema Client in grado di collegarsi al Server tramite l'indirizzo Ip e il numero
 * di porta su cui il Server è in ascolto. Una volta instaurata la connessione l'utente può scegliere se avviare un nuovo processo di 
 * clustering oppure recuperare cluster precedentemente serializzati in un qualche file.</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
 * <p>Class description: KMeans<br>
 * Definizione della classe KMeans che definisce un JFRAME e che permette di visualizzare due sezioni organizzate in tab: la PRIMA SEZIONE, 
 * identificata dal tab DB con relativa icona, si occupa di effettuare richieste al Server per l'esecuzione di un'attività di scoperta dei 
 * cluster sulla base di dati, eseguendo l'algoritmo del kmeans. In questo caso il Client rimarrà in attesa dei risultati da parte del 
 * Server, mostrando, se tutto procede correttamente, il risultato dell'algoritmo kmeans con i relativi cluster scoperti, altrimenti verrà 
 * mostrato un messaggio nel caso di errore. Inoltre la corretta esecuzione dell'algoritmo in questo caso produrrà sul Server un file il cui 
 * nome corrisponderà al nome della tabella specificato nell'apposita casella di testo concatenato al numero di cluster che sono stati 
 * scoperti.<br><br>
 * <center><img src="images/centroids_from_database.jpg"></center><br><br>
 * La SECONDA SEZIONE, invece, identificata dal tab FILE con relativa icona, si occupa di effettuare richieste al Server per effettuare 
 * una lettura da file contenente attività precendenti di scoperta dei cluster sulla base di dati. Il nome del file corrisponderà alla 
 * concatenazione tra il nome della tabella letta in precedenza e il numero di cluster inserito nell'apposita casella di testo. Nel caso 
 * in cui tale file esista verranno mostrati a video i risultati dell'algoritmo kmeans con i relativi cluster scoperti in precedenza, 
 * altrimenti verrà visualizzato un messaggio di errore.<br><br>
 * <center><img src="images/centroids_from_file.jpg"></center></p>
 * @author Gianfranco Brescia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class KMeans extends JFrame {
	/**Stream di output che permette al Client di inviare le richieste al Server*/
	private ObjectOutputStream out;
	
	/**Stream di input che permette al Client di ricevere i risultati da parte del Server*/
	private ObjectInputStream in;
		
	/**
	 * <p>Title: KmeansClient</p>
	 * <p>Description: Il progetto KmeansClient realizza un sistema Client in grado di collegarsi al Server tramite l'indirizzo ip e il numero
	 * di porta su cui il Server è in ascolto. Una volta instaurata la connessione l'utente può scegliere se avviare un nuovo processo di 
	 * clustering oppure recuperare cluster precedentemente serializzati in un qualche file.</p>
	 * <p>Copyright: Copyright (c) 2018</p>
	 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
	 * <p>Class description: TabbedPane<br>
	 * Definizione della Inner Class TabbedPane che estende la classe JPanel e che rappresenta la schermata principale dell'interfaccia 
	 * grafica che conterrà le due sezioni (tab) principali</p>
	 * @author Gianfranco Brescia
	 * @version 1.0
	 */
	private class TabbedPane extends JPanel {
		/**Tab per l'utilizzo delle funzionalità sulla base di dati*/
		private JPanelCluster panelDB;
		
		/**Tab per l'utilizzo delle funzionalità sul file*/
		private JPanelCluster panelFile;
		
		/**
		 * <p>Title: KmeansClient</p>
		 * <p>Description: Il progetto KmeansClient realizza un sistema Client in grado di collegarsi al Server tramite l'indirizzo ip e il numero
		 * di porta su cui il Server è in ascolto. Una volta instaurata la connessione l'utente può scegliere se avviare un nuovo processo di 
		 * clustering oppure recuperare cluster precedentemente serializzati in un qualche file.</p>
		 * <p>Copyright: Copyright (c) 2018</p>
		 * <p>Company: Dipartimento di Informatica, Università degli studi di Bari</p>
		 * <p>Class description: JPanelCluster<br>
		 * Definizione della Inner Class JPanelCluster che estende la classe JPanel e che rappresenta ogni singola sezione (tab) che fa 
		 * parte dell'interfaccia grafica. La sezione creata verrà poi aggiunta ad un JTabbedPane. Ciascuna sezione contiene al suo 
		 * interno:<br> 
		 * - due caselle di testo (una per il nome della tabella e una per il numero di cluster)<br>
		 * - un'area di testo scorrevole non editabile che conterrà il risultato da parte del Server<br>
		 * - un bottone per l'invio della richiesta al Server (che è diversa per ciascun tab)
		 * </p>
		 * @author Gianfranco Brescia
		 * @version 1.0
		 */
		private class JPanelCluster extends JPanel {
			/**
			 * Casella di testo in cui verrà inserito il nome della tabella. Tale nome coinciderà sia con il nome della tabella del 
			 * database sia con il nome del file creato sul Server.
			 */
			private JTextField tableText = new JTextField(20);
			
			/**Casella di testo in cui verrà inserito il numero di cluster che si intende scoprire*/
			private JTextField kText = new JTextField(10);
			
			/**Area di testo non editabile che conterrà l'output fornito dal Server*/
			private JTextArea clusterOutput = new JTextArea();
			
			/**
			 * Bottone che si occuperà di effettuare una particolare richiesta al Server. A questo bottone verrà associato un nome e 
			 * un ascoltatore (entrambi passati come argomenti al costruttore).
			 */
			private JButton executeButton;
			
			/**
			 * Questo metodo è il costruttore della classe JPanelCluster che si occupa di creare ciascuna sezione (tab) inizializzando 
			 * il pannello come segue:<br>
			 * - il JPanel upPanel (panel iniziale) conterrà le due textfield per la lettura del nome della tabella e il numero di cluster<br>
			 * - il JPanel centralPanel (panel centrale) conterrà l'area di testo scorrevole non editabile contenente l'output del Server<br>
			 * - il JPanel downPanel (panel finale) conterrà il bottone per l'invio della richiesta al Server<br>
			 * Inoltre questo metodo aggiunge l'ascoltatore a al bottone executeButton.
			 * @param buttonName Nome da assegnare al bottone executeButton
			 * @param a Ascoltatore da assegnare al bottone 
			 */
			JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				super(new FlowLayout());
				JScrollPane outputScrollPane = new JScrollPane(clusterOutput);
				JPanel mainPanel = new JPanel();
				mainPanel.setPreferredSize(new Dimension(600, 400));
				mainPanel.setLayout(new BorderLayout());
				
				JPanel upPanel = new JPanel();
				JPanel centralPanel = new JPanel();
				JPanel downPanel = new JPanel();
				//upPanel
				upPanel.add(new JLabel("Table: "));
				upPanel.add(tableText);
				upPanel.add(new JLabel("k: "));
				upPanel.add(kText);
				clusterOutput.setEditable(false);
				clusterOutput.setLineWrap(true);
				//centralPanel
				centralPanel.setLayout(new BorderLayout());
				centralPanel.add(outputScrollPane, BorderLayout.CENTER);
				//downPanel
				downPanel.setLayout(new FlowLayout());
				downPanel.add(executeButton = new JButton(buttonName));
				
				mainPanel.add(upPanel, BorderLayout.NORTH);
				mainPanel.add(centralPanel, BorderLayout.CENTER);
				mainPanel.add(downPanel, BorderLayout.SOUTH);
				add(mainPanel);
				executeButton.addActionListener(a);
			}
		}
		
		/**
		 * Questo metodo è il costruttore della classe TabbedPane che inizializza i membri panelDB e panelFile e li aggiunge ad un oggetto
		 * istanza della classe TabbedPane. Tale oggetto (di istanza TabbedPane) è quindi inserito nel pannello che si sta costruendo.
		 */
		TabbedPane() {
			super(new GridLayout(1,1));
			JTabbedPane jTabbedPane = new JTabbedPane();
			java.net.URL imgURL = getClass().getResource("db.jpg");
			ImageIcon iconDB = new ImageIcon(imgURL);
			panelDB = new JPanelCluster("MINE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						learningFromDBAction();
					}catch(SocketException e1) {
						JOptionPane.showMessageDialog(panelFile, "Errore! Impossibile connettersi al Server\n\nDettagli:\n" + e1);
					}catch(IOException e1) {
						JOptionPane.showMessageDialog(panelFile, e1);
					}catch(ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(panelFile, e1);
					}
				}
			});
			jTabbedPane.addTab("DB", iconDB, panelDB, "Kmeans from Database");
			imgURL = getClass().getResource("file.jpg");
			ImageIcon iconFile = new ImageIcon(imgURL);
			panelFile = new JPanelCluster("STORE FROM FILE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						learningFromFileAction();
					}catch(SocketException e1) {
						JOptionPane.showMessageDialog(panelFile, "Errore! Impossibile connettersi al Server\n\nDettagli:\n" + e1);
					}catch(IOException e1) {
						JOptionPane.showMessageDialog(panelFile, e1);
					}catch(ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(panelFile, e1);
					}
				}
			});
			jTabbedPane.addTab("FILE", iconFile, panelFile, "Kmeans from File");
			add(jTabbedPane);
			jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		
		/**
		 * Questo metodo effettua un'attività di scoperta dei cluster sulla base di dati. Questo metodo acquisisce le due caselle di 
		 * testo corrispondenti al nome della tabella del database e al numero di cluster da scoprire. Qualora il valore 
		 * acquisito non fosse un numero positivo viene visualizzato un messaggio di errore all'interno di una JOptionPane.<br>
		 * Le diverse richieste al server vengono effettuate attraverso una serie di valori numerici:<br>
		 * 1. Il Client invia al Server il comando 0 e il nome della tabella, restando in attesa di una risposta da parte del Server.
		 * Questo comando corrisponde (sul Server) a un'interrogazione sulla base di dati. In caso di risposta diversa da "OK", 
		 * viene visualizzato un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo.<br>
		 * 2. Altrimenti, il Client invia al Server il comando 1 e il numero di cluster da scoprire e aspetta una risposta da parte del
		 * Server. Questo comando corrisponde (sul Server) alla vera e propria attività di scoperta attraverso l'algoritmo kmeans. In caso
		 * di risposta diversa da "OK", viene visualizzato un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo.<br>
		 * 3. Altrimenti il Client legge il numero di iterazioni e i cluster così come sono trasmessi dal Server e li visualizza in  
		 * panelDB.clusterOuput. Il Client invia al Server il comando 2 e aspetta una risposta da parte del Server. Questo comando 
		 * corrisponde (sul Server) al salvataggio su file dell'attività di scoperta per letture successive. In caso di risposta diversa 
		 * da "OK", viene visualizzato un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo, altrimenti viene 
		 * visualizzato un messaggio che confermi il successo dell'attività in una JOptionPane.
		 * @throws SocketException Questa eccezione è sollevata quando termina la comunicazione tra il Client e il Server
		 * @throws IOException Questa eccezione è sollevata in caso di errori nelle operazioni di lettura dei risultati ottenuti dal Server
		 * @throws ClassNotFoundException Questa eccezione è sollevata nelle operazioni di lettura dei risultati da file nel caso in cui
		 * gli oggetti passati sullo stream sono istanza di una classe non presente sul Client
		 */
		private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
			int k;
			String tableName = "";
			String result = "";
			tableName = panelDB.tableText.getText();
			if( tableName.equals("") ) {
				JOptionPane.showMessageDialog(this, "Errore! Inserire il nome della tabella");
				return;
			}
			try {
				k = new Integer(panelDB.kText.getText()).intValue();
			}catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(this, e.toString());
				return;
			}
			out.writeObject(0);
			out.writeObject(tableName);
			result = (String)in.readObject();
			if( !result.equals("OK") ) {
				JOptionPane.showMessageDialog(this, result);
				return;
			}else {
				out.writeObject(1);
				out.writeObject(k);
				result = (String)in.readObject();
				if( !result.equals("OK") ) {
					JOptionPane.showMessageDialog(this, result);
					return;
				}else {
					Integer integer = (Integer)in.readObject();
					panelDB.clusterOutput.setText("Numero iterate: " + integer + "\n\n" + (String)in.readObject());
					out.writeObject(2);
					result = (String)in.readObject();
					if( !result.equals("OK") ) {
						JOptionPane.showMessageDialog(this, result);
						return;
					}else 
						JOptionPane.showMessageDialog(this, "Attività effettuata con successo!");
				}
			}
		}
		
		/**
		 * Questo metodo effettua la lettura di un determinato file sul Server. Questo metodo acquisisce il nome della tabella 
		 * (da panelFile.tableText) e il numero dei cluster (da panelFile.kText). Il Client invia al Server il comando 3, il nome della
		 * tabella e il numero di cluster e aspetta una risposta da parte del Server. In caso di risposta diversa da "OK", viene 
		 * visualizzato un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo, altrimenti viene visualizzato, in una 
		 * JOptionPane, un messaggio che confermi il successo dell'attività.
		 * @throws SocketException Questa eccezione è sollevata quando termina la comunicazione tra il Client e il Server
		 * @throws IOException Questa eccezione è sollevata in caso di errori nelle operazioni di lettura dei risultati ottenuti dal Server
		 * @throws ClassNotFoundException Questa eccezione è sollevata nelle operazioni di lettura dei risultati da file nel caso in cui
		 * gli oggetti passati sullo stream sono istanza di una classe non presente sul Client
		 */
		private void learningFromFileAction() throws SocketException, IOException, ClassNotFoundException {
			String tableName = panelFile.tableText.getText();
			String numberOfCluster = panelFile.kText.getText();
			String result = "";
			if( tableName.equals("") ) {
				JOptionPane.showMessageDialog(this, "Errore! Inserire il nome della tabella");
				return;
			}
			if( numberOfCluster.equals("") ) {
				JOptionPane.showMessageDialog(this, "Errore! Inserire un valore per k");
				return;
			}
			out.writeObject(3);
			out.writeObject(tableName);
			out.writeObject(numberOfCluster);
			result = (String)in.readObject();
			if( !result.equals("OK") ) {
				JOptionPane.showMessageDialog(this, result);
				return;
			}else {
					panelFile.clusterOutput.setText((String)in.readObject());
					JOptionPane.showMessageDialog(this, "Attività effettuata con successo!");
			}
		}
	}
	
	/**
	 * Questo metodo inizializza la componente grafica dell'interfaccia grafica istanziando un oggetto della classe JTabbedPane e 
	 * aggiungendolo al container della JFrame. Inoltre questo metodo avvia la richiesta di connessione al Server ed inizializza i 
	 * flussi di comunicazione (membri in e out).
	 */
	@SuppressWarnings("resource")
	public void init() {
		setTitle("CASO DI STUDIO K-MEANS");
		setSize(700, 500);
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		String ip = this.getParameter("ServerIP");
		int port = 8080;
		try {
			InetAddress addr = InetAddress.getByName(ip);
			Socket socket = new Socket(addr, port);
			TabbedPane tab = new TabbedPane();
			getContentPane().add(tab);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}catch(IOException e) {
			JOptionPane.showMessageDialog(this, "Impossibile connettersi al Server!");
			cp.add(Box.createRigidArea(new Dimension(20, 20)));
			cp.add(new JLabel("Impossibile connettersi al Server!"));
			cp.add(Box.createRigidArea(new Dimension(20,20)));
			cp.add(new JLabel("Non è possibile eseguire alcuna operazione."));
		}
	}
	
	/**
	 * Questo metodo restituisce l'indirizzo ip della macchina che richiede il servizio dati
	 * @param param Indirizzo ip del Server
	 * @return serverAddress
	 */
	private String getParameter(String param) {
		String serverAddress= "127.0.0.1";
		if(param == "ServerIP") 
			serverAddress = JOptionPane.showInputDialog("Inserire un indirizzo IP valido della macchina su cui è\n" + " in esecuzione il servizio dati sulla porta 8080, altrimenti\n" + " verrà utilizzato quello di default(127.0.0.1): ");
		return serverAddress;
	}
	
	/**
	 * Questo metodo istanzia un oggetto di tipo KMeans e permette di visualizzare l'interfaccia grafica definita dal JFrame
	 * @param args Argomento passato in input
	 */
	public static void main(String args[]) {
		KMeans kmeans = new KMeans();
		kmeans.init();
		kmeans.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		kmeans.setVisible(true);
	}
}

