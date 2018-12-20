package com.github.domt4j.config;

import java.util.ArrayList;

import org.fusesource.jansi.Ansi.Color;

import cloud.jgo.£;

public class ConfigurationMarshTest{
public static void main(String[] args) {
	
	// default configurazione :marsh
	
	DomT4jConfig config = new DomT4jConfig();
	
	
	£.convertFromObjectToXML(DomT4jConfig.class,"DomT4j.xml",config);
	
	System.out.println("Configuration created @");
	
}
}
