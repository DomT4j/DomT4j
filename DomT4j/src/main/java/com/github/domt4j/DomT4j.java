package com.github.domt4j;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;

import javax.swing.JOptionPane;
import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.ColorConfig;
import com.github.domt4j.config.Config;
import com.github.domt4j.config.DefaultDomT4jConfig;
import com.github.domt4j.config.DomT4jConfig;

import cloud.jgo.*;
import cloud.jgo.io.File;
import cloud.jgo.jjdom.JjDom;
import cloud.jgo.jjdom.dom.DomColors;
import cloud.jgo.jjdom.dom.nodes.Comment;
import cloud.jgo.jjdom.dom.nodes.Document;
import cloud.jgo.jjdom.dom.nodes.Element;
import cloud.jgo.jjdom.dom.nodes.Node;
import cloud.jgo.jjdom.dom.nodes.NodeList;
import cloud.jgo.jjdom.dom.nodes.html.color.HTMLColorDocument;
import cloud.jgo.jjdom.dom.nodes.xml.XMLDocument;
import cloud.jgo.utils.command.LocalCommand;
import cloud.jgo.utils.command.Parameter;
import cloud.jgo.utils.command.color.ColorLocalCommand;
import cloud.jgo.utils.command.execution.Execution;
import cloud.jgo.utils.command.terminal.TerminalColors;
import cloud.jgo.utils.command.terminal.phase.ColorLocalPhaseTerminal;
import cloud.jgo.utils.command.terminal.phase.DefaultPhase;
import cloud.jgo.utils.command.terminal.phase.LocalPhaseTerminal;

public class DomT4j extends ColorLocalPhaseTerminal {

	private static DomT4j instance = null;
	public static String TERMINAL_NAME = null ;
	public final static File CONF_DIR= new File("conf");  
	public final static File CONF_FILE = new File(CONF_DIR,"domt4j.xml");

	// campi della classe

	private Node currentNode = null;
	
	private DomT4jConfig configuration = null;
	
	public DomT4jConfig getConfiguration() {
		return this.configuration;
	}

	public static DomT4j getDomTerminal() {
		if (instance == null) {
			instance = new DomT4j();
			initTerminal();
		}
		return instance;
	}

	static String error(String msg) {
		return ansi().fg(Color.RED).a(msg + " #").reset().toString();
	}

	static String setOk(String var) {
		return ansi().fg(Color.WHITE).a("The " + var + " is set (" + ansi().fg(Color.CYAN).a("OK").reset() + ")")
				.reset().toString();
	}

	static String positiveMsg(String msg) {
		return ansi().fg(Color.WHITE).a(msg + " (" + ansi().fg(Color.CYAN).a("OK").reset() + ")").reset().toString();
	}

	private DomT4j() {}

