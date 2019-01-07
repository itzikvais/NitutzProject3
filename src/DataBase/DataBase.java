package DataBase;
import java.sql.*;

public class DataBase implements IRelationalDB {
    /**
     * a sample database
     */
    private String location;
    private String fileName;
    private Connection conn;


    private static DataBase instance;

    public void insert(Adb entry){
    }
    public static DataBase getInstance(){
        if (instance==null)
            instance = new DataBase("projectDB");
        return instance;
    }

    private DataBase(String fileName) {
        this.fileName=fileName;
        this.conn = null;
        location = "jdbc:sqlite:" + fileName;
    }
    public void deleteTable(){
        try (Connection conn = DriverManager.getConnection(location)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sqlCommand = "DROP TABLE IF EXISTS 'transactions'" ;

                System.out.println( "output : " + stmt.executeUpdate( sqlCommand ) );

                stmt.close();
                     // commit after execute sql command
                //COMMIT TRANSACTION makes all data modifications performed since
                //the start of the transaction a permanent part of the database,
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createNewDatabase() {

        String url = location;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected Connection create() {

        conn = null;
        try {
            // db parameters
            //String url = "jdbc:sqlite:nituzDB.sqlite";
            // create a connection to the database
            String sql = "CREATE TABLE ids (\n"
                    + "	vacationID INTEGER NOT NULL,\n"
                    + "	messageID INTEGER NOT NULL, \n"
                    + " PRIMARY KEY (vacationID,messageID)"
                    + ");";
            try (Connection conn = DriverManager.getConnection(location);
                 Statement stmt = conn.createStatement()) {
                // create a new table
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            if (conn != null) {

                System.out.println("A new database, " + fileName + ", has been connected to.");
            }

            System.out.println("Connection to " + fileName + " has been established.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(location);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    protected void disConnect() {
        try {
            if (conn != null) {
                conn.close();
                this.conn = null;
                System.out.print("Disconmected from " + fileName );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Create a new table in the test database
     *
     */
    public boolean executeInsertCommand(String[] fields,String toExecute){
        boolean success = false;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(toExecute)) {
            for (int i = 1; i <=fields.length ; i++) {
                pstmt.setString(i, fields[i-1]);
            }
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    @Override
    public String executeSelectCommand(String sql) {
        String record = null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                record=rs.getString("username") +  "\t" +
                        rs.getString("password") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getString("birth_date") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("last_name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return record;
    }
    public String executeSelectPWCommand(String sql) {
        String record = null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            record=rs.getString("Password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return record;
    }

    @Override
    public ResultSet querry(String sql) {
        Connection conn = connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean executeSQLCommand(String toExecute)
    {
        if (!isValidInput(toExecute)) {
            System.out.println("invalid command");
            return false;
        }
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(toExecute)) {
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     *
     * @param toCheck input to check
     * @return
     */
    private boolean isValidInput(String toCheck){
        return toCheck != null && toCheck != "";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataBase db = new DataBase("Nituz.db");
        //db.deleteTable();
        db.create();
        //db.createNewDatabase( );
        //db.connect();
        //String[] fields={"Itzikvais","1234","a@a","Itzik","Vaisman","28/04/93",null};
        //db.executeInsertCommand( fields,"INSERT INTO users(username,password,email,name,last_name,birth_date,image) VALUES(?,?,?,?,?,?,?)" );
        //System.out.println(db.executeSelectCommand( "SELECT * FROM users" ));
        db.disConnect();
    }

}
