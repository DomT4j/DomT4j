package com.github.domt4j;

import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.Ansi.Color;

import cloud.jgo.£;
import cloud.jgo.jjdom.JjDom;
import cloud.jgo.jjdom.dom.nodes.Comment;
import cloud.jgo.jjdom.dom.nodes.Document;
import cloud.jgo.jjdom.dom.nodes.Element;
import cloud.jgo.jjdom.dom.nodes.Node;
import cloud.jgo.jjdom.dom.nodes.NodeList;
import cloud.jgo.jjdom.dom.nodes.xml.XMLDocument;
import cloud.jgo.utils.ColorString;
import cloud.jgo.utils.command.LocalCommand;
import cloud.jgo.utils.command.Parameter;
import cloud.jgo.utils.command.execution.Execution;
import cloud.jgo.utils.command.terminal.phase.DefaultPhase;
import cloud.jgo.utils.command.terminal.phase.LocalPhaseTerminal;

public class DomT4j extends LocalPhaseTerminal{

	// esiste solo una instanza abbiamo detto 
	// 1 cosa da risolvere : mi serve un oggetto configurazione
	// per la creazione dei nodi e documenti, cosi da avere flessibilit£
	
	private static DomT4j instance = null ;
	public final static String TERMINAL_NAME = £.colors("DomT4j",Color.GREEN);
	
	// campi della classe 
	
	private Node currentNode = null;
	
	public static DomT4j getDomTerminal() {
		if (instance == null) {
			instance = new DomT4j();
			initTerminal();
		}
		return instance ;
	}
	
	private static String error(String msg) {
		return ansi().fg(Color.RED).a(msg+" #").reset().toString();
	}
	
	private static String setOk(String var) {
		return ansi().fg(Color.WHITE).a("The "+var+" is set ("+ansi().fg(Color.CYAN).a("OK").reset()+")").reset().toString();
	}
	
	private static String positiveMsg(String msg) {
		return ansi().fg(Color.WHITE).a(msg+" ("+ansi().fg(Color.CYAN).a("OK").reset()+")").reset().toString();
	}
	
	
	private DomT4j() {}

