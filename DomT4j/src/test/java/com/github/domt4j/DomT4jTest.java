package com.github.domt4j;

public class DomT4jTest {
	
public static void main(String[] args) {
	
	// 1 passo : per approvare le modifiche jGo, creare i comandi per impostare i nodi specifici xml/html
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
	
	term.open();
	
	
	
}
}
