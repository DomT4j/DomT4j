package com.github.domt4j;

import static org.fusesource.jansi.Ansi.ansi;

import javax.swing.JOptionPane;

import org.fusesource.jansi.Ansi.Color;


import cloud.jgo.*;
import cloud.jgo.jjdom.JjDom;
import cloud.jgo.jjdom.dom.nodes.Comment;
import cloud.jgo.jjdom.dom.nodes.Document;
import cloud.jgo.jjdom.dom.nodes.Element;
import cloud.jgo.jjdom.dom.nodes.Node;
import cloud.jgo.jjdom.dom.nodes.NodeList;
import cloud.jgo.jjdom.dom.nodes.xml.XMLDocument;
import cloud.jgo.utils.command.LocalCommand;
import cloud.jgo.utils.command.Parameter;
import cloud.jgo.utils.command.execution.Execution;
import cloud.jgo.utils.command.terminal.phase.DefaultPhase;
import cloud.jgo.utils.command.terminal.phase.LocalPhaseTerminal;



public class DomT4j extends LocalPhaseTerminal{

	
	// per prima cosa, risolvere bug quando impostiamo il nome di un elemento
	// dopo la creazione di un documento
	
	
	
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
	
	static String error(String msg) {
		return ansi().fg(Color.RED).a(msg+" #").reset().toString();
	}
	
	static String setOk(String var) {
		return ansi().fg(Color.WHITE).a("The "+var+" is set ("+ansi().fg(Color.CYAN).a("OK").reset()+")").reset().toString();
	}
	
	static String positiveMsg(String msg) {
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
		final LocalCommand createCommand;
		final LocalCommand cdCommand;
		LocalCommand lsCommand, setCommand, getCommand;
		// 1 comando : create
		createCommand = new LocalCommand("create","This command creates a node");
		// params :
		final Parameter nodeTypeParam ;
		final Parameter documentTypeParam;
		final Parameter charsetDocumentParam;
		Parameter nodeNameParam, nodeValueParam, appendParam;
		nodeTypeParam = createCommand.addParam("nodeType","Specify the node type");
		nodeNameParam = createCommand.addParam("nodeName","Specify the node name");
		nodeValueParam = createCommand.addParam("nodeValue","Specify the node value");
		appendParam = createCommand.addParam("append","appends to current node");
		documentTypeParam = createCommand.addParam("type","Specify the document type");
		charsetDocumentParam = createCommand.addParam("charset","Specify the document charset");
		nodeTypeParam.setInputValueExploitable(true);
		nodeNameParam.setInputValueExploitable(true);
		nodeValueParam.setInputValueExploitable(true);
		documentTypeParam.setInputValueExploitable(true);
		charsetDocumentParam.setInputValueExploitable(true);
		createCommand.setExecution(new Execution() {
			@SuppressWarnings("static-access")
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
		// esecuzioni dei parametri:nodeType
		nodeTypeParam.setExecution(new Execution() {
			@Override
			public Object exec() {
				
					String inputType = nodeTypeParam.getInputValue();
					// in questo comando si crea il nodo, quindi la configurazione
					NodeConfiguration config = new NodeConfiguration();
					if (inputType!=null) {
						boolean result = config.setNodeType(inputType);
						if (result==false) {
							return error("Node type is not valid - Available types= document|element|comment");
						}
						else {
							// condivido l'oggetto
							createCommand.shareObject(config);
							return setOk(inputType);
						}
					}
				return null ;
			}
		});
		// esecuzioni dei parametri:documentType
		documentTypeParam.setExecution(new Execution() {
			@SuppressWarnings("static-access")
			@Override
			public Object exec() {
				// qui mi devo assicurare in tanto che ci sia una config condivisa
				// e inoltre verificare che quest'ultima punti a un documento
				if (createCommand.getSharedObject()!=null) {
					// mi assicuro che la config punti a un documento 
					if (createCommand.getSharedObject()instanceof NodeConfiguration) {
						// qui diamo per scontato che la configurazione intercettata
						// abbia un valore, poichè senza specificare il tipo di nodo
						// non si può creare proprio il nodo.
						if (((NodeConfiguration)createCommand.getSharedObject()).getNodeType().equals("document")) {
							if (documentTypeParam.getInputValue()!=null) {
								boolean result = ((NodeConfiguration)createCommand.getSharedObject()).setDocumentType(documentTypeParam.getInputValue());
								if (result==false) {
									return error("Document type is not valid - Available types= html|xml");
								}
								else {
									if (((NodeConfiguration)createCommand.getSharedObject()).isCompleted()) {
										// sappiamo che la configurazione è completata
										// quindi creo il documento e lo condivido
										Document document = null ;
										if (documentTypeParam.getInputValue().equals("html")) {
											JjDom.newDocument().setMinimalTags().useDoctype(true).home().jqueryInit();
											document = JjDom.document;
										}
										else {
											// deve essere per forza un documento xml
											document = new XMLDocument();
										}
										// condivido il documento questa volta
										// cosi potrà essere appeso all'occorrenza
										createCommand.shareObject(document);
										return setOk("Document type");
									}
								}
							}
							
						}
						else {
							// da definire ...
						}
					}
					else {
						// da definire ...
					}
				}
				else {
					// da definire ...
				}
				return null ;
			}
		});
		// esecuzioni dei parametri:append
		appendParam.setExecution(new Execution() {
			
			@Override
			public Object exec() {
				if (instance.currentNode!=null) {
					// lo appendiamo nel nodo corrente
				}
				else {
					// qui non c'è il nodo corrente
					// quindi siamo all'inizio e quindi potrebbe trattarsi di un documento
					if (createCommand.getSharedObject()instanceof Document) {
						// okok c'è un documento condiviso, per cui
						instance.currentNode = createCommand.getSharedObject(); // si tratta di un documento
					}
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
