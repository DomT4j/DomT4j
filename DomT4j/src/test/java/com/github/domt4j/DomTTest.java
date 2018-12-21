package com.github.domt4j;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;

public class DomTTest {
	 public static void main (String [] args) throws IOException, InterruptedException, URISyntaxException{
	        Console console = System.console();
	        if(console == null && !GraphicsEnvironment.isHeadless()){
	            String filename = DomT4jTest.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
	            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
	        }else{
	            DomT4jTest.main(new String[0]);
	            System.out.println("Program has ended, please type 'exit' to close the console");
	        }
	    }
}
