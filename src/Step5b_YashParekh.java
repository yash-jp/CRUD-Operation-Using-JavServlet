

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Step5_YashParekh
 */
@WebServlet("/Step5b_YashParekh")
public class Step5b_YashParekh extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	Form Processing Variables
	private ArrayList<String> arrayList = new ArrayList<String>();
	private String email,title,firstName,lastName,dateOfBirth,type;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Step5b_YashParekh() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		getting data from the user
		email = getFormData("email", request).toString();
		title = getFormData("title", request).toString();
		firstName = getFormData("firstName", request).toString();
		lastName = getFormData("lastName", request).toString();
		dateOfBirth = getFormData("dateOfBirth",request).toString();
		type=getRadioData("type", request).toString();
		
		String message = "Thank you, "+title+" "+firstName+" "+lastName+", for registering in this event. We will contact you via "+email+". Your DOB is "+dateOfBirth+", and you are a "+type+".";
		
		thankDialog(response,message);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

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
	
	private void thankDialog(HttpServletResponse response,String message) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
	    out.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>Thank You</TITLE></HEAD>\n" +
	                "<link rel=\"stylesheet\" href=\"Step3_YashParekh.css\">\n" +
	                "<BODY><div class='thankYou'>\n" +
	                "<p>"+message+"</p>\n"+
	                "</div></BODY></HTML>");
	}

}
