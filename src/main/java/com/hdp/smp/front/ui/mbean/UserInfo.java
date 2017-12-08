package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.util.CredentialUtil;
import com.hdp.smp.model.User;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.UserDAO;

import org.hibernate.Session;


/**
 * used for hloding user roles and priviledges.
 * Can be used for UI component to check user authorization.
 * global for all mbeans
 * This should be a session bean.
 */
public class UserInfo {
    
    public static final String admin_role_name="admin-users";
    
    public UserInfo() {
        super();
    }
    
    public UserInfo(String name, String role) {
        super();
        this.userName = name;
        this.roleName = role;
    }
    
    public UserInfo(String name) {
        super();
        this.userName = name;
        this.roleName = "User";
    }
    
    private String userName;
    private String roleName;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
    
    public boolean isAdmin(){
         boolean result = false;
         String user =  CredentialUtil.getCurentUser();
         System.out.println("============------current user="+user);
         UserDAO udao = new UserDAO();
         Session session = HibernateUtil.getSession();
         User userPO = (User)udao.getByName(session, com.hdp.smp.model.User.class, user);
         String roleName = userPO.getRoleName();
         System.out.println("============------current user role="+roleName);
         session.close();
         return roleName.equalsIgnoreCase(admin_role_name);
        }


}
