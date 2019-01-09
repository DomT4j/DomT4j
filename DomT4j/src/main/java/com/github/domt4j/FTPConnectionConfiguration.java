package com.github.domt4j;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.Config;

import cloud.jgo.j£;
import cloud.jgo.utils.ColorString;
import cloud.jgo.utils.command.annotations.CommandClass;
import cloud.jgo.utils.command.color.ColorLocalCommand;
@XmlRootElement(name="server.config")
@CommandClass(command="connection", help = "ftp connection config", involveAll=true)
public class FTPConnectionConfiguration implements Config{
	
	private String host,username,password ;

	public String getTarget() {
		// TODO Auto-generated method stub
		return "ftp_server";
	}
	
	public FTPConnectionConfiguration() {
		// TODO Auto-generated constructor stub
		this.host = null ;
		this.username =null ;
		this.password = null ;
	}

	@XmlElement
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	@XmlElement
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
//		ColorString buffer = new ColorString();
//		buffer.append("------------------------------------------------------------------------\n");
//		buffer.append("FTP-Server-configuration\n");
//		buffer.append("------------------------------------------------------------------------\n");
//		buffer.append("° Ftp-Host",Color.YELLOW).append("=",Color.WHITE).append(this.host,Color.DEFAULT).append("  ")
//		      .append("° Ftp-User",Color.YELLOW).append("=",Color.WHITE).append(this.username,Color.DEFAULT).append("  ")
//		      .append("° Ftp-Passw",Color.YELLOW).append("=",Color.WHITE).append(this.password,Color.DEFAULT).append("\n");
//		return buffer.toString();
		String result = null ;
		try {
			result =  ColorLocalCommand.toString(this,Color.DEFAULT,Color.CYAN);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result ;
	}

	public boolean isCompleted() {
		if (this.host!=null&&this.username!=null&&this.password!=null)return true ;
		else return false ;
	}
	
	
}
