package com.github.domt4j;

import static org.fusesource.jansi.Ansi.ansi;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import org.fusesource.jansi.Ansi.Color;

import com.github.domt4j.config.Config;
import com.github.domt4j.config.DefaultDomT4jConfig;
import com.github.domt4j.config.DomT4jConfig;
import com.github.domt4j.config.colors.ColorConfig;

import cloud.jgo.*;
import cloud.jgo.io.File;
import cloud.jgo.jjdom.JjDom;
import cloud.jgo.jjdom.dom.Colorable;
import cloud.jgo.jjdom.dom.DomColors;
import cloud.jgo.jjdom.dom.nodes.Comment;
import cloud.jgo.jjdom.dom.nodes.Document;
import cloud.jgo.jjdom.dom.nodes.Element;
import cloud.jgo.jjdom.dom.nodes.Node;
import cloud.jgo.jjdom.dom.nodes.NodeList;
import cloud.jgo.jjdom.dom.nodes.html.HTMLComment;
import cloud.jgo.jjdom.dom.nodes.html.HTMLDefaultElement;
import cloud.jgo.jjdom.dom.nodes.html.HTMLDocument;
import cloud.jgo.jjdom.dom.nodes.html.color.HTMLColorDocument;
import cloud.jgo.jjdom.dom.nodes.xml.color.XMLColorDocument;
import cloud.jgo.utils.ColorString;
import cloud.jgo.utils.command.Command;
import cloud.jgo.utils.command.LocalCommand;
import cloud.jgo.utils.command.Parameter;
import cloud.jgo.utils.command.Sharer;
import cloud.jgo.utils.command.color.ColorLocalCommand;
import cloud.jgo.utils.command.execution.Execution;
import cloud.jgo.utils.command.execution.SharedExecution;
import cloud.jgo.utils.command.terminal.TerminalColors;
import cloud.jgo.utils.command.terminal.phase.ColorLocalPhaseTerminal;
import cloud.jgo.utils.command.terminal.phase.DefaultPhase;
import cloud.jgo.utils.command.terminal.phase.LocalPhaseTerminal;
import cloud.jgo.utils.command.terminal.phase.Phase;
import cloud.jgo.utils.command.terminal.phase.Rule;

public class DomT4j extends ColorLocalPhaseTerminal {

	private static DomT4j instance = null;
	public static final String DEFAULT_TERMINAL_NAME = j£.colors("DomT4j", Color.CYAN);
	public final static File GLOBAL_CONF_DIR = new File("global-conf");
	public final static File GLOBAL_CONF_FILE = new File(GLOBAL_CONF_DIR, "domt4j.xml");

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

	private DomT4j() {
	}

