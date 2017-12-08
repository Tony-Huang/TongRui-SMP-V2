package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.util.CredentialUtil;
import com.hdp.smp.model.Role;
import com.hdp.smp.model.User;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.RoleDAO;
import com.hdp.smp.persistence.UserDAO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class UserProfile {
    public UserProfile() {
        super();
    }
    
    public Logger log = Logger.getLogger(UserProfile.class);
    
    private String newPassword;
    private String confirmPassword;
    private String roleName;


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    
    public String getUsername (){
          return  CredentialUtil.getCurentUser();
        }
    
    //the role assigned to user in profile change.
    public String getUserRole(){
        return this.roleName;
        }
    public void setUserRole(String role){
        this.roleName = role;
        }
    
    
    //used by all
    public List<String> getAvailableRoles () {
        DAO rdao = new DAO();
        Session session = HibernateUtil.getSession();
        List<Role> roles = rdao.getAll(session, Role.class);
        
        List<String> roleNames = new ArrayList<String>();
        for(Role r:roles){
                roleNames.add(r.getName());
            }
        
        return roleNames;
        }


    public String changePwd() {
        if(!this.newPassword.equals(this.confirmPassword) ){
            System.out.println("Error! password not match!!!");
            MBean mb = new MBean();
            mb.showError("password mismatch", mb.getMsg("user.profile.passwd.change.msg.mismtach"));
            return null;
            }
        
        try {
        String passwd2Store =  CredentialUtil.encryptTomcatRealmPasswd(getNewPassword());
        System.out.println("******************************encrypted Password="+passwd2Store +"***************");
        //get the username from session
        String username = this.getUsername();
        System.out.println("******************************username="+username +"***************");
        
        //call the DAO to store passwd
        UserDAO udao = new UserDAO();
        Session  hbsession = HibernateUtil.getSession();
        User user = (User)udao.getByName(hbsession, User.class, username);
        
        user.setPasswd(passwd2Store);
      
        udao.saveOrUpdate(user, hbsession);
        hbsession.close();
        } catch (Exception e) {
                MBean mb = new MBean();
                mb.showError("password change fail", mb.getMsg("user.profile.passwd.change.msg.error"));
            }
        
        MBean mb = new MBean();
        mb.showInfo("password change success", mb.getMsg("user.profile.passwd.change.msg.success"));
        
        return null;
        }
    
    private void changeRole(String roleName,String userName){
            UserDAO udao = new UserDAO();
            Session  hbsession = HibernateUtil.getSession();
            User user = (User)udao.getByName(hbsession, User.class, userName);
            String existedRoleName = user.getRoleName();
            if ( !roleName.equals(existedRoleName) ){
                 user.setRoleName(roleName);
                 RoleDAO rdao = new RoleDAO();
                 Role r = (Role)rdao.getByName(hbsession, Role.class, roleName);
                 user.setRole(r);
                    udao.saveOrUpdate(user, hbsession);
                }
            hbsession.close();
            
        }
    
    
}
