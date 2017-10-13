/*
Zhimin Lin
ISTE 330 PE05
*/

import java.sql.*;
import java.util.ArrayList;
import java.util.*;


public class MySQLDatabase
{
   public String mydb_url;
   public String mydb_driver;
   public String mydb_userID;
   public String mydb_password;
   public Connection mydb_conn;
   public int numOfField;
   
  
   public MySQLDatabase()
   {
   
   }
  
   public MySQLDatabase(String sql_mydb_url, String sql_mydb_driver, String sql_mydb_userID, String sql_mydb_passward, Connection sql_mydb_conn)
   {
      mydb_url =  sql_mydb_url;
      mydb_driver = sql_mydb_driver;
      mydb_userID = sql_mydb_userID;
      mydb_password = sql_mydb_passward;
      mydb_conn = sql_mydb_conn;
   }  
   
   // Connect to Database
   public boolean connect() throws DLException
    {
      boolean ableToConnect= false;
       try
       {
         mydb_conn = DriverManager.getConnection( mydb_url, mydb_userID, mydb_password );
         ableToConnect =  mydb_conn.isValid(1);
       }
       catch(SQLException sqle)
       {
         throw new DLException(sqle);
       }
          
       return ableToConnect;
    }
     
    // Close Database connection
    public boolean close()
    {
       boolean closed = false;
       try
       {
            mydb_conn.close();
            closed = mydb_conn.isClosed();
       }
       catch(SQLException sqle)
       {
            return closed;
       }
       
       return closed;
    }
    
    public ResultSet setRs(String SQLString) throws DLException
    {
      try
      {
         Statement stmnt = mydb_conn.createStatement();
         ResultSet rs= stmnt.executeQuery(SQLString);
         return rs;
      }
      catch(SQLException sqle)
      {
         throw new DLException(sqle,"setRs() method ERROR!");
      }
    }
    
    public ResultSetMetaData setMetadata(String SQLString) throws DLException
    {
      try
      {
         ResultSetMetaData metadata = setRs(SQLString).getMetaData();
         return metadata;
      }
      catch(SQLException sqle)
      {
         throw new DLException(sqle,"setRs() method ERROR!");
      }
    }
    
    public int setNumOfField(String SQLString) throws DLException
    {
      try
      {
         numOfField = setMetadata(SQLString).getColumnCount();
         return numOfField;
      }
      catch(SQLException sqle)
      {
         throw new DLException(sqle,"setNumOfField() method ERROR!");
      }
    }
    
    
    
    
    //getData perform the query that was passed in and then covert the resultset in a simple 2d arraylist
    public ArrayList<ArrayList<String>> getData(String SQLString, int numOfField) throws DLException
    {
      ArrayList<ArrayList<String>> arr = new ArrayList<>();
      try
      {   
         
         ResultSet rs = setRs(SQLString);
         numOfField = setNumOfField(SQLString);
         
         int row = 0;
         while(rs.next())
         {
            ArrayList<String> arraylist = new ArrayList<String>();
            
            for(int i = 1; i <= numOfField; i++)
            {
               arraylist.add(rs.getString(i));
            }
            arr.add(arraylist);
            
         }
      }
      catch(SQLException sqle)
      {
         throw new DLException(sqle,"Invalid SQL query !!");
      }
      
      return arr;
    }
    
    //setData perform the query that was passed
    //If query runs successfully, it will return true. Otherwise it should return false.
    public boolean setData(String SQLString)
    {
      boolean result = false; 
      try
      {
         Statement stmnt = mydb_conn.createStatement();
         int rc= stmnt.executeUpdate(SQLString);
         if(rc == 0)
         {
            result = false;
         }
         else
         {
            result = true;
         }
      }
      catch(SQLException sqle)
      {
      
      }
      return result;
   }
   
   
   public int getMaxColWidth(String ColName, ArrayList<String> ColData)
   {
      int maxSize = ColName.length();
      for (int i=0; i<ColData.size(); i++)
      {
         if (ColData.get(i).length() > maxSize)
         {
            maxSize = ColData.get(i).length();
         }
      }
      return maxSize;
   }
   
   // descTable method allow exection of any SELECT query
   // Print how many fields were retrieved
   // Print two column report of the Filed Name and Column Type with Nice format
   // Print output of SELECT statement
   public void descTable(String SQLString) throws DLException
   {
      try
      {
         int columnNum = setNumOfField(SQLString);
         System.out.println("\n-------------------------------------");
         System.out.printf(String.format("%20s %15s ","Column Name |", "Column Type"));
         System.out.println("\n-------------------------------------");
         
         ArrayList<String> colNameList =new ArrayList<String> ();        
         for(int i=1; i <= columnNum; i++)
         {
            String columnName = setMetadata(SQLString).getColumnName(i);
            String columnType = setMetadata(SQLString).getColumnTypeName(i);
            int columnsize = setMetadata(SQLString).getColumnDisplaySize(i);
            colNameList.add(columnName);
            
            if(columnName.length() >= 20)
            {
               columnName = columnName.substring(0,15) + "...";
            }
            System.out.println(String.format("%20s %15s ",columnName+" |",columnType));
         }
         System.out.println("-------------------------------------");
         System.out.println("Total Column: " + columnNum);
         
        
         ArrayList<Integer> columnWidth = new ArrayList<Integer>();
         ArrayList<ArrayList<String>> arr = getData(SQLString,columnNum);
         
         for (int j=0; j < columnNum; j++)
         {
         
            ArrayList<String> colData = new ArrayList<String>();
            for(int i=0; i< arr.size(); i++)
            {
               if (arr.get(i).get(j) == null)
               {
                  colData.add("");
               }
               else
               {
                  colData.add(arr.get(i).get(j));
               }
            }
            columnWidth.add(getMaxColWidth(colNameList.get(j), colData));
         }
                 
         for(int i = 0; i< columnNum; i++)
         {
            System.out.printf(String.format("%"+ Integer.toString(columnWidth.get(i)) + "s ", colNameList.get(i))); 
         }
         System.out.println();
         
         for(int i = 0; i < arr.size(); i++)
         {
            for(int j = 0; j < arr.get(i).size(); j++)
            {
               System.out.printf(String.format("%"+ Integer.toString(columnWidth.get(j)) + "s ", arr.get(i).get(j)));
            }
            System.out.println();
         }
         
      }
      catch(SQLException sqle)
      {
         throw new DLException(sqle,"descTable() method ERROR!");
      } 
   }
   
}

