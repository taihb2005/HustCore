package tile;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MapParser {

    public static void loadMap(String id , String filepath)
    {
        parseMap(id , filepath);
    }

    private static void parseMap(String id , String path)
    {
        try
        {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();

            int tilewidth  , tileheight , numrows , numcols ;

            Element root = doc.getDocumentElement();
            tilewidth = Integer.parseInt(root.getAttribute("tilewidth"));
            tileheight = Integer.parseInt(root.getAttribute("tileheight"));
            numrows = Integer.parseInt(root.getAttribute("height"));
            numcols = Integer.parseInt(root.getAttribute("width"));

            GameMap mp = new GameMap(tilewidth * numcols , tileheight * numrows);

            ArrayList<TileSet> tilesetlist = new ArrayList<>();
            NodeList list = doc.getElementsByTagName("tileset");
            for(int i = 0; i < list.getLength() ; i++)
            {
                Element eElement = (Element) list.item(i);
                tilesetlist.add(getTileSet(eElement));
            }

            list = doc.getElementsByTagName("layer");

            int layers = list.getLength();

            for(int i = 0 ; i < layers ; i++)
            {
                Element eElement = (Element) list.item(i);
                String visible = eElement.getAttribute("visible");
                boolean isVisible = visible.equals("1");
                String data = eElement.getElementsByTagName("data").item(0).getTextContent();
                //System.out.print("-----------------------\n" + data);
                mp.map.add(parseTileLayer(data , numrows , numcols , isVisible , tilesetlist));
            }


            MapManager.appendGameMap(id , mp);

        } catch(Exception e)
        {
            System.out.println("Cannot parse map from path: " + path);
            e.printStackTrace();
        }
    }

    private static TileLayer parseTileLayer(String data , int numRows , int numCols , boolean isVisible , ArrayList<TileSet> tilesetlist)
    {
        String[] values = data.trim().split(",");

        int index = 0;

        // Initialize the 2D array
        int[][] array = new int[numRows][numCols];

        // Populate the 2D array
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                array[i][j] = Integer.parseInt(values[index].trim());
                index++;
            }
        }

        return new TileLayer(numRows , numCols , array , isVisible , tilesetlist);
    }




    private static TileSet getTileSet(Element eElement)
    {
        String tilename = eElement.getAttribute("name");

        int tilewidth = Integer.parseInt(eElement.getAttribute("tilewidth"));
        int tileheight = Integer.parseInt(eElement.getAttribute("tilewidth"));

        int tilecount = Integer.parseInt(eElement.getAttribute("tilecount"));
        int firstid = Integer.parseInt(eElement.getAttribute("firstgid"));
        int lastid = firstid + tilecount - 1;

        int numcols = Integer.parseInt(eElement.getAttribute("columns"));
        int numrows = tilecount / numcols;

        return new TileSet(firstid , lastid , tilewidth , tileheight ,numrows , numcols , tilename);
    }
}
