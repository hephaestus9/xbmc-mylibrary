
package db;

import btv.db.DbType;
import btv.db.SingleStatementDatabase;
import java.sql.*;
import utilities.Config;

import static utilities.Constants.*;

public class SQLiteDB extends SingleStatementDatabase
{
    public SQLiteDB(String DBPath)
    {
        super(DbType.SQLITE_NEW_DB, DBPath);
    }
    
    public synchronized boolean hasColumn(String tablename, String columnName)
    {
        String sql = "PRAGMA table_info("+tablename+")";
        boolean hasColumn = false;
        PreparedStatement stmt = null;
        try
        {
            stmt = getStatement(sql);//conn.createStatement();            
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                String nextColumnName = rs.getString("name");
                if(nextColumnName.equalsIgnoreCase(columnName))
                {
                    hasColumn =true;
                    break;
                }
            }
            rs.close();
            return hasColumn;
        }
        catch(Exception x)
        {
            Logger.ERROR( "Failed to determine if table \""+tablename+"\" has column named \""+columnName+"\"",x);
            return false;
        }
        finally
        {
            //if(stmt != null) try{stmt.close();}catch(Exception ignored){}
            closeStatement();
        }
    }
}
