package engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ColladaLoader {

	
	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	public static Model loadModel(File model,File texture) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document domTree = db.parse(model);
		Node rootNode  = domTree.getDocumentElement();
		
        NodeList childNodes = rootNode.getChildNodes();
        FileOutputStream fos = new FileOutputStream(new File("d:/out.tree"));
        printNodes(childNodes,fos,0);
        fos.close();
 
		//TODO: this will convert the node data to a model
		return null;
	}
	
	public static void printNodes(NodeList list,OutputStream os,int depth) throws IOException{
		for(int i=0;i<list.getLength();i++){
			String tab = "";
			for(int i2=0;i2<depth;i2++)
				tab += "\t";
			os.write((new String(tab + "Node Name: " + list.item(i).getNodeName() + "\n").getBytes()));
			os.write((new String(tab + "\tValue: " + list.item(i).getNodeValue() + "\n").getBytes()));
			printNodes(list.item(i).getChildNodes(),os,depth + 1);
		}
	}
	
	static Node getNode(NodeList list, String name){
		for(int i=0;i<list.getLength();i++){
			if(list.item(i).getNodeName().equals(name)){
				return list.item(i);
			}
		}
		return null;
	}
}	
	
	/*
	private static class Node{		
		
		public Attribute[] atts;
		public String name;
		public String val;
		
		public Node(Attribute[] atts, String name, String val){
			this.name = name;
			this.val = val;
			this.atts = atts;
		}
		
		@Override
		public String toString() {
			String out = "";
			out += name;
			Character c = new Character((char) 10);
			out += c;
			out += "Attributes: ";
			out += c;
			for(int i=0;i<atts.length;i++){
				out += atts[i].name;
				out += ": ";
				out += atts[i].val + ", ";
			}
			out += c;
			out += "Chars: " + val;
			out += c;
			return out;
		}
	}
	
	private static class Attribute{
		public String name;
		public String val;
		public Attribute(String name, String val){
			this.name = name;
			this.val = val;
		}
	}
	
	private static class ParserCallBack extends DefaultHandler {
		
		
		String name;
		Attribute[] atts;
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			name = qName;
			atts = new Attribute[attributes.getLength()];
			for(int i=0;i<attributes.getLength();i++){
				atts[i] = new Attribute(attributes.getQName(i), attributes.getValue(attributes.getQName(i)));
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String val = new String(ch,start,length);
			Node n = new Node(atts, name, val);
			nodes.add(n);
		}
	}
	
}
	*/
