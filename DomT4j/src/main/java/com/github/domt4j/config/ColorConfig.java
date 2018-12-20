package com.github.domt4j.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fusesource.jansi.Ansi.Color;
@XmlRootElement(name="color.config")
public class ColorConfig implements Config{

	@XmlElement
	Color color ;
	@XmlElement
	String target ;
	
	public ColorConfig() {
		// TODO Auto-generated constructor stub
		this.color = null ;
		this.target = null ;
	}

	public ColorConfig(Color color, String target) {
		this.color = color;
		this.target = target;
	}
	
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target ;
	}
	
}
