package com.github.domt4j.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DomT4j.config")
public class DomT4jConfig implements Config{

	
	// 1 elemento : colors.config
	@XmlElement(name="colors.config")
	public ColorsConfig colorsConfiguration ;
	// 2 elemento : banner
	@XmlElement
	public boolean banner;
	// 3 elemento : terminal.name
	@XmlElement(name="terminal.name")
	public String terminalName ;
	
	public DomT4jConfig() {
		// TODO Auto-generated constructor stub
		this.colorsConfiguration = new ColorsConfig();
		this.banner = false ;
		this.terminalName = null ;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return "global";
	}
	
	
	
	 
	
}
