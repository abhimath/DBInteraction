import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLData
{
    public XMLData()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("hostname", "username", "password");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            sql = "SELECT gpID FROM t_gpinfo ORDER BY gpID DESC LIMIT 1;";
            rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.next())
            {
                gpid = rs.getInt("gpID");
            }
            else
            {
                gpid = 0;
            }
            
            sql = "SELECT dID FROM t_datasetinfo ORDER BY dID DESC LIMIT 1;";
            rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.next())
            {
                did = rs.getInt("dID");
            }
            else
            {
                did = 0;
            }
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch(InstantiationException ex)
        {
            ex.printStackTrace();
        }
        catch(IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void xmlMysql()
    {
        try
        {
            file = new File("pooledtargetediii_map_glycopeptides.xml");
            reader = new BufferedReader(new FileReader(new File("sequences.fasta")));
            String text = null;
            while((text = reader.readLine()) != null)
            {
                if(text.startsWith(">"))
                {
                    protid = "'" + text.substring(text.indexOf(">") + 1, text.indexOf(" ")) + "'";
                }
                else
                {
                    sequence = "'" + text + "'";
                    sql = "INSERT INTO t_proteininfo (ProteinID, Sequence, Length) VALUES ("
                            + protid + ", "
                            + sequence + ", "
                            + Integer.toString(sequence.length()) + ")";
                    stmt.executeUpdate(sql);
                }
            }
            
            sql = "INSERT INTO t_diseaseinfo (DiseaseID, Disease_Type) VALUES (0, 'Control');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO t_diseaseinfo (DiseaseID, Disease_Type) VALUES (1, 'Cancer_E');";
            stmt.executeUpdate(sql);
            
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            nodeLst = doc.getElementsByTagName("GlycoRecord");
            for(; gpid < nodeLst.getLength(); gpid++)
            {
                record = nodeLst.item(gpid);
                if(record.getNodeType() == Node.ELEMENT_NODE)
                {
                    e = (Element) record;
                    
                    nodes = e.getElementsByTagName("Mass");
                    mass = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("NET");
                    net = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("Protein");
                    protid = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    nodes = e.getElementsByTagName("Peptide");
                    peptide = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    nodes = e.getElementsByTagName("PeptideMass");
                    pepmass = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("Site");
                    site = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    nodes = e.getElementsByTagName("Glycan");
                    glycan = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    nodes = e.getElementsByTagName("GlycanMass");
                    glymass = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepresentativeCIDLength");
                    cidlen = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepresentativeCIDSpectra");
                    if(((Node) nodes.item(0)).getChildNodes().item(0) != null)
                    {
                        cidspec = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    }
                    else
                    {
                        cidspec = "NULL";
                    }
                    nodes = e.getElementsByTagName("RepresentativeHCDLength");
                    hcdlen = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepresentativeHCDSpectra");
                    if(((Node) nodes.item(0)).getChildNodes().item(0) != null)
                    {
                        hcdspec = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    }
                    else
                    {
                        hcdspec = "NULL";
                    }
                    nodes = e.getElementsByTagName("RepresentativeETDLength");
                    etdlen = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepresentativeETDSpectra");
                    if(((Node) nodes.item(0)).getChildNodes().item(0) != null)
                    {
                        etdspec = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                    }
                    else
                    {
                        etdspec = "NULL";
                    }
                    nodes = e.getElementsByTagName("RepCIDScore");
                    rcids = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepHCDScore");
                    rhcds = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    nodes = e.getElementsByTagName("RepETDScore");
                    retds = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                    
                    sql = "INSERT INTO t_gpinfo (gpID, Mass, NET, ProteinID, Peptide, Pep_Mass, Site, Glycan, Gly_Mass, "
                            + "Status, CID_Length, CID_Spectra, HCD_Length, HCD_Spectra, ETD_Length, ETD_Spectra, "
                            + "CID_Score, HCD_Score, ETD_Score) VALUES ("
                            + Integer.toString(gpid) + ", "
                            + mass + ", "
                            + net + ", "
                            + protid + ", "
                            + peptide + ", "
                            + pepmass + ", "
                            + site + ", "
                            + glycan + ", "
                            + glymass + ", 0, "
                            + cidlen + ", "
                            + cidspec + ", "
                            + hcdlen + ", "
                            + hcdspec + ", "
                            + etdlen + ", "
                            + etdspec + ", "
                            + rcids + ", "
                            + rhcds + ", "
                            + retds + ");";
                    stmt.executeUpdate(sql);
                    
                    innodeLst = e.getElementsByTagName("DatasetInfo");
                    dataset = innodeLst.item(0);
                    e = (Element) dataset;
                    innodeLst = e.getElementsByTagName("DatasetRecord");
                    for(int i = 0; i < innodeLst.getLength(); i++)
                    {
                        dataset = innodeLst.item(i);
                        if(dataset.getNodeType() == Node.ELEMENT_NODE)
                        {
                            e = (Element) dataset;
                            
                            nodes = e.getElementsByTagName("DatasetName");
                            name = "'" + ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue() + "'";
                            nodes = e.getElementsByTagName("DatasetType");
                            type = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            if(type.equals("Control"))
                            {
                                disease = 0;
                            }
                            else if(type.equals("Cancer_E"))
                            {
                                disease = 1;
                            }
                            nodes = e.getElementsByTagName("Abundance");
                            abundance = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("PrecursorMz");
                            precmz = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("PrecursorMass");
                            precmass = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("PrecursorCS");
                            preccs = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("PrecursorRT");
                            precrt = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("CIDScan");
                            cidscan = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("CIDScore");
                            cids = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("HCDScan");
                            hcdscan = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("HCDScore");
                            hcds = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("ETDScan");
                            etdscan = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            nodes = e.getElementsByTagName("ETDScore");
                            etds = ((Node) nodes.item(0)).getChildNodes().item(0).getNodeValue();
                            
                            if(Double.parseDouble(abundance) > 0)
                            {
                                sql = "INSERT INTO t_datasetinfo (dID, Dataset_Name, DiseaseID, Abundance, "
                                    + "Prec_Mz, Prec_Mass, Prec_Charge, Prec_Rt, "
                                    + "CID_Scan, CID_Score, HCD_Scan, HCD_Score, ETD_Scan, ETD_Score, gpID) VALUES ("
                                    + Integer.toString(did) + ", "
                                    + name + ", "
                                    + Integer.toString(disease) + ", "
                                    + abundance + ", "
                                    + precmz + ", "
                                    + precmass + ", "
                                    + preccs + ", "
                                    + precrt + ", "
                                    + cidscan + ", "
                                    + cids + ", "
                                    + hcdscan + ", "
                                    + hcds + ", "
                                    + etdscan + ", "
                                    + etds + ", "
                                    + Integer.toString(gpid) + ");";
                                stmt.executeUpdate(sql);
                            }
                        }
                        did++;
                    }
                }
            }
            reader.close();
        }
        catch(ParserConfigurationException ex)
        {
            ex.printStackTrace();
        }
        catch(SAXException ex)
        {
            ex.printStackTrace();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        XMLData xml = new XMLData();
        xml.xmlMysql();
    }
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String sql;
    private File file;
    private BufferedReader reader;
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;
    private Node record, dataset;
    private NodeList nodeLst, nodes, innodeLst;
    private Element e;
    private int did, gpid, disease;
    private String mass, net, protid, sequence, peptide, pepmass, site, glycan, glymass;
    private String cidlen, cidspec, hcdlen, hcdspec, etdlen, etdspec, rcids, rhcds, retds;
    private String name, type, abundance, precmz, precmass, preccs, precrt, hcdscan, hcds, cidscan, cids, etdscan, etds;
}