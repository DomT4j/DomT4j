package com.github.domt4j;

import org.fusesource.jansi.Ansi.Color;

import cloud.jgo.j£;
import cloud.jgo.jjdom.JjDom;
import cloud.jgo.jjdom.dom.nodes.Document;

public class DomT4jTest {
	
@SuppressWarnings("static-access")
public static void main(String[] args) {
	
	// Allora primissima cosa, non posso permettere alle fasi connection, migration, ecc ecc di eseguirsi cosi
	// perchè succede che nel momento in cui, ho un documento appena scaricato, effettuo qualche modifica
	// dopodichè emetto un update, e quindi viene eseguita di nuovo la fase download, quindi posso fare in modo
	// tale che il download execution se verifica l'assenza del documento, allora va bene, altrimenti se c'è
	// un documento in elaborazione, e diamo update, sappiamo che non scarica, ma si solo connette e fa l'update 
	// del documento
	
	// per esempio voglio poter settare un oggetto ftpConfiguration globale
	// in modo tale da migrare/aggiornare senza dover specificare dati
	// prossimo passo : gestire la multi utenza, in attesa che si risolva in jGo 1.0.9
	// la feature che ci permette di condividere oggetti creando parametri automatici
	// in base ai campi dell'oggetto condiviso, questo approccio non lo utilizzeremo però
	// per la creazione dei nodi, quindi con il comando create, ma ci servirà per altre features
	// di DomT4j, e comunue è un di + jGo.
	// adesso ci occupiamo di questo primo bug
	
	// dobbiamo eliminare tutti i parametri aggiunti al comando, una volta che l'oggetto condiviso viene meno
	// 0) Dobbiamo impedire di aggiungere due elementi nel documento, ma costringere
	// l'utente ha aggungere solo un elemento nel document, eppoi aggiungere al root
	// 1) devo fare la possibilità di cambiare il parametro toString, o meglio il suo nome
	// 2) Ora devo migliorare tutto quello che abbiamo fatto finora
	
	DomT4j term = DomT4j.getDomTerminal();
	if (args.length>0) {
		// se ci sono argomento, esegui il terminale solo se l'url/path del documento è corretto e se si riesce a scaricare il documento
		String documentPath = args[0];
		JjDom.connect("localhost", "wasp91", "wasp91dayno");
		if (JjDom.isConnected()) {
			System.out.println(term.positiveMsg(j£.colors("[",Color.DEFAULT)+j£.colors("*",Color.CYAN)+j£.colors("]",Color.DEFAULT)+" Successfully connected"));
			Document doc  = JjDom.download("upload/test/test.xml");
			if (doc!=null) {
				System.out.println(term.positiveMsg(j£.colors("[",Color.DEFAULT)+j£.colors("*",Color.CYAN)+j£.colors("]",Color.DEFAULT)+" Document successfully downloaded"));
				term.currentNode = doc ;
				term.open();
				// chiudo la connessione
				JjDom.closeConnection();
			}
			else {
				System.out.println(term.error("problems with downloading"));
			}
		}
		else {
			System.out.println(term.error("No connection"));
		}
	}
	else {
		term.open();	
	}
}
}
