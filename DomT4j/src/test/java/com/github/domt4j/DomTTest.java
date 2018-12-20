package com.github.domt4j;

public class DomTTest {
public static void main(String[] args) {
	// dobbiamo risolvere quando chiamiamo l'help generale
	// due cose :
	// 0: okok ora dobbiamo creare elementi dom colorati
	// 1: i primi comandi non sono descritti bene e anche la cornicetta
	// 2: lascia troppe andate a capo prima della richiesta dell'atro comando
	
	DomT4j t = DomT4j.getDomTerminal();
	t.open();
}
}
