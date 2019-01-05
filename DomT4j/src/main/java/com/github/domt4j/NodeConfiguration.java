package com.github.domt4j;

import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.Config;

import cloud.jgo.utils.command.color.ColorLocalCommand;

public class NodeConfiguration implements Config{
	
	// questa classe serve per creare
	// giusto le variabili necessarie minimali
	// per la creazione di un dato tipo di nodo.
	// quindi si occupa di filtrare le info necessarie in base al nodo
	// che si sta creando.
	
	private String nodeName ;
	private String nodeValue;
	private String nodeType ;
	private String rootElementName; // valido per xml
	private String append;
	private String documentType ;
	private String attribute ;
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
			return true ;
		}
		else {
			return false ;
		}
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
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
		boolean configurated = false ;
		if (nodeType!=null) {
			if (nodeType.equals("document")) {
				if (documentType!=null) {
					configurated = true;
				}
			}
			else if(nodeType.equalsIgnoreCase("element")){
				if (nodeName!=null) {
					configurated = true ;
				}
			}
			else{
				// comment
				if (nodeValue!=null) {
					configurated = true ;
				}
			}
		}
		return configurated ;
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
