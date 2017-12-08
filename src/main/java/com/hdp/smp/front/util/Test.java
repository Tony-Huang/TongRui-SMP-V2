package com.hdp.smp.front.util;

import com.hdp.smp.model.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class Test {
    public Test() {
        super();
    }

    static void testClassName() {
        Test test = new Test();
        Class clazz = User.class;
        System.out.println("class name=" + clazz.getName() + " , canonical name=" + clazz.getCanonicalName());
    }
    
    static void testResourceBundle() {
         ResourceBundle rb   = ResourceBundle.getBundle("com.hdp.smp.front.ui.UIBundle",Locale.forLanguageTag("CN"));
         String msg = rb.getString("monitor.test.msg.error");
         System.out.println("msg="+msg);
        }
    
    static void testResourceBundle2() {
        Locale local = Locale.CHINESE;
        Locale defaultLocale = Locale.getDefault();
        
        System.out.println("locale="+local +" defaultLocale="+Locale.getDefault());
         ResourceBundle rb   = ResourceBundle.getBundle("com.hdp.smp.front.ui.UIBundle",local);
         String msg = rb.getString("monitor.test.msg.error");
         System.out.println("msg="+msg);
        }
    
    static void testResourceBundle3() {
          Locale local = Locale.CHINA;
            System.out.println("locale="+local );
         ResourceBundle rb   = ResourceBundle.getBundle("com.hdp.smp.front.ui.UIBundle",Locale.CHINA);
         String msg = rb.getString("monitor.test.msg.error");
         System.out.println("msg="+msg);
        }

    public static void main(String[] args) {
        testResourceBundle();
        testResourceBundle2();
        testResourceBundle3();
    }
}