	private static void config() {
		/*
		 * Config XML from HERE To §
		 */
		if (GLOBAL_CONF_DIR.exists()) {

			// verifico se esiste il file di config

			if (GLOBAL_CONF_FILE.exists()) {
				instance.configuration = (DomT4jConfig) £.convertFromXMLToObject(GLOBAL_CONF_FILE, DomT4jConfig.class);
				System.out.println("Abbiamo correttamente aquisito il file di configurazione ...");
			} else {
				// qui devo creare una instanza di configurazione di default e ricreare il file
				System.out.println("Il file di configurazione non esiste #");
				instance.configuration = new DefaultDomT4jConfig();
				// okok creo il file di configurazione
				£.convertFromObjectToXML(DomT4jConfig.class, "global-conf" + File.separator + "domt4j.xml",
						instance.configuration);
				System.out.println("File di configurazione creato @");
			}

		} else {
			// non esiste la cartella di configurazione
			// 1 passo : la creo
			GLOBAL_CONF_DIR.mkdir();
			System.out.println("La cartella di configurazione non esiste .. la si crea ...");
			// creo una instanza di configurazione di default
			instance.configuration = new DefaultDomT4jConfig();
			// okok creo il file di configurazione
			£.convertFromObjectToXML(DomT4jConfig.class, "global-conf" + File.separator + "domt4j.xml",
					instance.configuration);
			System.out.println("File di configurazione creato @");
		}
		// qui devo prendere tutti i valori della configurazione
		/*
		 * COLORS
		 */
		// command and parameter colors
		ColorConfig parameterColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("parameter");
		ColorConfig commandColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("command");
		ColorConfig phaseColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("phase");
		// qui devo verificare se abbiamo ottenuto le rispettive configurazioni dei
		// colori del terminale
		if (parameterColorConfig != null)
			TerminalColors.PARAMETER_COLOR = parameterColorConfig.color;
		if (commandColorConfig != null)
			TerminalColors.COMMAND_COLOR = commandColorConfig.color;
		if (phaseColorConfig != null)
			TerminalColors.PHASE_COLOR = phaseColorConfig.color;
		// nodes colors
		ColorConfig nodeNameColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("nodeName");
		ColorConfig tagColorConfig = (ColorConfig) instance.configuration.colorsConfiguration.getConfigByTarget("tag");
		ColorConfig attributeValueColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("attribute_value");
		ColorConfig commentColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("comment");
		ColorConfig nodeValueColorConfig = (ColorConfig) instance.configuration.colorsConfiguration
				.getConfigByTarget("nodeValue");
		// qui devo verificare se abbiamo ottenuto le rispettive configurazioni dei
		// colori dei nodi
		if (nodeNameColorConfig != null)
			DomColors.NODENAME_COLOR = nodeNameColorConfig.color;
		if (tagColorConfig != null)
			DomColors.TAG_COLOR = tagColorConfig.color;
		if (commentColorConfig != null)
			DomColors.COMMENT_COLOR = commentColorConfig.color;
		if (attributeValueColorConfig != null)
			DomColors.ATTRIBUTE_VALUE_COLOR = attributeValueColorConfig.color;
		if (nodeValueColorConfig != null)
			DomColors.NODEVALUE_COLOR = nodeValueColorConfig.color;
		/*
		 * 
		 * TERMINAL.NAME
		 * 
		 */
		// non teniamo conto per il momento di questa configurazione
		// quindi configuriamo con il valore di default
		instance.setName(DEFAULT_TERMINAL_NAME);
	}