	private static void initTerminal() {
		instance.setName(TERMINAL_NAME);
		// imposto  i primi settaggi del terminale
		instance.useGeneralHelp();
		LocalCommand.setInputHelpExploitable(true);
		Parameter.color = Color.YELLOW;
		LocalCommand.color = Color.CYAN;
		DefaultPhase.color = Color.MAGENTA;
		LocalCommand createCommand;
		final LocalCommand cdCommand;
		LocalCommand lsCommand, setCommand, getCommand;
		// 1 comando : create
		createCommand = new LocalCommand("create","This command creates a node");
		// params :
		final Parameter nodeTypeParam ;
		Parameter nodeNameParam, nodeValueParam, appendParam;
		nodeTypeParam = createCommand.addParam("nodeType","Specify the node type");
		nodeNameParam = createCommand.addParam("nodeName","Specify the node name");
		nodeValueParam = createCommand.addParam("nodeValue","Specify the node value");
		appendParam = createCommand.addParam("append","appends to current node");
		nodeTypeParam.setInputValueExploitable(true);
		nodeNameParam.setInputValueExploitable(true);
		nodeValueParam.setInputValueExploitable(true);
		appendParam.setInputValueExploitable(true);
		createCommand.setExecution(new Execution() {
			@Override
			public Object exec() {
				System.out.print("Specify the node type:");
				String nodeType = £._I();
				if (nodeType.equals("document")) {
					// chiedo il tipo del documento
					System.out.print("Specify the document type"+£.colors("(",Color.GREEN)+£.colors("html,xml",Color.MAGENTA)+£.colors(")",Color.GREEN)+":");
					String documentTypeInput = £._I();
					if (documentTypeInput.equals("html")) {
						JjDom.newDocument().setMinimalTags().useDoctype(true).home().jqueryInit();
						instance.currentNode = JjDom.document;
						return positiveMsg("Document "+£.colors("HTML",Color.CYAN)+" is created");
					}
					else if(documentTypeInput.equals("xml")) {
						// chiedo il nome dell'elemento root 
						System.out.print("Root element name:");
						String rootElementName = £._I();
						instance.currentNode = new XMLDocument(rootElementName);
						return positiveMsg("Document "+£.colors("XML",Color.CYAN)+" is created");
					}
					else {
						return error("Document type is not valid - Available types= xml|html");
					}
				}
				else if(nodeType.equals("element")) {
					if (instance.currentNode!=null) {
						// chiedo il nome dell'elemento 
						System.out.print("Element Name:");
						String elementName = £._I();
						Element element = instance.currentNode.getDocument().createElement(elementName);
						if (element!=null) {
							// appendo automaticamente
							instance.currentNode.appendChild(element);
							return positiveMsg("element added to the "+£.colors(instance.currentNode.getNodeName(), Color.CYAN)+" node");
						}
						else {
							return error("element creation failed");
						}
					}
					else {
						// da definire ...
					}
				}
				else if(nodeType.equals("comment")) {
					if (instance.currentNode!=null) {
						// chiedo il nome dell'elemento 
						System.out.print("Comment text:");
						String commentText = £._I();
						Comment comment = instance.currentNode.getDocument().createComment(commentText);
						instance.currentNode.appendChild(comment);
						return positiveMsg("comment added to the "+£.colors(instance.currentNode.getNodeName(), Color.CYAN)+" node");
					}
					else {
						// da definire ...
					}
				}
				else {
					return error("Node type is not valid - Available types= document|element|comment");
				}
				return null;
			}
		});
		// esecuzioni dei parametri
		nodeTypeParam.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode!=null) {
					String inputType = nodeTypeParam.getInputValue();
					if (inputType!=null) {
						Node node = null ;
						if (inputType.equals("document")) {
							// questo caso lo facciamo all'ultimo
							// poich£ £ quello + problematico
						}
						else if(inputType.equals("element")) {
							
						}
						else if(inputType.equals("comment")) {
							
						}
						else {
							return error("Node type is not valid - Available types= document|element|comment");
						}
					}
				}
				else {
					// da definire ...
				}
				return null ;
			}
		});
		// 2 comando : cd
		cdCommand = new LocalCommand("cd","this command allows to change node");
		// ha un valore da input 
		cdCommand.setInputValueExploitable(true);
		Parameter reset;
		// mi creo i parametri del comando
		Parameter root ;
		reset = cdCommand.addParam("reset","this command takes us back to the document");
		root = cdCommand.addParam("root","this command takes us back to the root node");
		// mi creo l'esecuzione del comando come prima cosa
		cdCommand.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode!=null) {
					// verifico se il nodo corrente ha figli
					if (instance.currentNode.hasChildNodes()) {	
						// ottengo il path da input 
						String inputPath = cdCommand.getInputValue();
						if (inputPath!=null) {
							Node destinationNode = instance.currentNode.getNodeByPath(inputPath);
							if (destinationNode!=null) {
								// cambiamo nodo
								instance.currentNode = destinationNode;
							}
							else 
								return error("The indicated path is not valid");
						}
					}
					else {
						return error("The current node has no children");
					}
				}
				else {
					// da definire ...
				}
				return null ;
			}
		});
		
		// esecuzioni dei parametri 
		reset.setExecution(new Execution() {
			
			@Override
			public Object exec() {
			if (instance.currentNode!=null) {
				Document doc = instance.currentNode.getDocument();
				instance.currentNode = doc ;
			}
			else {
				// da definire ...
			}
			return null ;
			}
		});
		root.setExecution(new Execution() {
			@Override
			public Object exec() {
			if (instance.currentNode!=null) {
				Node rootNode = instance.currentNode.getDocument().getRootElement();
				instance.currentNode = rootNode ;
			}
			else {
				// da definire ...
			}
			return null ;
			}
		});
		// terzo comando : ls
		lsCommand = new LocalCommand("ls","lists all the children of the current node");
		lsCommand.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode!=null) {
					NodeList listNodes = instance.currentNode.getChildNodes();
					describeNodes(listNodes);
				}
				else {
					// da definire ...
				}
				return null ;
			}
		});
		// 4 comando:set:imposta valori del nodo
		setCommand = new LocalCommand("set","\"This command sets\"");
		// inserisco i comandi nel terminale, quelli generali
		instance.addCommands(createCommand,cdCommand,lsCommand);
	}
	
	private static void describeNodes(NodeList listNodes) {
		for (int i = 0; i < listNodes.getLength(); i++) {
			System.out.println((i+1)+") - NodeName:"+£.colors(listNodes.item(i).getNodeName(),Color.CYAN)+" - Parent:"+£.colors(listNodes.item(i).getParentNode().getNodeName(),Color.CYAN)+" - Children:"+listNodes.item(i).getChildNodes().getLength());
		}
	}
	
	// ridefinisco il metodo getCommandRequest
	
	@Override
	public String getCommandRequest() {
		StringBuffer myCommandRequest = new StringBuffer();
			// il server ha un nome 
			myCommandRequest.append(getName());
			// controllo se abbiamo una fase corrente
			
			if (currentPhase!=null) {
				
				myCommandRequest.append("_("+£.colors(currentPhase.phaseName(),cloud.jgo.utils.command.terminal.phase.DefaultPhase.color)+")_");
				
			}
			if (currentNode!=null) {
				myCommandRequest.append("-<"+£.colors(currentNode.getNodeName().toUpperCase(),cloud.jgo.utils.command.LocalCommand.color)+">_:");
			}
			else {
				myCommandRequest.append("-<"+£.colors("NULL",Color.DEFAULT)+">_:");
			}
		
		return myCommandRequest.toString();
	}
	
}
