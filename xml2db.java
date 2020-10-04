package mysql;
import java.io.File;
import java.sql.*; 
import java.util.regex.Pattern;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class xml2db {
	
	static private String getAttrValue(Node node,String attrName) {
	    if ( ! node.hasAttributes() ) return "";
	    NamedNodeMap nmap = node.getAttributes();
	    if ( nmap == null ) return "";
	    Node n = nmap.getNamedItem(attrName);
	    if ( n == null ) return "";
	    return n.getNodeValue();
	}
	
	static private String getTextContent(Node parentNode,String childName) {
	    NodeList nlist = parentNode.getChildNodes();
	    for (int i = 0 ; i < nlist.getLength() ; i++) {
	    Node n = nlist.item(i);
	    String name = n.getNodeName();
	    if ( name != null && name.equals(childName) )
	        return n.getTextContent();
	    }
	    return "";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Scanner sc=new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manish",
					"root","root");
			if (con != null)			 
				System.out.println("Connected");			 
			else			
				System.out.println("Not Connected");
			
			File file = new File("src/pokemon.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.parse(file);
			
			NodeList booksLength = xmlDoc.getElementsByTagName("row");
			
			//this gives the number of records in the xml file
			System.out.println(booksLength.getLength());
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			Object res = xpath.evaluate("/pokemon/row",
			                 xmlDoc,
			                 XPathConstants.NODESET);
			
			PreparedStatement stmt = con
				    .prepareStatement("insert into xml2db values(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			for (int i = 0 ; i < booksLength.getLength() ; i++) {
				Node node = booksLength.item(i);
				if(getAttrValue(node, "Name")!=null) {
					List<String> columns = Arrays
			    			.asList(getAttrValue(node, "Name"),
			    			getTextContent(node, "Type1"),
						    getTextContent(node, "Type2"),
						    getTextContent(node, "HP"),
						    getTextContent(node, "Attack"),
						    getTextContent(node, "Defense"),
						    getTextContent(node, "spatk"),
						    getTextContent(node, "spdef"),
						    getTextContent(node, "Speed"),
						    getTextContent(node, "Generation"),
						    getTextContent(node, "Legendary"),
						    getTextContent(node, "Total"));
					
					for (int n = 0 ; n < columns.size() ; n++) {
						stmt.setString(n+1, columns.get(n));
					}
					stmt.execute();
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}

	}

}
