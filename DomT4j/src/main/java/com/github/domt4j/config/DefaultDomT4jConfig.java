package com.github.domt4j.config;

import java.util.ArrayList;

import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.colors.ColorConfig;

public class DefaultDomT4jConfig extends DomT4jConfig{

	public DefaultDomT4jConfig() {
		/* terminal.name*/
		terminalName = "DomT4j";
		/*colors config :*/
		// parameters and commands :
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.YELLOW,"parameter"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.CYAN,"command"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.MAGENTA,"phase"));
		// nodes :
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.DEFAULT,"nodeName"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.YELLOW,"attribute_value"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.DEFAULT,"tag"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.BLUE,"comment"));
		colorsConfiguration.colorConfig.add(new ColorConfig(Color.DEFAULT,"nodeValue"));
	}
		
}
