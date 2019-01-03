package com.github.domt4j;

public class DomT4jTest {
	
public static void main(String[] args) {
	
	// 1 cosa da risolvere : quando tentiamo di spostarci in un altro nodo xml, mediante "cd", mi da errore
	// non viene settato il nodeName del nodo, vediamo dove è situato questo bug ...
	// 1 cosa da risolvere : il comando ex non ha un help
	
	
	DomT4j term = DomT4j.getDomTerminal();
	
	term.open();
	
	
	
}
}
