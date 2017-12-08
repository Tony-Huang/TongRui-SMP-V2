package com.hdp.smp.front.util;
/**
 * copywright @2015
 * @author hdp214@163.com
 * @version 1.0
 * @since 1.0
 */
import com.hdp.smp.model.Role;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.realm.RealmBase;
import org.apache.commons.codec.binary.Base64;

import org.hibernate.Session;

/**
 * the util class that change user credential
 */
public class CredentialUtil {

    protected  static List<Role> roles = getRoles();

    //protected static List<CategoryNameValue> cats = getCats();

    /**
     *used for authentication, an non-reversable encrption.
     * @param origialClearTextPasswd
     * @return
     */


    public static String encryptTomcatRealmPasswd(String origialClearTextPasswd) {
        String s1 = null;
           // RealmBase.Digest(origialClearTextPasswd, "MD5", null); //credential, algorithm{MD5,SHA,MD2}, encoding
        return s1;
    }

    public static String getCurentUser() {
        //need to use this for the security.
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        ExternalContext ectx = ctx.getExternalContext();
        HttpServletRequest request = null;//(HttpServletRequest) ectx.getRequest();

        String principalName = request.getUserPrincipal().getName();
        return principalName;

    }

    public static List<Role> getRoles() {
        if (roles == null) {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            roles = dao.getAll(session, Role.class);
            session.close();
        }
        return roles;
    }

//    public static List<SelectItem> getSelectionRoles(Locale lo) {
//        if (lo == null) {
//            lo = Locale.getDefault();
//        }
//        String lang = lo.getLanguage();
//
//        List<Role> roles = getRoles();
//        List<SelectItem> avrs = new ArrayList<SelectItem>();
//        for (Role r : roles) {
//            SelectItem si = new SelectItem();
//            if (lang.equalsIgnoreCase("en")) {
//                si.setLabel(r.getName_EN()); //English
//            } else if (lang.equalsIgnoreCase("zh")) {
//                si.setLabel(r.getName_CN()); //Chinese
//            }
//            si.setValue(r);
//            avrs.add(si);
//        }
//
//        return avrs;
//    }

   /***
    public static List<CategoryNameValue> getCats() {
        if (cats == null) {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            cats = dao.getAll(session, CategoryNameValue.class);
            session.close();
        }
        return cats;
    }  ***/


   /*****
    protected  static List<Shift> shifts = getShifts();
    public static List<Shift> getShifts() {
        if (shifts == null) {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            shifts = dao.getAll(session, Shift.class);
            session.close();
        }
        return shifts;
    } ***/


    /**
     * used for encode for cleartext, can be decoded later. Should use together with decodeBase64.
     */
    public static String encodeBase64(String originalText) {
        byte[] bytes = originalText.getBytes();
        String encodedStr = Base64.encodeBase64String(bytes);
        return encodedStr;
    }

    /**
     * used for decode of encoded text. Should use together with encodeBase64.
     */
    public static String decodeBase64(String encodedText) {
        byte[] bytes = Base64.decodeBase64(encodedText);
        String decodedStr = new String(bytes);
        return decodedStr;
    }


}
