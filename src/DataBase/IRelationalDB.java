package DataBase;

import java.sql.ResultSet;

public interface IRelationalDB {
    boolean executeSQLCommand(String toExecute);
    public boolean executeInsertCommand(String[] fields, String toExecute);
    public String executeSelectCommand(String sql);
    public String executeSelectPWCommand(String sql);
    public ResultSet querry(String sql);
}
