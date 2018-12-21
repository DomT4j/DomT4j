package com.github.domt4j;

public class DomT4jTest {
	
public static void main(String[] args) {
	
	// trovato bug grave:preview(): mi esegue il file colorato: potrei risolvere facendo si
	// che il markup del documento rimanga intatto, e la ColorCommand avrà un markup separato
	// che sarà quello colorato, gestire assolutamente, altrimenti poi ci saranno altri bugs.
	// Primissima cosa da fare , sviluppare i comandi nodeValue e attribute
	// 1 bug : risolvere il fatto che se un comando non ha parametri, non ne viene stampato il suo help
	// poi studiarmi questa istruzione : DomT4jTest.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);

	DomT4j term = DomT4j.getDomTerminal();
	
	term.open();
	
	
	
}
}
