package com.github.domt4j.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.domt4j.config.colors.ColorsConfig;

import cloud.jgo.utils.command.annotations.Configurable;

@XmlRootElement(name = "DomT4j.config")
public class DomT4jConfig implements Configurable {

	public DomT4jConfig() {
		this.colorsConfiguration = new ColorsConfig();
		this.terminalName = null;
		this.phaseVisible = true;
		this.firstPhaseVisible = true;
	}

	// 1 elemento : colors.config
	@XmlElement(name = "colors.config")
	public ColorsConfig colorsConfiguration;
	// 2 elemento : terminal.name
	@XmlElement(name = "terminal.name")
	public String terminalName;
	// 3 elemento : phase.visible
	@XmlElement(name = "phase.visible")
	public boolean phaseVisible;
	@XmlElement(name = "first.phase.visible")
	public boolean firstPhaseVisible;

	public String getTarget() {
		// TODO Auto-generated method stub
		return "global";
	}

	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false ; // da definire ...
	}

	public Class<? extends Configurable> getTargetType() {
		// TODO Auto-generated method stub
		return getClass();
	}

}
