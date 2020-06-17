package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.XMLDecoder;
import java.io.File;
import java.util.ArrayList;

public class DatasetLoader {

    public static ArrayList<City> decodeDataset(File datasetFile) throws Exception{

        ArrayList<City> cityList = new ArrayList<>();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(datasetFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("city");

            for (int temp = 0; temp < nList.getLength();temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE){

                    Element eElement = (Element) nNode;
                    City city = new City();
                    city.setNumber(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent()));
                    city.setPopulation(Integer.parseInt(eElement.getElementsByTagName("population").item(0).getTextContent()));
                    city.setX(Double.parseDouble(eElement.getElementsByTagName("x").item(0).getTextContent()));
                    city.setY(Double.parseDouble(eElement.getElementsByTagName("y").item(0).getTextContent()));
                    cityList.add(city);
                }
            }

        return cityList;
    }
}