	@SuppressWarnings("static-access")
	private static void initTerminal() {
		config(); // configuration
		instance.setExitCommand("ex");
		instance.getHelpCommands().sort();
		instance.useGeneralHelp();
		ColorLocalCommand.setInputHelpExploitable(true);
		ColorLocalCommand.setToStringParamName("c"); // config -so : returns shared object config
		// imposto JjDom in modo tale che si adatti a un documento HTML colorato a
		// livello di sintassi:
		JjDom.documentTypeUsed = HTMLColorDocument.class;
		final ColorLocalCommand createCommand;
		final ColorLocalCommand cdCommand;
		ColorLocalCommand lsCommand;
		final ColorLocalCommand setCommand;
		ColorLocalCommand getCommand, markupCommand, previewCommand, exitCommand, helpsCommand, statusCommand;
		// 1 comando : create
		createCommand = new ColorLocalCommand("create", "This command creates a node");
		// 2 comando:set:imposta valori del nodo
		setCommand = new ColorLocalCommand("set", "\"This command sets\"");
		// 3 comando:status:stampa un resoconto approfondito di una determinata cosa
		statusCommand = new ColorLocalCommand("status","Displays a report of ... Displays the parameters");
		final ColorLocalCommand globalConfig = new ColorLocalCommand("global-config", "DomT4j Global configuration"); // global-config/domt4j.xml
		final ColorLocalCommand config = new ColorLocalCommand("config","DomT4j configuration"); // global-config/config/domt4j.xml
		// params :
		final Parameter nodeTypeParam;
		final Parameter documentTypeParam;
		final Parameter charsetDocumentParam;
		final Parameter nodeNameParam;
		final Parameter nodeValueParam;
		final Parameter attrParam;
		Parameter appendParam;
		nodeTypeParam = createCommand.addParam("nodeType",
				"Specify the node type - " + j£.colors("(", Color.GREEN)
						+ j£.colors("document" + j£.colors("|", Color.DEFAULT) + j£.colors("element", Color.MAGENTA),
								Color.MAGENTA)
						+ j£.colors("|", Color.DEFAULT) + j£.colors("comment", Color.MAGENTA)
						+ j£.colors(")", Color.GREEN));
		nodeNameParam = createCommand.addParam("nodeName", "Specify the node name");
		nodeValueParam = createCommand.addParam("nodeValue", "Specify the node value");
		appendParam = createCommand.addParam("append", "appends to current node");
		documentTypeParam = createCommand.addParam("format",
				"Specify the document type - " + j£.colors("(", Color.GREEN) + j£
						.colors("html" + j£.colors("|", Color.DEFAULT) + j£.colors("xml", Color.MAGENTA), Color.MAGENTA)
						+ j£.colors(")", Color.GREEN));
		attrParam = createCommand.addParam("attr", "attribute");
		charsetDocumentParam = createCommand.addParam("charset", "Specify the document charset");
		attrParam.setInputValueExploitable(true);
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
						instance.currentNode = new XMLColorDocument(Document.CHARSET_UTF_8, null, rootElementName);
						return positiveMsg("Document " + j£.colors("XML", Color.CYAN) + " is created");
					} else {
						return error("Document type is not valid - Available types= xml|html");
					}
				} else if (nodeType.equals("element")) {
					if (instance.currentNode != null) {
						// chiedo il nome dell'elemento
						if (instance.currentNode != null) {
							System.out.print("Element Name:");
							String elementName = £._I();
							if (instance.currentNode.getDocument() instanceof HTMLDocument) {
								// html
								Element element = ((HTMLColorDocument) instance.currentNode.getDocument())
										.createColorElement(elementName);
								if (element != null) {
									// appendo automaticamente
									instance.currentNode.appendChild(element);
									return positiveMsg("Element added to the "
											+ j£.colors(instance.currentNode.getNodeName(), Color.CYAN) + " node");
								} else {
									return error("Element creation failed");
								}
							} else {
								// xml
								Element element = ((XMLColorDocument) instance.currentNode.getDocument())
										.createElement(elementName);
								if (element != null) {
									// appendo automaticamente
									instance.currentNode.appendChild(element);
									return positiveMsg("Element added to the "
											+ j£.colors(instance.currentNode.getNodeName(), Color.CYAN) + " node");
								} else {
									return error("Element creation failed");
								}
							}
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
		// parameter executions - create :
		// - NODETYPE
		nodeTypeParam.setExecution(new Execution() {

			@Override
			public Object exec() {
				// questo comando è la chiave per creare la configurazione

				if (nodeTypeParam.getInputValue() != null) {

					NodeConfiguration conf = new NodeConfiguration();

					conf.setNodeType(nodeTypeParam.getInputValue());

					// condivido la conf

					createCommand.shareObject(conf);
					
					String setOk = setOk("Node type");
					
					return setOk+"\n"+"Added new parameter ("+j£.colors(ColorLocalCommand.getToStringParamName(),TerminalColors.PARAMETER_COLOR)+") to the \""+j£.colors(createCommand.getCommand(),TerminalColors.COMMAND_COLOR)+"\" command";
				}
				return null;
			}
		});
		// - FORMAT
		documentTypeParam.setExecution(new Execution() {

			@Override
			public Object exec() {

				// qui si scegli il tipo di documento

				// prima cosa verifico che la configurazione ci sia

				if (createCommand.getSharedObject() != null) {

					if (createCommand.getSharedObject() instanceof NodeConfiguration) {

						// verifico che la configurazione rispecchi un documento
						NodeConfiguration conf = createCommand.getSharedObject();
						if (conf.getNodeType() != null) {
							if (conf.getNodeType().equals("document")) {
								if (documentTypeParam.getInputValue() != null) {
									// qui finalmente procediamo impostando il formato del documento :)
									conf.setDocumentType(documentTypeParam.getInputValue());
									return setOk("Document format");
								}
							} else {
								return error("The node is not a document");
							}
						} else {
							// non abbiamo settato il tipo di nodo
							return error("Node type is not set");
						}
					} else {
						// error #1 : l'oggetto condiviso non rispecchia quello previsto
						return error("The shared object does not reflect the expected one");
					}
				} else {
					// da definire ...
					return error("Configuration not present - You must specify the nodeType");
				}

				return null;
			}
		});
		// - NODENAME
		nodeNameParam.setExecution(new Execution() {
			@Override
			public Object exec() {
				// 1 passo : controllo se abbiamo una configurazione
				if (createCommand.getSharedObject() != null) {
					if (createCommand.getSharedObject() instanceof NodeConfiguration) {
						if (nodeNameParam.getInputValue() != null) {
							NodeConfiguration c = createCommand.getSharedObject();
							c.setNodeName(nodeNameParam.getInputValue());
							if (c.getNodeName().contains(",")) {
								return setOk2("Nodes names");
							}
							else {
								return setOk("Node name");
							}
						}
					} else {
						return error("The shared object does not reflect the expected one");
					}
				} else {
					return error("Configuration not present - You must specify the nodeType");
				}
				return null;
			}
		});
		// - NODEVALUE
		// continuare da qui ...
		nodeValueParam.setExecution(new SharedExecution() {
			@Override
			protected Object sharedExec(Sharer sharer) {
				Parameter parameter = (Parameter) sharer;
				Command parent = parameter.getParent();
				// controllo da quale comando è partito il parametro
				if (parent.getCommand().equals(createCommand.getCommand())) {
					// CREATE
					// qui controllo che sia una configurazione
					if (createCommand.getSharedObject() != null) {
						if (createCommand.getSharedObject() instanceof NodeConfiguration) {

							if (parameter.getInputValue() != null) {

								((NodeConfiguration) createCommand.getSharedObject())
										.setNodeValue(parameter.getInputValue());
								if (parameter.getInputValue().contains(",")) {
									return setOk2("Nodes values");
								} else {
									return setOk("Node value");
								}
							}
						} else {
							return error("The shared object does not reflect the expected one");
						}
					} else {
						return error("Configuration not present - You must specify the nodeType");
					}
				} else {
					// SET
					// qui dobbiamo settare il node value sul nodo corrente
					// controllo che si sia un nodo corrente
					if (instance.currentNode != null) {
						if (parameter.getInputValue() != null) {
							instance.currentNode.setNodeValue(parameter.getInputValue());
							return setOk("Node value");
						}
					} else {
						return error("There is no current node");
					}
				}
				return null;
			}
		});
		// - APPEND
		appendParam.setExecution(new Execution() {
			@Override
			public Object exec() {
				if (createCommand.getSharedObject() != null) {
					if (createCommand.getSharedObject() instanceof NodeConfiguration) {
						// ottengo la configurazione e creo il nodo in base ai suoi dati
						NodeConfiguration c = createCommand.getSharedObject();
						// cancello l'oggetto condiviso
						createCommand.shareObject(null);
						// prima cosa verifico il tipo di nodo
						if (c.getNodeType() != null) {
							if (c.getNodeType().equals("document")) {
								// controllo il formato
								if (c.getDocumentType() != null) {
									Document document = null;
									if (c.getDocumentType().equals("xml")) {
										document = new XMLColorDocument();
									} else if (c.getDocumentType().equals("html")) {
										JjDom.newDocument().useDoctype(true).setMinimalTags().home().jqueryInit();
										document = JjDom.document;
									}
									instance.currentNode = document;
								} else {
									return error("Document format is not set");
								}
							}
							// in questi altri due casi, dobbiamo considerare che ci possono
							// essere + nodi specificati
							else if (c.getNodeType().equals("element")) {

								// per prima cosa controllo se abbiamo un nodo corrente
								// dentro cui appendere

								if (instance.currentNode != null) {
									// controllo il nodeName per prima cosa

									if (c.getNodeName() != null) {
										// controllo se sono multipli
										List<Element> elements = new ArrayList<Element>();
										;
										if (c.getNodeName().contains(",")) {
											String[] split = c.getNodeName().split(",");
											for (int i = 0; i < split.length; i++) {
												split[i] = split[i].trim();
												Element element = instance.currentNode.getDocument()
														.createElement(split[i]);
												elements.add(element);
											}
										} else
											elements.add(
													instance.currentNode.getDocument().createElement(c.getNodeName()));
										// okok si procede : si controlla il nodeValue
										if (c.getNodeValue() != null) {
											if (c.getNodeValue().contains(",")) {
												String[] split = c.getNodeValue().split(",");
												for (int j = 0; j < elements.size(); j++) {
													if (j <= split.length - 1) {
														elements.get(j).setNodeValue(split[j]);
													}
												}
											} else {
												// qui setto solo il primo degli elementi pescati nella lista
												elements.get(0).setNodeValue(c.getNodeValue());
											}
										}
										// procedo con il controllo dell'attributo
										if (c.getAttribute() != null) {

											// in tanto distinguo attr/val

											String[] split = c.getAttribute().split(" ");
											if (split.length == 2) {
												String attr = split[0].trim();
												String val = split[1].trim();
												elements.get(0).setAttribute(attr, val);
											} 
										}
										//"Added new parameter ("+j£.colors(ColorLocalCommand.getToStringParamName(),TerminalColors.PARAMETER_COLOR)+") to the \""+j£.colors(createCommand.getCommand(),TerminalColors.COMMAND_COLOR)+"\" command";
										// okok possiamo appendere gli elementi
										StringBuffer s = new StringBuffer();
										for (int i = 0; i < elements.size(); i++) {
											instance.currentNode.appendChild(elements.get(i));
											if (i < elements.size() - 1) {
												s.append(
														positiveMsg(
																"The \"" + j£.colors(elements.get(i).getNodeName(),
																		DomColors.NODENAME_COLOR)
																		+ "\" node has been added to the \""
																		+ j£.colors(instance.currentNode.getNodeName(),
																				DomColors.NODENAME_COLOR)
																		+ "\" node.\n"+"Removed parameter ("+j£.colors(ColorLocalCommand.getToStringParamName(),TerminalColors.PARAMETER_COLOR)+") from \""+j£.colors(createCommand.getCommand(),TerminalColors.COMMAND_COLOR)+"\" command")
																+ "\n");
											} else {
												s.append(positiveMsg("The \""
														+ j£.colors(elements.get(i).getNodeName(),
																DomColors.NODENAME_COLOR)
														+ "\" node has been added to the \""
														+ j£.colors(instance.currentNode.getNodeName(),
																DomColors.NODENAME_COLOR)
														+ "\" node.\n"+"Removed parameter ("+j£.colors(ColorLocalCommand.getToStringParamName(),TerminalColors.PARAMETER_COLOR)+") from \""+j£.colors(createCommand.getCommand(),TerminalColors.COMMAND_COLOR)+"\" command"));
											}
										}
										return s.toString();
									} else {
										return error("Node name is not set");
									}
								} else {
									return error("There is no current node");
								}
							} else if (c.getNodeType().equals("comment")) {
								// definire qui ...
								if (instance.currentNode!=null) {
									List<Comment>elements = new ArrayList<Comment>();
									if (c.getNodeValue() != null) {
										if (c.getNodeValue().contains(",")) {
											String[] split = c.getNodeValue().split(",");
											for (int j = 0; j < split.length; j++) {
												elements.add(instance.currentNode.getDocument().createComment(split[j]));
											}
										} else {
											// qui setto solo il primo degli elementi pescati nella lista
											elements.add(instance.currentNode.getDocument().createComment(c.getNodeValue()));
										}
										StringBuffer s = new StringBuffer();
										for (int i = 0; i < elements.size(); i++) {
											instance.currentNode.appendChild(elements.get(i));
											if (i < elements.size() - 1) {
												s.append(
														positiveMsg(
																"The \"" + j£.colors(elements.get(i).getNodeName(),
																		DomColors.NODENAME_COLOR)
																		+ "\" node has been added to the \""
																		+ j£.colors(instance.currentNode.getNodeName(),
																				DomColors.NODENAME_COLOR)
																		+ "\" node")
																+ "\n");
											} else {
												s.append(positiveMsg("The \""
														+ j£.colors(elements.get(i).getNodeName(),
																DomColors.NODENAME_COLOR)
														+ "\" node has been added to the \""
														+ j£.colors(instance.currentNode.getNodeName(),
																DomColors.NODENAME_COLOR)
														+ "\" node"));
											}
										}
										return s.toString();
									}
									else {
										return error("Node value is not set");
									}
									
								} else {
									return error("There is no current node");
								}
							} else {
								return error("Node type is not valid");
							}
						} else {
							return error("Node type is not set");
						}
					} else {
						return error("The shared object does not reflect the expected one");
					}
				} else {
					return error("Configuration not present - You must specify the nodeType");
				}
				return null;
			}
		});
		// - ATTR
		attrParam.setExecution(new Execution() {
			@Override
			public Object exec() {
				// verifico che ci sia una configurazione
				if (createCommand.getSharedObject() != null) {
					if (createCommand.getSharedObject() instanceof NodeConfiguration) {
						if (attrParam.getInputValue() != null) {
							NodeConfiguration conf = createCommand.getSharedObject();
							conf.setAttribute(attrParam.getInputValue());
							return setOk("Attribute");
						}
					} else {
						return error("The shared object does not reflect the expected one");
					}
				} else {
					return error("Configuration not present - You must specify the nodeType");
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
		// params -ls:
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

		// set params :

		// come prima cosa condivido il parametro nodeValue da create

		setCommand.shareItEntirely(nodeValueParam);

		final Parameter attribute;
		attribute = setCommand.addParam("attribute", "Specify the attribute");
		attribute.setInputValueExploitable(true);
		attribute.setExecution(new Execution() {

			@Override
			public Object exec() {

				if (instance.currentNode != null) {
					if (attribute.getInputValue() != null) {
						// otteniamo valore e prop
						String value, attrib;

						String input = attribute.getInputValue();
						if (input.split(" ").length == 2) {

							attrib = input.split(" ")[0];
							value = input.split(" ")[1];

							// imposto l'attributo

							if (instance.currentNode instanceof Element) {
								((Element) instance.currentNode).setAttribute(attrib, value);
								return setOk(attrib);
							} else {
								// da definire ...
							}
						} else {
							// da definire ,..
						}
					} else {
						// da definire ,..
					}

				} else {
					// da definire ...
				}

				return null;
			}
		});
		// 5 comando:markup: ottiene il markup del nodo corrente
		markupCommand = new ColorLocalCommand("markup", "Gets the markup of the current node");
		markupCommand.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (instance.currentNode != null) {
					String markup = null;
					if (instance.currentNode instanceof Colorable) {
						markup = ((Colorable) instance.currentNode).getColorMarkup();
					} else {
						markup = instance.currentNode.getMarkup();
					}
					return "===================================================================================\n"
							+ markup
							+ "===================================================================================";
				} else {
					// da definire ...
					return null;
				}
			}
		});
		// 6 comando: preview
		previewCommand = new ColorLocalCommand("preview", "From a preview of the page on the browser");
		previewCommand.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (instance.currentNode != null) {

					if (instance.currentNode.getDocument() instanceof HTMLDocument) {

						JjDom.preview();

					} else {
						// da definire ...
						if (Desktop.isDesktopSupported()) {

							// salvo il file
							java.io.File xmlFile = new java.io.File("preview.xml");
							j£.writeFile(xmlFile, false,
									new String[] { instance.currentNode.getDocument().getMarkup() });
							// apro il file

							try {
								Desktop.getDesktop().browse(xmlFile.toURI());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							xmlFile.delete();
							xmlFile.deleteOnExit();
						}
					}

				} else {
					// da definire ...
				}
				return null;
			}
		});
		// command : exit
		exitCommand = new ColorLocalCommand("exit", "closes the program");
		exitCommand.setExecution(new Execution() {

			@Override
			public Object exec() {

				// disistallo jansi

				j£.ANSI_CONSOLE.systemUninstall();

				System.out.println("Good bye !!");

				System.exit(0);

				return null;
			}
		});
		// command : helps
		helpsCommand = new ColorLocalCommand("helps", "Shows the commands list");
		helpsCommand.setExecution(new Execution() {

			@Override
			public Object exec() {
				ColorString string = new ColorString();
				string.append("------------------------------------------------------------------------\n");
				string.append("Commands List :\n");
				string.append("------------------------------------------------------------------------\n");
				Collection<LocalCommand> collection = instance.commands.values();
				List<LocalCommand> commands = new ArrayList<LocalCommand>();
				Iterator<LocalCommand> iterator = collection.iterator();
				while (iterator.hasNext()) {
					LocalCommand localCommand = (LocalCommand) iterator.next();
					commands.add(localCommand);
				}
				Collections.sort(commands);
				for (int i = 0; i < commands.size(); i++) {
					if (i < commands.size() - 1) {
						if (commands.get(i).getBelongsTo() != null) {
							string.append("* Command:")
									.append(commands.get(i).getCommand(), TerminalColors.COMMAND_COLOR)
									.append(" -  Phase:")
									.append(commands.get(i).getBelongsTo().phaseName(), TerminalColors.PHASE_COLOR)
									.append("\n");
						} else {
							string.append("* Command:")
									.append(commands.get(i).getCommand(), TerminalColors.COMMAND_COLOR)
									.append(" -  Phase:").append("null", Color.DEFAULT).append("\n");
						}
					} else {
						if (commands.get(i).getBelongsTo() != null) {
							string.append("* Command:")
									.append(commands.get(i).getCommand(), TerminalColors.COMMAND_COLOR)
									.append(" -  Phase:")
									.append(commands.get(i).getBelongsTo().phaseName(), TerminalColors.PHASE_COLOR);
						} else {
							string.append("* Command:")
									.append(commands.get(i).getCommand(), TerminalColors.COMMAND_COLOR)
									.append(" -  Phase:").append("null", Color.DEFAULT);
						}
					}
				}
				return string.toString();
			}
		});
		// command : config
		Parameter file = globalConfig.addParam("file", "This parameter shows the global configuration file");
		file.setExecution(new Execution() {
			@Override
			public Object exec() {
				try {
					return j£.readFile(GLOBAL_CONF_FILE).trim();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
		
		// STATUS COMMAND : PARAMETERS
		final Parameter node = statusCommand.addParam("node","current node");
		final Parameter n = statusCommand.addParam("n","current node");
		final Parameter phase = statusCommand.addParam("phase","current phase");
		final Parameter p = statusCommand.addParam("p","current phase");
		
		// status exec
		
		statusCommand.setExecution(new Execution() {
			@Override
			public Object exec() {

				return phase.execute()+"\n"+node.execute();
				
			}
		});
		phase.setExecution(new Execution() { // provvisorio, controllare se la fase corrente esiste
			@Override
			public Object exec() {
				// TODO Auto-generated method stub
				ColorString string = new ColorString();
				string.append("\n\t\t|Current Phase > ").append(instance.currentPhase.phaseName().toUpperCase(),TerminalColors.PHASE_COLOR).append("\n")
					  .append("\t\t|Level = ").append(instance.currentPhase.getValue()+"",Color.DEFAULT);
				if (((DefaultPhase)instance.currentPhase).getAccessibilityRule()!=null) {
					string.append("\n\t\t|Access-Rule = ").append(((DefaultPhase)instance.currentPhase).getAccessibilityRule().ruleExplanation(),Color.DEFAULT);
				}
				if (((DefaultPhase)instance.currentPhase).getSatisfiabilityRule()!=null) {
					string.append("\n\t\t|Satisfaction-Rule = ").append(((DefaultPhase)instance.currentPhase).getSatisfiabilityRule().ruleExplanation(),Color.DEFAULT);
				}
				string.append("\n");
				return string.toString() ;	   
			}
		});
		node.setExecution(new Execution() {
			
			@Override
			public Object exec() {
				String result = null ;
				if (instance.currentNode!=null) {
					try {
						result = ColorLocalCommand.toString(instance.currentNode,Color.DEFAULT,Color.GREEN);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					// da definire ...
				}
				return result ;
			}
		});
		
		n.setExecution(node.getExecution());
		p.setExecution(phase.getExecution());
		
		// inserisco i comandi nel terminale, quelli generali
		instance.addCommands(cdCommand, lsCommand, markupCommand, previewCommand, setCommand, exitCommand, helpsCommand, globalConfig, config, statusCommand);

		//////////////////////////////////////////////////////////////////////////
		// PHASES DEV :
		//////////////////////////////////////////////////////////////////////////

		Phase creationPhase, migrationPhase;

		// 1 PHASE : CREATE

		creationPhase = instance.createPhase(1, "creation", "In questa fase si crea e imposta un nodo dom",
				createCommand);

		// 2 PHASE : MIGRATION

		// commands :
		final ColorLocalCommand connect, migrate;

		connect = new ColorLocalCommand("connect", "Connection to ftp server");

		migrate = new ColorLocalCommand("migrate", "migrate the document");

		migrate.setExecution(new Execution() {

			@Override
			public Object exec() {
				// controllo che ci sia una configurazione pronta
				if (connect.getSharedObject() != null) {
					FTPServerConfiguration conf = connect.getSharedObject();
					// cancello l'oggetto condiviso
					connect.shareObject(null);
					// controllo che la configurazione sia pronta
					if (conf.isCompleted()) {
						System.out.print("Destination URL:");
						String urlResource = j£._I();
						System.out.println("Migration in progress ...");
						// mi connetto al server
						JjDom.connect(conf.getHost(), conf.getUsername(), conf.getPassword()).migrate(urlResource,
								instance.currentNode.getDocument());
					} else {
						error("The configuration is not complete");
					}
				} else {
					// da definire ...
				}

				return null;
			}
		});

		final Parameter host;
		final Parameter user;
		final Parameter passw;

		host = connect.addParam("host", "ftp host");
		user = connect.addParam("user", "ftp username");
		passw = connect.addParam("password", "ftp password");

		host.setInputValueExploitable(true);
		user.setInputValueExploitable(true);
		passw.setInputValueExploitable(true);

		host.setExecution(new Execution() {

			@Override
			public Object exec() {
				// verifico che ci sia il valore da input

				if (host.getInputValue() != null) {

					// okok mi preparo

					// l'oggetto configurazione apposito

					FTPServerConfiguration c = new FTPServerConfiguration();

					c.setHost(host.getInputValue());

					// condivido l'oggetto

					connect.shareObject(c);

					return setOk("Ftp-Host");
				}

				return null;
			}
		});

		user.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (connect.getSharedObject() != null) {

					if (user.getInputValue() != null) {
						// ottengo la configurazione

						FTPServerConfiguration conf = connect.getSharedObject();

						conf.setUsername(user.getInputValue());

						return setOk("Ftp-User");
					}
				} else {
					// da definire ...
				}
				return null;
			}
		});

		passw.setExecution(new Execution() {

			@Override
			public Object exec() {
				if (connect.getSharedObject() != null) {

					if (passw.getInputValue() != null) {
						// ottengo la configurazione

						FTPServerConfiguration conf = connect.getSharedObject();

						conf.setPassword(passw.getInputValue());

						return setOk("Ftp-Passw");
					}
				} else {
					// da definire ...
				}
				return null;
			}
		});

		migrationPhase = instance.createPhase(2, "migration", "Caricamento di un documento in rete", connect, migrate);

		// mi creo le regole di migration 
		
		migrationPhase.accessibleThrough(new Rule() {
			
			public boolean verification() {
				if (instance.currentNode!=null) {
					if (instance.currentNode.getDocument()!=null) {
						return true ;
					}
					else {
						return false ;
					}
				}
				else {
					return false ;
				}
			}
			
			public String ruleExplanation() {
				// TODO Auto-generated method stub
				return "Accessible if there is a document";
			}
		});
		
		migrationPhase.satisfiableThrough(new Rule() {
			
			public boolean verification() {
				// DA DEFINIRE ...
				return false;
			}
			
			public String ruleExplanation() {
				// TODO Auto-generated method stub
				return "Satisfied if the migration took place";
			}
		});

	}

	private static void describeNodes(NodeList listNodes) {
		System.out.println("___________________________________________________________________________________\n");
		for (int i = 0; i < listNodes.getLength(); i++) {
			System.out.println((i + 1) + ") - NodeName:" + j£.colors(listNodes.item(i).getNodeName(), Color.CYAN)
					+ " - Parent:" + j£.colors(listNodes.item(i).getParentNode().getNodeName(), Color.CYAN)
					+ " - Children:" + listNodes.item(i).getChildNodes().getLength());
		}
		System.out.println("___________________________________________________________________________________");
	}
	// ridefinisco il metodo getCommandRequest

	@Override
	public String getCommandRequest() {
		StringBuffer myCommandRequest = new StringBuffer();
		// il server ha un nome
		myCommandRequest.append(getName());
		// controllo se abbiamo una fase corrente
		if (currentPhase != null) {
			if (currentPhase.getValue()==1) {
				if (instance.configuration.firstPhaseVisible) {
					myCommandRequest.append(
							"_(" + j£.colors(currentPhase.phaseName().toUpperCase(), TerminalColors.PHASE_COLOR) + ")");
				}
			}
			else {
				if (instance.configuration.phaseVisible) {
					myCommandRequest.append(
							"_(" + j£.colors(currentPhase.phaseName().toUpperCase(), TerminalColors.PHASE_COLOR) + ")");
				}	
			}
		}
		if (currentNode != null) {
			myCommandRequest.append("_<" + j£.colors(currentNode.getNodeName().toUpperCase(), Color.MAGENTA) + ">_ "
					+ j£.colors("~/" + currentNode.getPath(), Color.YELLOW) + j£.colors(":", Color.WHITE));
		} else {
			myCommandRequest.append("_<" + j£.colors("NULL", Color.DEFAULT) + ">_:");
		}
		return myCommandRequest.toString();
	}

}
