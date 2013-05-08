import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.*;
import sun.misc.BASE64Decoder;

public class base64
{
    public static void main(String args[])
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("hostname", "username", "password");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT HCD_Length, HCD_Spectra FROM t_gpinfo;";
            ResultSet rs = stmt.executeQuery(sql);
            float mzmax = 0f, intensitymax = 0f, mzmin = 1000000f, intensitymin = 1000000f;
            while(rs.next())
            {
                int peaks = rs.getInt("HCD_Length");
                String encodedBytes = rs.getString("HCD_Spectra");
                if(peaks > 0)
                {
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
                    ByteBuffer bf = ByteBuffer.wrap(decodedBytes);
                    for(int i = 0; i < peaks; i++)
                    {
                        float mz = bf.getFloat();
                        if(mzmax < mz)
                        {
                            mzmax = mz;
                        }
                        if(mzmin > mz)
                        {
                            mzmin = mz;
                        }
                        float intensity = bf.getFloat();
                        if(intensitymax < intensity)
                        {
                            intensitymax = intensity;
                        }
                        if(intensitymin > intensity)
                        {
                            intensitymin = intensity;
                        }
                    }
                }
            }
            
            System.out.println(mzmax + "\t" + mzmin);
            System.out.println(intensitymax + "\t" + intensitymin);
            
            stmt.close();
            rs.close();
            conn.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
