/**
 * Copyrights are reserved.
 * 2015
 * 
 * @Author hdp214@163.com
 * @since 1.0
 */

package com.hdp.smp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "USERS" )
public class User implements SMPEntity {

	private int id;
	private String name;
	private String passwd;
	private String email;
	private Role role;
	
	private String roleName ;
	
	
	@Id 
	@GeneratedValue
	@GenericGenerator(name="increment", strategy = "increment")
	@Column
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column ( unique=true, length=30,nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column ( length=60,nullable=false)
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	@Transient
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@ManyToOne @JoinColumn(name="roleId", updatable=true,nullable=false)
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
	@Column
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String toString() {
		return "user: id="+id +" name="+name +"  rolename="+roleName;
	}
	
	
    @Override
    @Transient
    public Long getEntityId() {
        return (long)this.getId();
    }
	
	
}
