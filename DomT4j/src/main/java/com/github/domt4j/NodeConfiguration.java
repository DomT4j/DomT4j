package com.github.domt4j;

import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.Config;

import cloud.jgo.utils.command.annotations.£Command;
import cloud.jgo.utils.command.color.ColorLocalCommand;
@£Command(command="Node",help = "This command cretes a node", involveAll=true)
public class NodeConfiguration implements Config{
	
	// Provvisoria, può contenere bugs
	private String nodeName ;
	private String nodeValue;
	private String nodeType ;
	private String rootElementName; // valido per xml
	private String append;
	private String documentType ;
	private String attribute ;
	private boolean completed ;
	final static String[]documentTypes= {"html","xml"};
	final static String[]nodeTypes = {"document","element","comment"};
	public NodeConfiguration() {
		// TODO Auto-generated constructor stub
		this.nodeName = null ;
		this.nodeValue = null ;
		this.nodeType = null ;
		this.append = null ;
		this.rootElementName = null ;
		this.documentType = null ;
		this.attribute = null ;
		this.completed = false ;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getDocumentType() {
		return documentType;
	}
	
	public boolean setDocumentType(String documentType) {
		if (documentType.equals(documentTypes[0])||documentType.equals(documentTypes[1])) {
			this.documentType = documentType;
			if (this.getNodeType().equals("document")&&documentType!=null) {
				this.completed = true ;
			}
		}
		return this.completed ;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
		if (this.nodeType.equals("element")&&nodeName!=null) {
			this.completed = true ;
		}
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
		if (this.nodeType.equals("comment")&&nodeValue!=null) {
			this.completed = true ;
		}
	}

	public String getNodeType() {
		return nodeType;
	}

	public boolean setNodeType(String nodeType) {
		if (nodeType.equals("document")||nodeType.equals("element")||nodeType.equals("comment")) {
			this.nodeType = nodeType;
			return true ;
		}
		else {
			return false ;
		}
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public boolean isCompleted() {
		return this.completed ;
	}

	public String getTarget() {
		// TODO Auto-generated method stub
		return "node";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
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

}
