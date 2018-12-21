package com.github.domt4j.config.colors;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.domt4j.config.Config;

@XmlRootElement(name="colors.config")
public class ColorsConfig implements Config,Colors{

	@XmlElement(name="color.config")
	public
	List<ColorConfig> colorConfig;

	public ColorsConfig() {
		// TODO Auto-generated constructor stub
		this.colorConfig = new ArrayList<ColorConfig>();
	}
	public Config getConfigByTarget(String target) {
		Config config = null ;
		for (ColorConfig conf: colorConfig) {
			if (conf.getTarget().equals(target)) {
				config = conf ;break;
			}
		}
		return config;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return "colors";
	}

}
