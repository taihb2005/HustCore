package org.kat.app.ui;

import org.kat.app.graphics.AssetPool;
import org.kat.app.ui.components.GameButton;
import org.kat.app.ui.components.ValueAdjuster;
import org.kat.app.ui.views.*;
import org.kat.app.ui.views.Text;
import org.kat.app.util.GenericTree;
import org.kat.app.util.Tree;
import org.kat.app.util.TreeNode;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class UIBuilder {

    public static Tree<View> buildFromXML(String xmlPath) {
        try {
            InputStream input = UIBuilder.class.getResourceAsStream(xmlPath);
            if (input == null) {
                throw new RuntimeException("Resource not found: " + xmlPath);
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(input);
            Element rootElement = doc.getDocumentElement();

            View rootView = createView(rootElement);
            Tree<View> tree = new GenericTree<>(new TreeNode<>(rootView));

            buildChildren(tree.getRoot(), rootElement);

            input.close();
            return tree;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build UI tree: " + e.getMessage(), e);
        }
    }

    private static void buildChildren(TreeNode<View> parentNode, Element parentElement) {
        NodeList nodeList = parentElement.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n instanceof Element childElement) {
                View childView = createView(childElement);
                TreeNode<View> childNode = new TreeNode<>(childView);
                parentNode.add(childNode);

                buildChildren(childNode, childElement);
            }
        }
    }

    private static View createView(Element e) {
        String tag = e.getTagName();
        String id = e.getAttribute("id");
        int x = getIntAttr(e, "x", 0);
        int y = getIntAttr(e, "y", 0);
        int width = getIntAttr(e, "width", 0);
        int height = getIntAttr(e, "height", 0);

        View res = null;

        switch(tag){
            case "ImageView" -> {
                String imagePath = e.getAttribute("image");
                BufferedImage image = AssetPool.getImage(imagePath);

                res = new ImageView(x, y, width, height, image);
            }

            case "TextView" -> {
                String content = e.getAttribute("text");
                boolean shouldShadow = getBooleanAttr(e, "shadow", false);
                Text text = (shouldShadow) ? new ShadowedText(content) : new Text(content);
                text.setFontSize(getIntAttr(e, "fontSize", 0));

                String[] alignments = getAttr(e, "alignment", "center").split("\\|");
                Alignment hAlign =  switch(alignments[0].trim()){
                    case "left" -> Alignment.HORIZONTAL_LEFT;
                    case "right" -> Alignment.HORIZONTAL_RIGHT;
                    default -> Alignment.HORIZONTAL_CENTER;
                };
                Alignment vAlign = switch((alignments.length > 1 ? alignments[1] : "center").trim()){
                    case "top" -> Alignment.VERTICAL_TOP;
                    case "bottom" -> Alignment.VERTICAL_BOTTOM;
                    default -> Alignment.VERTICAL_CENTER;
                };
                text.setAlignment(hAlign, vAlign);

                res = new TextView(text, x, y, width, height);
            }

            case "WrappedTextView" -> {
                String content = e.getAttribute("text");
                boolean shouldShadow = getBooleanAttr(e, "shadow", false);
                Text text = (shouldShadow) ? new ShadowedText(content) : new Text(content);
                text.setFontSize(getIntAttr(e, "fontSize", 0));

                String[] alignments = getAttr(e, "alignment", "center").split("\\|");
                Alignment hAlign =  switch(alignments[0].trim()){
                    case "left" -> Alignment.HORIZONTAL_LEFT;
                    case "right" -> Alignment.HORIZONTAL_RIGHT;
                    default -> Alignment.HORIZONTAL_CENTER;
                };
                Alignment vAlign = switch((alignments.length > 1 ? alignments[1] : "center").trim()){
                    case "top" -> Alignment.VERTICAL_TOP;
                    case "bottom" -> Alignment.VERTICAL_BOTTOM;
                    default -> Alignment.VERTICAL_CENTER;
                };
                text.setAlignment(hAlign, vAlign);

                res = new WrappedTextView(text, x, y, width, height);
            }

            case "SubWindow" -> {
                res = new SubWindow(x, y, width, height);
            }

            case "GameButton" -> {
                String content = e.getAttribute("text");
                int roundArc = getIntAttr(e, "roundRadius", 30);
                int fontSize = getIntAttr(e, "fontSize", 30);
                boolean enabled = getBooleanAttr(e, "enabled", true);

                Text text = new Text(content);
                text.setFontSize(fontSize);

                res = new GameButton(text, x, y, width, height, roundArc, enabled);
            }

            case "ValueAdjuster" -> {
                int value = getIntAttr(e, "value", 0);
                int upperThreshold = getIntAttr(e, "upperThreshold", 100);
                int lowerThreshold = getIntAttr(e, "lowerThreshold", 0);
                int fontSize = getIntAttr(e, "fontSize", 30);

                Text text = new Text(String.valueOf(value));
                text.setFontSize(fontSize);

                res = new ValueAdjuster(text, x, y, width, height, value, lowerThreshold, upperThreshold);

            }

        }

        assert res != null;
        res.setId(id);

        return res;
    }

    private static int getIntAttr(Element e, String name, int def) {
        return e.hasAttribute(name) ? Integer.parseInt(e.getAttribute(name)) : def;
    }

    private static boolean getBooleanAttr(Element e, String name, boolean def){
        if(!e.hasAttribute(name)) return def;
        return e.getAttribute(name).trim().equals("true");
    }

    private static <T> String getAttr(Element e, String name, T def){
        return e.hasAttribute(name) ? e.getAttribute(name).trim() : def.toString();
    }

}
