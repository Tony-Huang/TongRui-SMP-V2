package com.hdp.smp.bigscreen.data;

import com.hdp.smp.bigscreen.xml.XMLWR;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class BigScreenData
 */
@WebServlet("/BigScreenData")
public class BigScreenData extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final Logger log = Logger.getLogger(BigScreenData.class); 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BigScreenData() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.generateData(request, response);
	}
	
	protected void generateData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("******start getting  data for big screen******");
                
                //boolean screenNOParamOK =false;
                int screenNO = -1;
                String screenNOParam= request.getParameter("no");
                if (screenNOParam==null) {
                        screenNOParam= request.getParameter("NO");
                    }
                try {
                screenNO = Integer.parseInt(screenNOParam);
                } catch (Exception e ) {
                    log.error("The BigScreen NO is not a number! please check the caller's parameter.");
                    }
                
                
		XMLWR xw = new XMLWR();
		String docStr= xw.writeDoc2String(xw.genDocWithRoot(screenNO));
		response.getWriter().println(docStr);
		
		log.info("******Successfully getting data for big screen ******");
	}
        
        
	

}
