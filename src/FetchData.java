import java.sql.*;
import java.io.*;

public class FetchData
{
    public FetchData(String id)
    {
        try
        {
	    this.id = id;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("hostname","username", "password");
            stmt = conn.createStatement();
            file = new File("result");
            bw = new BufferedWriter(new FileWriter(file));
            sb = new StringBuffer();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void fetchInfo()
    {
        try
        {
	    if (!id.startsWith("sp|"))
            {
                id = "%" + id + "%";
            }
            sql = "select * from t_gpinfo where proteinid like \"" + id + "\"";
            rs = stmt.executeQuery(sql);
            rs.next();
            gpid = rs.getInt("gpid");
            mass = rs.getDouble("mass");
            net = rs.getDouble("net");
            proteinid = rs.getString("proteinid");
            peptide = rs.getString("peptide");
            glycan = rs.getString("glycan");
            s_cid = rs.getDouble("cid_score");
            s_hcd = rs.getDouble("hcd_score");
            s_etd = rs.getDouble("etd_score");
            
            for(int i = 0; i < 22; i++)
            {
                sql = "select intensity from t_datasetinfo" + i + " where gpid=" + gpid;
                rs = stmt.executeQuery(sql);
                rs.next();
                intensity = rs.getDouble("intensity");
                if(intensity > 0)
                {
                    count++;
                }
            }
            
            sb.append("<tr class=\"odd\">\n");
            sb.append("<td><b>Protein ID:</b></td>\n");
            sb.append("<td>" + proteinid + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"even\">\n");
            sb.append("<td><b>Mass:</b></td>\n");
            sb.append("<td>" + mass + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"odd\">\n");
            sb.append("<td><b>NET:</b></td>\n");
            sb.append("<td>" + net + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"even\">\n");
            sb.append("<td><b>Peptide:</b></td>\n");
            sb.append("<td>" + peptide + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"odd\">\n");
            sb.append("<td><b>Glycan:</b></td>\n");
            sb.append("<td>" + glycan + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"even\">\n");
            sb.append("<td><b>CID Score:</b></td>\n");
            sb.append("<td>" + s_cid + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"odd\">\n");
            sb.append("<td><b>HCD Score:</b></td>\n");
            sb.append("<td>" + s_hcd + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"even\">\n");
            sb.append("<td><b>ETD Score:</b></td>\n");
            sb.append("<td>" + s_etd + "</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr class=\"odd\">\n");
            sb.append("<td><b>Count:</b></td>\n");
            sb.append("<td>" + count + "</td>\n");
            sb.append("</tr>\n");

            System.out.println(sb.toString());
            //bw.write(sb.toString());
            bw.close();
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
    
    public static void main(String args[])
    {
        FetchData fd = new FetchData(args[0]);
        
        fd.fetchInfo();
        fd.closeConn();
    }
    
    File file = null;
    BufferedWriter bw = null;
    Connection conn = null;
    Statement stmt = null;
    StringBuffer sb = null;
    ResultSet rs = null;
    String sql, id, proteinid, peptide, glycan;
    int gpid, count = 0;
    Double mass, net, s_cid, s_hcd, s_etd, intensity;
}