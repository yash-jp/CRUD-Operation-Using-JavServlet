

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class Step7b_YashParekh
 */
@WebServlet("/Step7b_YashParekh")
public class Step7b_YashParekh extends HttpServlet {
	
	//CONNECTION VARIABELS
	String databaseName="humberevents";
	String tableName="registered";

	//DATABASE VARIABLES
	Connection connection = null;

	//REQUEST DISPATCHER
	RequestDispatcher rd=null;
	
	
	//VALIDATION VARIABLES
	private String email,title,firstName,lastName,dateOfBirth,type;
	String myFieldsNames[] = new String[] {"email","title","firstName","lastName","DOB","type"};
	
//	Form Processing Variables
	private ArrayList<String> arrayList = new ArrayList<String>();

			
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Step7b_YashParekh() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		getting data from the user
		email = getFormData("email", request).toString();
		title = getFormData("title", request).toString();
		firstName = getFormData("firstName", request).toString();
		lastName = getFormData("lastName", request).toString();
		dateOfBirth = getFormData("dateOfBirth",request).toString();
		type=getRadioData("type", request).toString();
		if(type==null) {
			type="";
		}
		
//		String message = "Thank you, "+title+" "+firstName+" "+lastName+", for registering in this event. We will contact you via "+email+". Your DOB is "+dateOfBirth+", and you are a "+type+".";
		
		String myFields[] = new String[] {email,title,firstName,lastName,dateOfBirth,type};
		
		validateEmptyFields(myFields,myFieldsNames);
		
		if(arrayList.isEmpty()) {
//		NOW WORK WITH DATAABSE
			if(connectToDatabase(databaseName)) {
				int confirmationCode=insertEmployee(email,firstName,lastName,dateOfBirth,title,type);
				if(confirmationCode==1062) {
//					1062 IS FOR INTEGRITY CONSTRAINT VIOLATION
					response.getWriter().append("</br><h2> email already exists!"+"</h2>");
//					rd.include(request, response);
				}
//				else if(confirmationCode==1406) {
////					1406 IS FOR Data Truncation: Data too long for column 
//					response.getWriter().append("</br><h2>"+employee.getEmployeeNumber()+" too long maximum 5 digits allowed!"+"</h2>");
//					rd.include(request, response);
//				}
				else if(confirmationCode==1) {
//					1 ROW HAS BEEN INSERTED
					response.getWriter().append("</br><h2> registered! "+firstName+"</h2>");
//					rd.include(request, response);
				}else {
					response.getWriter().append("</br><h2>"+"not added, Please try again!"+"</h2>");
//					rd.include(request, response);
				}
			}
			else {
//				NOT ABLE TO CONNECT WITH DATABASE, SO SHOW PROPER ERROR MESSAGE BY REDIRECTING TO PROPER PAGE
				response.getWriter().append("</br><h2>Not Able to Connect with "+databaseName+" database "+"please try again!</h2>");
//				REDIRECT TO PROPERPAGE
//				rd.include(request, response);
			}
			
		}else {
			for (String arrString : arrayList) {
				response.getWriter().append(arrString+"<br/>");
			}
			rd = getServletContext().getRequestDispatcher("/Step7a_YashParekh.html");
			rd.include(request, response);
		}
		
//		thankDialog(response,message);
	}
	
	private void validateEmptyFields(String[] var,String[] myStrings) {
		arrayList.clear();
		for(int i=0;i<var.length;i++) {
			if(var[i]=="") {
				arrayList.add(myStrings[i]+" can't be empty"+"\n");
			}
		}
	}
	
	private int insertEmployee(String email, String firstName, String lastName, String dob, String title,String type) {
		// TODO Auto-generated method stub
//		GENERATING QUERY
		String sqlQuery = "INSERT INTO registered (email, firstName, lastName, dob, title, status)" +
		        "VALUES (?,?,?,?,?,?)";
		
//		CREATING STATEMENT
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			if(preparedStatement!=null) {
//				CREATED PREPARED STATEMENT NOW FIRE IT BY SETTIGN VARIABLES
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, firstName);
				preparedStatement.setString(3, lastName);
				preparedStatement.setString(4, dob);
				preparedStatement.setString(5, title);
				preparedStatement.setString(6, type);
				
//				FIRE THE QUERY
				return preparedStatement.executeUpdate();
				
			}else {
//				PREPARED STATEMENT IS NULL, GIVE MESSAGE OF PLEASE TRY AGAIN
				return -3;
			}
		}
		
		catch(MySQLIntegrityConstraintViolationException pkViolation) {
//			INTEGRITYCONSTRAINTVIOLATION EITHER UNIQUE,PRIMARY,FOREIGN
			return pkViolation.getErrorCode();
			
		}
		catch (SQLException e) {
//			ANY OTHER KIND OF EXCEPTION
			System.out.println(e.getErrorCode());
			return e.getErrorCode();
		}
		
//		THIS BLOCK WILL ALWAYS BE EXECITED SO CLOSE CONNECTION AND OTHER DATABASE OBJECTS
		finally {
//			CLOSE CONNECTION
			try {
				if(connection!=null) {
					connection.close();
					connection=null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return e.getErrorCode();
			}
			
		}
		
	}
	
	
	private Object getFormData(String fieldName,HttpServletRequest request) {
		String object[] = request.getParameterValues(fieldName);

		if(object!=null) {

				return object[0];
		}else {

			return null;
		}

	}
	
	private String getRadioData(String fieldName,HttpServletRequest request) {
		String notification[] = request.getParameterValues(fieldName);
		if(notification!=null) {
			return notification[0];
		}
		else {
			return null;
		}
	}
	
	private String[] getCheckBoxData(String fieldName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String music[] = request.getParameterValues(fieldName);
		if(music!=null) {
			return music;
		}else {
			return null;
		}
	}
	
	
	
	private boolean connectToDatabase(String databaseName)  {
		// TODO Auto-generated method stub
		try {
//			LOAD THE DRIVERS
			Class.forName("com.mysql.jdbc.Driver");
			
//			INITIALIZE THE JDBC
			String dbURL = "jdbc:mysql://localhost:3306/"+databaseName;
			String username="root";
			String password="12345";
			
//			CREATE THE CONNECTION
			connection =  DriverManager.getConnection(dbURL, username, password);
			
			if(connection!=null) {
				return true;
			}
			else {
				return false;
			}
			
			
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
