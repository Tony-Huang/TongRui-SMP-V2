package com.hdp.camel.processor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by admin on 2017/11/25.
 */
public class MyServletProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("***************** MyServletProcessor ************");
        System.out.println("in = "+exchange.getIn() +" class="+exchange.getIn().getClass());
        System.out.println( "headers = "+ exchange.getIn().getHeaders() );

        System.out.println ( "body = " + exchange.getIn().getBody() );


        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
        System.out.println ( "HttpServletRequest = " + request );

        System.out.println("http.params.TC="+request.getParameter("TC"));
        System.out.println("http.session="+ request.getSession() );
        System.out.println("http.requestURL="+request.getRequestURL() );

        HttpServletResponse response = exchange.getIn().getBody(HttpServletResponse.class);
        System.out.println ( "HttpServletResponse = " + response );
        response.setContentType("application/json");
        response.setStatus(200);
        //response.getWriter().println(" { \"success\" : true } ");
        exchange.getOut().setBody(" { \"success\" : true } ");

    }
}
