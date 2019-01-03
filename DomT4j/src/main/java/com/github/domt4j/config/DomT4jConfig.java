package com.github.domt4j.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.domt4j.config.colors.ColorsConfig;

@XmlRootElement(name = "DomT4j.config")
public class DomT4jConfig implements Config {

	// 1 elemento : colors.config
	@XmlElement(name = "colors.config")
	public ColorsConfig colorsConfiguration;
	// 2 elemento : terminal.name
	@XmlElement(name = "terminal.name")
	public String terminalName;
	// 3 elemento : phase.visible
	@XmlElement(name = "phase.visible")
	public boolean phaseVisible;

	public DomT4jConfig() {
		// TODO Auto-generated constructor stub
		this.colorsConfiguration = new ColorsConfig();
		this.terminalName = null;
		this.phaseVisible = true;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return "global";
	}

}
