package com.github.domt4j.config.colors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fusesource.jansi.Ansi.Color;

import cloud.jgo.utils.command.annotations.Configurable;

@XmlRootElement(name = "color.config")
public class ColorConfig implements Configurable {

	@XmlElement
	public Color color;
	@XmlElement
	public String target;

	public ColorConfig(Color color, String target) {
		this.color = color;
		this.target = target;
	}

	public ColorConfig() {
		this.color = null;
		this.target = null;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}

	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false; // da definire ...
	}

	public Class<? extends Configurable> getTargetType() {
		// TODO Auto-generated method stub
		return getClass();
	}

}
