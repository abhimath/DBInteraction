import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

public class FillData
{
    public FillData()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("hostname","username", "password");
            stmt = conn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void extractInfo()
    {
        File file = new File("All22DatasetsExample.csv");
        File tfile = new File("form.txt");
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedReader treader = new BufferedReader(new FileReader(tfile));
            sql = null;
            
            String ttext = null;
            while((ttext = treader.readLine()) != null)
            {
                tst = new StringTokenizer(ttext, ",");
                String temp1 = tst.nextToken();
                String temp2 = tst.nextToken();
                sql = "INSERT INTO t_proteininfo (ProteinID, Sequence, Length) VALUES (\"" + temp1 + "\", \"" + temp2 + "\", " + temp2.length() + ")";
                stmt.executeUpdate(sql);
            }
            
            sql = "INSERT INTO t_diseaseinfo (DiseaseID, Disease_type) VALUES (0, \"Control\")";
            stmt.executeUpdate(sql);
            
            sql = "INSERT INTO t_diseaseinfo (DiseaseID, Disease_type) VALUES (1, \"Cancer\")";
            stmt.executeUpdate(sql);
            
            String text = null;
            int c = 0, d = 0;
            text = reader.readLine();
            
            while((text = reader.readLine()) != null)
            {
                st = new StringTokenizer(text, ",");
                st.nextToken();
                mass = st.nextToken();
                net = st.nextToken();
                protid = st.nextToken();
                peptide = st.nextToken();
                glycan = st.nextToken();
                
                sql = "INSERT INTO t_gpinfo (gpID, Mass, NET, ProteinID, Peptide, Glycan) VALUES ("
                        + Integer.toString(c++) + ", "
                        + mass + ", "
                        + net + ", '"
                        + protid + "', '"
                        + peptide + "', '"
                        + glycan + "')";
                stmt.executeUpdate(sql);
                
                while(st.hasMoreTokens())
                {
                    did = st.nextToken();
                    hcd = st.nextToken();
                    double h = Double.parseDouble(hcd);
                    if(hcd_s > h)
                    {
                        hcd_s = h;
                    }
                    cid = st.nextToken();
                    double i = Double.parseDouble(cid);
                    if(cid_s < i)
                    {
                        cid_s = i;
                    }
                    st.nextToken();
                    abundance = st.nextToken();
                    if(Integer.parseInt(did) < 11)
                    {
                        disease = "1";
                    }
                    else
                    {
                        disease = "0";
                    }
                    
                    if(Double.parseDouble(abundance) > 0)
                    {
                        sql = "INSERT INTO t_datasetinfo (dID, Abundance, gpID, CID_Score, HCD_Score, DiseaseID) VALUES ("
                            + Integer.toString(d) + ", "
                            + abundance + ", "
                            + Integer.toString(c-1) + ", "
                            + cid + ", "
                            + hcd + ", "
                            + disease + ")";
                        stmt.executeUpdate(sql);
                    }
                    d++;
                }
                
                sql = "UPDATE t_gpinfo SET HCD_Score=" + hcd_s + ", CID_Score=" + cid_s + " WHERE gpID=" + Integer.toString(c-1);
                stmt.executeUpdate(sql);
                hcd_s = 100.0;
                cid_s = 0.0;
            }
            
            reader.close();
            treader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    void closeConn()
    {
        try
        {
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String...args)
    {
        FillData fd = new FillData();
        
        fd.extractInfo();
        fd.closeConn();
    }
    
    Connection conn = null;
    Statement stmt = null;
    String sql = null;
    ResultSet rs = null;
    StringTokenizer st, tst;
    String mass, net, protid, peptide, glycan, did, hcd, cid, abundance, disease;
    double hcd_s = 100.0, cid_s = 0.0;
}