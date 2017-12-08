package com.hdp.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import static org.apache.camel.Exchange.HTTP_QUERY;

/**
 * Created by admin on 2017/11/24.
 */
public class MyProcess implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println( "headers="+ exchange.getIn().getHeaders() );
        String queryStr= (String)exchange.getIn().getHeaders().get(HTTP_QUERY);

        System.out.println("query string="+queryStr);

        validateTC(exchange);
    }


    private void validateTC(Exchange ex) throws Exception{
        String queryStr = (String)ex.getIn().getHeaders().get(HTTP_QUERY);
        if (queryStr == null) {
            throw new Exception("Not Accept T&C");
        }

        String[] params = queryStr.split("&");
        String tc = null ;
        if (params != null) {
            for(String p:params) {
                String[] prop = p.split("=");
                if (prop!=null && prop.length >1) {
                    String key = prop[0];
                    String value = prop[1];
                    if (key.equalsIgnoreCase("tc")) {
                        tc= value;
                    }
                }
            }
        }

        System.out.println("TC="+tc);

        if (tc==null || (tc != null && !tc.equalsIgnoreCase("true") ) ) {
            throw new Exception("Not Accept T&C");
        }

    }


}
