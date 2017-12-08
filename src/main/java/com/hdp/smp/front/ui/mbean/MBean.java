package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.util.LangSetting;

import java.util.Locale;
import java.util.ResourceBundle;

//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public  class MBean {
    public static final Logger log = Logger.getLogger(MBean.class);

    public MBean() {
        super();
    }

    public Locale getUserLocale() {
        Locale local = null ;//FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        log.info("user locale=" + local);
        return local;
    }

    public String getMsg(String key) {
        ResourceBundle rb = LangSetting.getResourceBundle(this.getUserLocale());
        return rb.getString(key);
    }

    public String getMsg(String key, Locale local) {
        ResourceBundle rb = LangSetting.getResourceBundle(local);
        return rb.getString(key);
    }

    public String getMsg(String key, String languageTag) {
        ResourceBundle rb = LangSetting.getResourceBundle(languageTag);
        return rb.getString(key);
    }

    public void showInfo(String head, String detailMsg) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, head, detailMsg);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void showError(String head, String detailMsg) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, head, detailMsg);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


}
