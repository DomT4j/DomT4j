package com.github.domt4j;

public class DomT4jTest {
	
public static void main(String[] args) {
	
	// 
	// Primissima cosa da fare , sviluppare i comandi nodeValue e attribute
	// 1 bug : risolvere il fatto che se un comando non ha parametri, non ne viene stampato il suo help
	// poi studiarmi questa istruzione : DomT4jTest.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);

	DomT4j term = DomT4j.getDomTerminal();
	
	term.open();
	
	
	
}
}
