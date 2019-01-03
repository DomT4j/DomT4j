package com.github.domt4j;

import com.github.domt4j.config.Config;

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
	private final static String[]documentTypes= {"html","xml"};
	private final static String[]nodeTypes = {"document","element","comment"};
	public NodeConfiguration() {
		// TODO Auto-generated constructor stub
		this.nodeName = null ;
		this.nodeValue = null ;
		this.nodeType = null ;
		this.append = null ;
		this.rootElementName = null ;
		this.documentType = null ;
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

}
