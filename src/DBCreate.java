import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBCreate
{
    public DBCreate()
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
    
    void createProt()
    {
        try
        {
            sql = "CREATE TABLE t_proteininfo("
                    + "ProteinID  VARCHAR(50) NOT NULL PRIMARY KEY, "
                    + "Sequence   VARCHAR(10000), "
                    + "Length   INTEGER)";
            stmt.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void createGPInfo()
    {
        try
        {
            sql = "CREATE TABLE t_gpinfo("
                    + "gpID  INTEGER NOT NULL PRIMARY KEY, "
                    + "Mass  DOUBLE, "
                    + "NET  DOUBLE, "
                    + "ProteinID  VARCHAR(50), "
                    + "Peptide  VARCHAR(100), "
                    + "Pep_Mass DOUBLE, "
                    + "Site  INTEGER, "
                    + "Glycan  VARCHAR(100), "
                    + "Gly_Mass DOUBLE, "
                    + "CID_Length INT, "
                    + "CID_Spectra TEXT, "
                    + "HCD_Length INT, "
                    + "HCD_Spectra TEXT, "
                    + "ETD_Length INT, "
                    + "ETD_Spectra TEXT, "
                    + "CID_Score  DOUBLE, "
                    + "HCD_Score  DOUBLE, "
                    + "ETD_Score  DOUBLE, "
                    + "FOREIGN KEY (ProteinID) REFERENCES t_proteininfo(ProteinID))";
            stmt.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void createDisInfo()
    {
	try
	{
            sql = "CREATE TABLE t_diseaseinfo("
                    + "DiseaseID  TINYINT NOT NULL PRIMARY KEY, "
                    + "Disease_Type  VARCHAR(20))";
            stmt.executeUpdate(sql);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
    }
    
    void createDSInfo()
    {
        try
        {
            sql = "CREATE TABLE t_datasetinfo("
                    + "dID INTEGER NOT NULL PRIMARY KEY, "
                    + "Dataset_Name  VARCHAR(10), "
                    + "DiseaseID TINYINT, "
                    + "Abundance  DOUBLE, "
		    + "Prec_Mz  DOUBLE, "
                    + "Prec_mass  DOUBLE, "
		    + "Prec_Charge  TINYINT, "
                    + "Prec_Rt DOUBLE, "
                    + "CID_Scan INT, "
		    + "CID_Score DOUBLE, "
                    + "HCD_Scan INT, "
		    + "HCD_Score DOUBLE, "
                    + "ETD_Scan INT, "
		    + "ETD_Score DOUBLE, "
                    + "gpID  INTEGER, "
                    + "FOREIGN KEY (gpID) REFERENCES t_gpinfo(gpID), "
		    + "FOREIGN KEY (DiseaseID) REFERENCES t_diseaseinfo(DiseaseID))";
            stmt.executeUpdate(sql);
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
        DBCreate dbc = new DBCreate();
        
        dbc.createProt();
	dbc.createDisInfo();
        dbc.createGPInfo();
        dbc.createDSInfo();
        
        dbc.closeConn();
    }
    
    public Connection conn = null;
    public Statement stmt = null;
    public String sql = null;
}
