/*
Zhimin Lin
ISTE 330 PE05
*/
import java.io.*;
import java.util.logging.*;
import java.sql.*;
import java.util.*;

public class DLException extends SQLException
{  
   //constructor that accepts a single parameter of type Exception
   public DLException(SQLException sqle)
   {
      super(sqle);
      log(sqle.toString());
   }
   
   //constructor that accepts a parameter of type Exception and additional string
   public DLException(SQLException sqle, String sqlString)
   {
      super(sqle);
      log("DLException cutsom error: " + sqlString);
      log(sqle.toString());
   }
   
   //Create log file
   public void log(String errorMsg)
   {
      Logger logger = Logger.getLogger("MyLog");  
      FileHandler fh;  

      try 
      {   
         fh = new FileHandler("MyLogFile.log",true);  
         logger.addHandler(fh);
         SimpleFormatter formatter = new SimpleFormatter();  
         fh.setFormatter(formatter);  
     
         logger.info(errorMsg);  
      } 
      catch (SecurityException e) 
      {  
         e.printStackTrace();  
      }
      catch (IOException e) 
      {  
         e.printStackTrace();  
      }  
    

   }
   
   
}
