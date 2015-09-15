// TODO: change to import only the necessary classes.
import java.sql.*;

public class Proxy {
  private static final String m_dbdriver; 
  private static final String m_dbconnection;
  private Connection m_conn;
  private ArrayList<SchemaModificationOperator> m_smos;
  private Statement m_stmt;
  public Proxy(String username, String password) {
    //default initialization
    m_dbdriver = "com.mysql.jdbc.Driver";
    m_dbconnection = "jdbc:postgressql://localhost/test";
    m_username = username;
    m_password = password;
    connect();
  }

  //establish connection to the database;
  private connect() {
    Class.forName(m_dbdriver);
    conn = DriverManager.getConnection(m_dbconnection, m_username, m_password);
    m_stmt = conn.createStatement;
  }

  public ResultSet transformQuery(String queryString) {
    ResultSet rs = m_stmt.executeQuery(queryString);
    for (SchemaModificationOperator smo: m_smos){
      smo.transform(queryString);
    }
    // TODO: some error checking before returning the resultset
    return rs;
  }

  // submit a Schema Modification Operator
  public int submitSMO(String smostatement) {
    SchemaModificationOperator smo = new SchemaModificationOperator(smostatement);
    return submitSMO(smo);
  }

  public int submitSMO(SchemaModificationOperator smo) {
    // 1. Make the necessary changes for the smo
    modifyDatabase(smo, m_conn);
    // 2. add the smo to the list
    m_smos.add(smo);
    // 3. atomically start translating queries
  }

  public void close(){
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
  }
}
