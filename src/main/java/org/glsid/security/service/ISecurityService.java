package org.glsid.security.service;

import org.glsid.security.entities.AppRole;
import org.glsid.security.entities.AppUser;

public interface ISecurityService 
{
	AppUser saveNewUser(String userName,String passWord,String verifyPassWord);
	AppRole saveNewRole(String roleName,String description);
	void addRoleToUser(String userName,String roleName);
	void removeRoleFromUser(String userName,String roleName);
	AppUser loadUserByUserName(String userName);

}