	private static void initTerminal() {
		/*
		 * Config XML from HERE To §
		 */
		if (CONF_DIR.exists()) {
			
			// verifico se esiste il file di config
			
			if (CONF_FILE.exists()) {
				instance.configuration = (DomT4jConfig) £.convertFromXMLToObject(CONF_FILE,DomT4jConfig.class);
				System.out.println("Abbiamo correttamente aquisito il file di configurazione ...");
			}
			else {
				// qui devo creare una instanza di configurazione di default e ricreare il file
				System.out.println("Il file di configurazione non esiste #");
				instance.configuration =  new DefaultDomT4jConfig();
				// okok creo il file di configurazione 
				£.convertFromObjectToXML(DomT4jConfig.class,"conf"+File.separator+"domt4j.xml",instance.configuration);
				System.out.println("File di configurazione creato @");
			}
			
		}
		else {
			// non esiste la cartella di configurazione
			// 1 passo : la creo 
			CONF_DIR.mkdir();
			System.out.println("La cartella di configurazione non esiste .. la si crea ...");
			// creo una instanza di configurazione di default 
			instance.configuration =  new DefaultDomT4jConfig();
			// okok creo il file di configurazione 
			£.convertFromObjectToXML(DomT4jConfig.class,"conf"+File.separator+"domt4j.xml",instance.configuration);
			System.out.println("File di configurazione creato @");
		}
		// qui devo prendere tutti i valori della configurazione
		/*
		 		COLORS
		 */
		// command and parameter colors
		ColorConfig parameterColorConfig = (ColorConfig) instance.configuration.colorsConfiguration.getConfigByTarget("parameter");
		ColorConfig commandColorConfig = (ColorConfig) instance.configuration.colorsConfiguration.getConfigByTarget("command");
		ColorConfig phaseColorConfig = (ColorConfig) instance.configuration.colorsConfiguration.getConfigByTarget("phase");
		// configuro i colori inerenti ai comandi, parametri e fasi.
		TerminalColors.PARAMETER_COLOR = parameterColorConfig.color;
		TerminalColors.COMMAND_COLOR = commandColorConfig.color;
		TerminalColors.PHASE_COLOR = phaseColorConfig.color;
		// controllo se i colori sono diversi da un valore null
		/*
 				TERMINAL.NAME
		 */
		TERMINAL_NAME = instance.configuration.terminalName;
		instance.setName(TERMINAL_NAME);
		/*
		 * §
		 */
		instance.getHelpCommands().sort();
		instance.useGeneralHelp();
		LocalCommand.setInputHelpExploitable(true);
		// imposto JjDom in modo tale che si adatti a un documento HTML colorato a
		// livello di sintassi:
		JjDom.documentTypeUsed = HTMLColorDocument.class;
		// imposto i colori dei nodi
		DomColors.tag_color = Color.WHITE;
		DomColors.nodeName_color = Color.CYAN;
		DomColors.comment_color = Color.GREEN;
		DomColors.attribute_value_color = Color.YELLOW;
		final ColorLocalCommand createCommand;
		final ColorLocalCommand cdCommand;
		ColorLocalCommand lsCommand, setCommand, getCommand, markup;
		// 1 comando : create
		createCommand = new ColorLocalCommand("create", "This command creates a node");
		// params :
		final Parameter nodeTypeParam;
		final Parameter documentTypeParam;
		final Parameter charsetDocumentParam;
		final Parameter nodeNameParam;
		Parameter nodeValueParam, appendParam;
		nodeTypeParam = createCommand.addParam("nodeType",
				"Specify the node type - " + j£.colors("(", Color.GREEN)
						+ j£.colors("document" + j£.colors("|", Color.DEFAULT) + j£.colors("element", Color.MAGENTA),
								Color.MAGENTA)
						+ j£.colors("|", Color.DEFAULT) + j£.colors("comment", Color.MAGENTA)
						+ j£.colors(")", Color.GREEN));
		nodeNameParam = createCommand.addParam("nodeName", "Specify the node name");
		nodeValueParam = createCommand.addParam("nodeValue", "Specify the node value");
		appendParam = createCommand.addParam("append", "appends to current node");
		documentTypeParam = createCommand.addParam("type",
				"Specify the document type - " + j£.colors("(", Color.GREEN) + j£
						.colors("html" + j£.colors("|", Color.DEFAULT) + j£.colors("xml", Color.MAGENTA), Color.MAGENTA)
						+ j£.colors(")", Color.GREEN));
		charsetDocumentParam = createCommand.addParam("charset", "Specify the document charset");
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
					System.out.print("Specify the document type" + j£.colors("(", Color.GREEN)
							+ j£.colors("html" + j£.colors("|", Color.DEFAULT) + j£.colors("xml", Color.MAGENTA),
									Color.MAGENTA)
							+ j£.colors(")", Color.GREEN) + ":");
					String documentTypeInput = £._I();
					if (documentTypeInput.equals("html")) {
						JjDom.newDocument().setMinimalTags().useDoctype(true).home().jqueryInit();
						instance.currentNode = JjDom.document;
						return positiveMsg("Document " + j£.colors("HTML", Color.CYAN) + " is created");
					} else if (documentTypeInput.equals("xml")) {
						// chiedo il nome dell'elemento root
						System.out.print("Root element name:");
						String rootElementName = £._I();
						instance.currentNode = new XMLDocument(rootElementName);
						return positiveMsg("Document " + j£.colors("XML", Color.CYAN) + " is created");
					} else {
						return error("Document type is not valid - Available types= xml|html");
					}
				} else if (nodeType.equals("element")) {
					if (instance.currentNode != null) {
						// chiedo il nome dell'elemento
						System.out.print("Element Name:");
						String elementName = £._I();
						Element element = instance.currentNode.getDocument().createElement(elementName);
						if (element != null) {
							// appendo automaticamente
							instance.currentNode.appendChild(element);
							return positiveMsg("element added to the "
									+ j£.colors(instance.currentNode.getNodeName(), Color.CYAN) + " node");
						} else {
							return error("element creation failed");
						}
					} else {
						// da definire ...
					}
				} else if (nodeType.equals("comment")) {
					if (instance.currentNode != null) {
						// chiedo il nome dell'elemento
						System.out.print("Comment text:");
						String commentText = £._I();
						Comment comment = instance.currentNode.getDocument().createComment(commentText);
						instance.currentNode.appendChild(comment);
						return positiveMsg("comment added to the "
								+ j£.colors(instance.currentNode.getNodeName(), Color.CYAN) + " node");
					} else {
						// da definire ...
					}
				} else {
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
				if (inputType != null) {
					boolean result = config.setNodeType(inputType);
					if (result == false) {
						return error("Node type is not valid - Available types= document|element|comment");
					} else {
						// condivido l'oggetto
						createCommand.shareObject(config);
						return setOk(inputType);
					}
				}
				return null;
			}
		});
		// esecuzioni dei parametri:documentType
		documentTypeParam.setExecution(new Execution() {
			@SuppressWarnings("static-access")
			@Override
			public Object exec() {
				// qui mi devo assicurare in tanto che ci sia una config condivisa
				// e inoltre verificare che quest'ultima punti a un documento
				if (createCommand.getSharedObject() != null) {
					// mi assicuro che la config punti a un documento
					if (createCommand.getSharedObject() instanceof NodeConfiguration) {
						// qui diamo per scontato che la configurazione intercettata
						// abbia un valore, poichè senza specificare il tipo di nodo
						// non si può creare proprio il nodo.
						if (((NodeConfiguration) createCommand.getSharedObject()).getNodeType().equals("document")) {
							if (documentTypeParam.getInputValue() != null) {
								boolean result = ((NodeConfiguration) createCommand.getSharedObject())
										.setDocumentType(documentTypeParam.getInputValue());
								if (result == false) {
									return error("Document type is not valid - Available types= html|xml");
								} else {
									if (((NodeConfiguration) createCommand.getSharedObject()).isCompleted()) {
										// sappiamo che la configurazione è completata
										// quindi creo il documento e lo condivido
										Document document = null;
										if (documentTypeParam.getInputValue().equals("html")) {
											JjDom.newDocument().setMinimalTags().useDoctype(true).home().jqueryInit();
											document = JjDom.document;
										} else {
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

						} else {
							// da definire ...
						}
					} else {
						// da definire ...
					}
				} else {
					// da definire ...
				}
				return null;
			}
		});
		// esecuzioni dei parametri:append
		appendParam.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					// ci assicuriamo che ci sia
					// un oggetto condiviso
					if (createCommand.getSharedObject() != null) {
						if (createCommand.getSharedObject() instanceof Node) {
							// appeso @
							instance.currentNode.appendChild((Node) createCommand.getSharedObject());
							createCommand.shareObject(null);
							return positiveMsg("element added to the "
									+ j£.colors(instance.currentNode.getNodeName(), Color.CYAN) + " node");
						} else {
							// da definire ...
							return error("element creation failed");
						}
					} else {
						// da definire ...
					}
				} else {
					// qui non c'è il nodo corrente
					// quindi siamo all'inizio e quindi potrebbe trattarsi di un documento
					if (createCommand.getSharedObject() instanceof Document) {
						// appeso @
						instance.currentNode = createCommand.getSharedObject(); // si tratta di un documento
						createCommand.shareObject(null);
					}
				}
				return null;
			}
		});
		// esecuzioni dei parametri:nodeName
		nodeNameParam.setExecution(new Execution() {

			@Override
			public Object exec() {
				// okok qui ricavo la configurazione se c'è
				if (createCommand.getSharedObject() != null) {

					if (createCommand.getSharedObject() instanceof NodeConfiguration) {

						if (nodeNameParam.getInputValue() != null) {
							// okok imposto il settaggio
							NodeConfiguration config = createCommand.getSharedObject();
							if (config.getNodeType().equals("element")) {
								config.setNodeName(nodeNameParam.getInputValue());
								// sappiamo che è completata la config
								if (config.isCompleted()) {

									// quindi procediamo con la creazione dell'elemento
									Element element = instance.currentNode.getDocument()
											.createElement(config.getNodeName());

									// condivido l'oggetto

									createCommand.shareObject(element);

									return setOk("Node name");
								}
							} else {
								// da definire ...
							}
						}
					} else {
						// da definire ...
					}
				} else {
					// da definire ...
				}
				return null;
			}
		});
		// 2 comando : cd
		cdCommand = new ColorLocalCommand("cd", "this command allows to change node");
		// ha un valore da input
		cdCommand.setInputValueExploitable(true);
		Parameter reset;
		// mi creo i parametri del comando
		Parameter root;
		reset = cdCommand.addParam("reset", "this command takes us back to the document");
		root = cdCommand.addParam("root", "this command takes us back to the root node");
		// mi creo l'esecuzione del comando come prima cosa
		cdCommand.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					// verifico se il nodo corrente ha figli
					if (instance.currentNode.hasChildNodes()) {
						// ottengo il path da input
						String inputPath = cdCommand.getInputValue();
						if (inputPath != null) {
							if (inputPath.equals("..")) {
								Node parent = instance.currentNode.getParentNode();
								if (parent != null) {
									instance.currentNode = parent;
								} else {
									// da definire ...
								}
							} else {
								Node destinationNode = instance.currentNode.getNodeByPath(inputPath);
								if (destinationNode != null) {
									// cambiamo nodo
									instance.currentNode = destinationNode;
								} else
									return error("The indicated path is not valid");
							}
						}
					} else {
						if (cdCommand.getInputValue().equals("..")) {
							Node parent = instance.currentNode.getParentNode();
							if (parent != null) {
								instance.currentNode = parent;
							} else {
								// da definire ...
							}
						} else {
							return error("The current node has not children");
						}
					}
				} else {
					// da definire ...
				}
				return null;
			}
		});

		// esecuzioni dei parametri
		reset.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					Document doc = instance.currentNode.getDocument();
					instance.currentNode = doc;
				} else {
					// da definire ...
				}
				return null;
			}
		});
		root.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					Node rootNode = instance.currentNode.getDocument().getRootElement();
					instance.currentNode = rootNode;
				} else {
					// da definire ...
				}
				return null;
			}
		});
		// terzo comando : ls
		lsCommand = new ColorLocalCommand("ls", "lists all the children of the current node");
		lsCommand.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					NodeList listNodes = instance.currentNode.getChildNodes();
					describeNodes(listNodes);
				} else {
					// da definire ...
				}
				return null;
			}
		});
		// 4 comando:set:imposta valori del nodo
		setCommand = new ColorLocalCommand("set", "\"This command sets\"");
		// 5 comando:markup: ottiene il markup del nodo corrente
		markup = new ColorLocalCommand("markup", "Gets the markup of the current node");
		markup.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					return instance.currentNode.getMarkup();
				} else {
					// da definire ...
					return null;
				}
			}
		});
		// inserisco i comandi nel terminale, quelli generali
		instance.addCommands(createCommand, cdCommand, lsCommand, markup);
	}

	private static void describeNodes(NodeList listNodes) {
		for (int i = 0; i < listNodes.getLength(); i++) {
			System.out.println((i + 1) + ") - NodeName:" + j£.colors(listNodes.item(i).getNodeName(), Color.CYAN)
					+ " - Parent:" + j£.colors(listNodes.item(i).getParentNode().getNodeName(), Color.CYAN)
					+ " - Children:" + listNodes.item(i).getChildNodes().getLength());
		}
	}

	// ridefinisco il metodo getCommandRequest

	@Override
	public String getCommandRequest() {
		StringBuffer myCommandRequest = new StringBuffer();
		// il server ha un nome
		myCommandRequest.append(getName());
		// controllo se abbiamo una fase corrente

		if (currentPhase != null) {

			myCommandRequest.append("_(" + j£.colors(currentPhase.phaseName(), TerminalColors.PHASE_COLOR) + ")_");

		}
		if (currentNode != null) {
			myCommandRequest.append(
					"-<" + j£.colors(currentNode.getNodeName().toUpperCase(), TerminalColors.PHASE_COLOR) + ">_:");
		} else {
			myCommandRequest.append("-<" + j£.colors("NULL", Color.DEFAULT) + ">_:");
		}

		return myCommandRequest.toString();
	}

}
