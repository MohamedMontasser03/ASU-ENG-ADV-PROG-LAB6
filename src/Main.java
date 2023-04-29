package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class NotVaildAutosarFileException extends Exception {
    public NotVaildAutosarFileException() {
        super("Error: ARXML file is not valid.\nMessage: ARXML file doesn't have the correct extension.");
    }
}

class EmptyAutosarFileException extends Exception {
    public EmptyAutosarFileException() {
        super("Error: ARXML file is not valid.\nMessage: ARXML file is empty.");
    }
}

class ARXMLReader {
    private final File arxmlFile;
    private final Document doc;
    private final Element root;
    private final NodeList containers;

    public ARXMLReader(final String arxmlPath) throws Exception {
        final File arxmlFile = new File(arxmlPath);
        if (!arxmlFile.exists()) {
            throw new NotVaildAutosarFileException();
        }
        if (arxmlFile.getName().endsWith(".arxml") == false) {
            throw new NotVaildAutosarFileException();
        }
        if (arxmlFile.length() == 0) {
            throw new EmptyAutosarFileException();
        }

        this.arxmlFile = arxmlFile;
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.doc = dBuilder.parse(this.arxmlFile);
        this.root = (Element) this.doc.getElementsByTagName("AUTOSAR").item(0);
        this.containers = this.root.getElementsByTagName("CONTAINER");
    }

    public final void sortContainerNodes() {
        final HashMap<String, Element> containerMap = new HashMap<String, Element>();
        for (int i = 0; i < this.containers.getLength(); i++) {
            final Element container = (Element) this.containers.item(i);
            final Element shortName = (Element) container.getElementsByTagName("SHORT-NAME").item(0);
            containerMap.put(shortName.getTextContent(), (Element) container.cloneNode(true));
        }

        final TreeMap<String, Element> sortedContainerMap = new TreeMap<String, Element>(containerMap);
        while (this.root.hasChildNodes()) {
            this.root.removeChild(this.root.getFirstChild());
        }
        for (final Element container : sortedContainerMap.values()) {
            this.root.appendChild(container);
        }
    }

    private static void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public final void saveToFile() throws TransformerException, FileNotFoundException {
        final File outDir = new File("out");
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        final String name = arxmlFile.getName().replaceFirst("[.][^.]+$", "") + "_mod";
        final File file = new File("out/" + name + ".arxml");

        final OutputStream outputStream = new FileOutputStream(file);
        writeXml(this.doc, outputStream);
    }
}

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: ARXML file path is required as an argument.");
            System.exit(1);
        }

        final String arxmlPath = args[0];

        try {
            final ARXMLReader arxmlReader = new ARXMLReader(arxmlPath);
            arxmlReader.sortContainerNodes();
            arxmlReader.saveToFile();
            System.out.println("Success: ARXML file is created.");
        } catch (Exception e) {
            System.out.println("Error: ARXML file is not valid.");
            System.out.println("Message: " + e.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }
}