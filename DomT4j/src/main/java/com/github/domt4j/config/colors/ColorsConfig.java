package com.github.domt4j.config.colors;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import cloud.jgo.utils.command.annotations.Configurable;

@XmlRootElement(name = "colors.config")
public class ColorsConfig implements Colors, Configurable {
	@XmlElement(name = "color.config")
	public List<ColorConfig> colorConfig;

	public ColorsConfig() {
		this.colorConfig = new ArrayList<ColorConfig>();
	}

	public Configurable getConfigByTarget(String target) {
		Configurable config = null;
		for (ColorConfig conf : colorConfig) {
			if (conf.getTarget().equals(target)) {
				config = conf;
				break;
			}
		}
		return config;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return "colors";
	}

	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false; // da definire ...
	}

	public Class<? extends Configurable> getTargetType() {
		// TODO Auto-generated method stub
		return null;
	}
}
