import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Coordinates
{
    public Coordinates()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("hostname","username", "password");
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            file = new File("coordata.csv");
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
            sql = "select * from t_gpinfo";
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                gpid = rs.getInt("gpid");
                mass = rs.getDouble("mass");
                net = rs.getDouble("net");
            
                for(int i = 0; i < 22; i++)
                {
                    sql2 = "select intensity from t_datasetinfo" + i + " where gpid=" + gpid;
                    rs2 = stmt2.executeQuery(sql2);
                    rs2.next();
                    intensity = rs2.getDouble("intensity");
                    if(intensity > 0)
                    {
                        count++;
                    }
                }
                
                String temp = mass + "," + net + "\n";
                count = 0;
                sb.append(temp);
            }
            
            bw.write(sb.toString());
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
        Coordinates cord = new Coordinates();
        
        cord.fetchInfo();
        cord.closeConn();
    }
    
    File file = null;
    BufferedWriter bw = null;
    StringBuffer sb = null;
    Connection conn = null;
    Statement stmt = null, stmt2 = null;
    ResultSet rs = null, rs2 = null;
    String sql, sql2, proteinid;
    int gpid, count = 0;
    Double mass, net, intensity;
}