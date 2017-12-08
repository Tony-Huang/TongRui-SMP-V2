package com.hdp.smp.front.util;

import java.util.Locale;
import java.util.ResourceBundle;

//import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public class LangSetting {
    
    public static final Logger log = Logger.getLogger(LangSetting.class);
    public static final String RES_BUNDLE="com.hdp.smp.front.ui.SMPUIBundle";//com.hdp.smp.front.ui.UIBundle
    public static final String RES_BUNDLE_CN="com.hdp.smp.front.ui.SMPUIBundle_zh";//com.hdp.smp.front.ui.UIBundle_CN
    public static final String RES_BUNDLE_EN="com.hdp.smp.front.ui.SMPUIBundle_en";//com.hdp.smp.front.ui.UIBundle_EN
    
    public LangSetting() {
        super();
    }

   public  static ResourceBundle getDefaultResourceBundle() {
        ResourceBundle rb = ResourceBundle.getBundle(RES_BUNDLE, Locale.forLanguageTag("zh"));//CN
        return rb;
    }

   public static ResourceBundle getResourceBundle(Locale local) {
        ResourceBundle rb = ResourceBundle.getBundle(RES_BUNDLE, local);
        if(rb == null){
            log.info("resourcebundle for locale:"+local +" does not exist, so use default locale resourcebundle...");
            rb = ResourceBundle.getBundle(RES_BUNDLE, Locale.getDefault());
            if (rb == null){
              rb = LangSetting.getResourceBundle("zh"); //CN
            }
        }
        return rb;
    }

   public static ResourceBundle getResourceBundle(String language) {
        ResourceBundle rb = ResourceBundle.getBundle(RES_BUNDLE, Locale.forLanguageTag(language));
        return rb;
    }

   public static ResourceBundle getResourceBundle_CN() {
        ResourceBundle rb = ResourceBundle.getBundle(RES_BUNDLE_CN);
        return rb;
    }

    static ResourceBundle getResourceBundle_EN() {
        ResourceBundle rb = ResourceBundle.getBundle(RES_BUNDLE_EN);
        return rb;
    }
    
    public static Locale getUserLocale() {
        Locale local = null;//FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        log.info("user locale=" + local);
        return local;
    }
    
    public static  boolean isCN() {
            //convert by locale
            String lang = getUserLocale().getLanguage();
            log.info("user language="+lang);
            boolean isCN=lang.equalsIgnoreCase("zh");
            return isCN;
        }
    
    public static  String getMsg(String key, Locale local) {
        ResourceBundle rb = LangSetting.getResourceBundle(local);
        return rb.getString(key);
    }
    
    public static void main(String[]  args) {
        System.out.println( Locale.forLanguageTag("zh") );
        }

}
