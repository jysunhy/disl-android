package ch.usi.dag.disldroidserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DiSLConfig {

    private static final String CONFIG_FILE = System.getProperty (
        "configfile", "config.xml");

    static String default_disl_classes = "";

    static boolean default_proc_observed = false;

    public static HashMap <String, Dex> dexMap;
    public static HashMap <String, Proc> procMap;
    public static HashMap <String, Proc> dex2procMap;
//    static {
//        parseXml ("test.xml");
//    }

    public static class Dex {
        public String dexname = "";

        public boolean isBootstrapDex = false;

        public String dislClass = default_disl_classes;

        public String preinstrumented_path = "";

		public boolean bypass = true;

        public void print () {
            System.out.println (dexname +":"+ isBootstrapDex +":"+ dislClass);
        }
    }


    public static class Proc {
        public String procname;

        public DiSLConfig.Dex dex;

        public boolean isObserved = default_proc_observed;


        public void print () {
            System.out.println (procname+":"+isObserved);
            if(dex!=null) {
                dex.print ();
            }
        }
    }




    public static void parseDexes (final Element node) {
        final NodeList dexes = node.getChildNodes ();
        for (int i = 0; i < dexes.getLength (); i++) {
            final Node dexnode = dexes.item (i);
            if (dexnode.getNodeType () == Node.ELEMENT_NODE) {

                final NodeList dexAttr = dexnode.getChildNodes ();
                final Dex newdex = new Dex ();
                for (int j = 0; j < dexAttr.getLength (); j++) {
                    final Node attrNode = dexAttr.item (j);
                    if (attrNode.getNodeType () == Node.ELEMENT_NODE) {
                        if (attrNode.getNodeName ().equals ("name")) {
                            newdex.dexname = attrNode.getTextContent ();
                        }
                        if (attrNode.getNodeName ().equals ("isbootstrap")) {
                            newdex.isBootstrapDex = Boolean.parseBoolean (attrNode.getTextContent ());
                        }
                        if (attrNode.getNodeName ().equals ("bypass")) {
                            newdex.bypass = Boolean.parseBoolean (attrNode.getTextContent ());
                        }
                        if (attrNode.getNodeName ().equals ("dislclass")) {
                            newdex.dislClass = attrNode.getTextContent ();
                            //System.out.println (newdex.dislClass);
                        }
                        if (attrNode.getNodeName ().equals ("preinstrumented")) {
                            newdex.preinstrumented_path = attrNode.getTextContent ();
                        }
                    }
                }
                dexMap.put (newdex.dexname, newdex);
            }
        }
    }


    public static void parseProcs (final Element node) {

        final NodeList procs = node.getChildNodes ();
        for (int i = 0; i < procs.getLength (); i++) {
            final Node procnode = procs.item (i);
            if (procnode.getNodeType () == Node.ELEMENT_NODE) {

                final NodeList dexAttr = procnode.getChildNodes ();
                final Proc newproc = new Proc ();
                for (int j = 0; j < dexAttr.getLength (); j++) {
                    final Node attrNode = dexAttr.item (j);
                    if (attrNode.getNodeType () == Node.ELEMENT_NODE) {
                        if (attrNode.getNodeName ().equals ("procname")) {
                            newproc.procname = attrNode.getTextContent ();
                        }
                        if (attrNode.getNodeName ().equals ("isobserved")) {
                            newproc.isObserved = Boolean.parseBoolean (attrNode.getTextContent ());
                        }
                        if (attrNode.getNodeName ().equals ("dexname")) {
                            newproc.dex = dexMap.get (attrNode.getTextContent ());
                        }
                    }
                }
                procMap.put (newproc.procname, newproc);
                if(newproc.dex!=null) {
                    dex2procMap.put (newproc.dex.dexname, newproc);
                }
            }
        }
    }

    private static byte[] xmlcontent = null;

    public static boolean parseXml () {
        final String fileName = CONFIG_FILE;
        try{
            final File xmlFile = new File(fileName);
            FileInputStream fis = null;
            DataInputStream dis = null;
            fis = new FileInputStream (xmlFile);
            dis = new DataInputStream (fis);

            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();

            int temp;
            int size = 0;
            final byte [] b = new byte [2048];
            while ((temp = dis.read (b)) != -1) {
                bout.write (b,0,temp);
                size += temp;
            }
            if(xmlcontent == null) {
                xmlcontent = bout.toByteArray ();
            }else{
                if(xmlcontent.length != bout.toByteArray ().length) {
                    return false;
                }
                if(!Arrays.toString(xmlcontent).equals (Arrays.toString (bout.toByteArray ()))){
                    return false;
                }else{
                    xmlcontent = bout.toByteArray ();
                }
            }
        }catch(final Exception e){
            e.printStackTrace ();
        }

        System.out.println ("Parsing the XML");
        dexMap = new HashMap <String, Dex> ();
        procMap = new HashMap <String, Proc> ();
        dex2procMap = new HashMap <String, Proc> ();
        default_proc_observed = false;
        default_disl_classes = "";
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
            final DocumentBuilder db = dbf.newDocumentBuilder ();
            final Document document = db.parse (new File(fileName));

            final Element rootElem = document.getDocumentElement ();
            // System.out.println(rootElem.getChildNodes ().getLength ());
            final NodeList nodes = rootElem.getChildNodes ();

            for (int i = 0; i < nodes.getLength (); i++)
            {
                final Node node = nodes.item (i);
                if (node.getNodeType () == Node.ELEMENT_NODE) {
                    final Element child = (Element) node;
                    // process child element
                    if (child.getNodeName ().equals ("default-dislclass")) {
                        default_disl_classes = child.getTextContent ();
                        if(default_disl_classes.startsWith ("\n")) {
                            default_disl_classes="";
                        }
                    }else if (child.getNodeName ().equals ("default-observed")) {
                        default_proc_observed = Boolean.getBoolean (child.getTextContent ());
                    }
                }
            }

            for (int i = 0; i < nodes.getLength (); i++)
            {
                final Node node = nodes.item (i);
                if (node.getNodeType () == Node.ELEMENT_NODE) {
                    final Element child = (Element) node;
                    // process child element
                    if (child.getNodeName ().equals ("dexes")) {
                        parseDexes (child);
                    } else if (child.getNodeName ().equals ("procs")) {
                        parseProcs(child);
                    }
                }
            }

        } catch (final Exception e) {
            e.printStackTrace ();
        }
        return true;
    }


    public static void main (final String [] args) {
        // TODO Auto-generated method stub
        parseXml ();
    }

}

