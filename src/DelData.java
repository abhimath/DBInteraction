import java.sql.*;

public class DelData
{
    public DelData()
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
    
    void delInfo()
    {
        try
        {
            for(int i = 0; i < 22; i++)
            {
                for(int j = 1; j < 306; j++)
                {
                    sql = "DELETE FROM T_DatasetInfo" + i + " WHERE dID=" + j;
                    stmt.executeUpdate(sql);
                    sql = "DELETE FROM T_HCDSpectra" + i + " WHERE HCD=" + j;
                    stmt.executeUpdate(sql);
                    sql = "DELETE FROM T_CIDSpectra" + i + " WHERE CID=" + j;
                    stmt.executeUpdate(sql);
                    sql = "DELETE FROM T_ETDSpectra" + i + " WHERE ETD=" + j;
                    stmt.executeUpdate(sql);
                }
            }
            for(int i = 1; i < 306; i++)
            {
                sql = "DELETE FROM T_gpInfo WHERE Site=" + i;
                stmt.executeUpdate(sql);
            }
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
        DelData dd = new DelData();
        
        dd.delInfo();
        dd.closeConn();
    }
    
    Connection conn = null;
    Statement stmt = null;
    String sql = null;
}