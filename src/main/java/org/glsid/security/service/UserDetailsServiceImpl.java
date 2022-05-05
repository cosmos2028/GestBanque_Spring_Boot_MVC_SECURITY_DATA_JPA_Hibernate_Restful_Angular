package org.glsid.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.glsid.security.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private ISecurityService securityService;
	/*
	 * ICI injection des dependance via le constructeur 
	 * je pouvais aussi utiliser les annotations de lombok.
	 */
	public UserDetailsServiceImpl(ISecurityService securityService) {
		this.securityService = securityService;
	}
	/*
	 * Ici je précise à Spring ecurity lorsque l'utilisateur va saissir
	 * son userName et PassWord, il doit faire appel à la methode loadUserByUsername.
	 * 
	 * et maintenant c'est la methode loadUserByUsername de faire le traitement en 
	 * allant dans la BDD pour retrouver l'utilisateur.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		AppUser appUser = securityService.loadUserByUserName(username);
		/*
		 * Comme on doit retourner un object UserDetails à Spring Security,
		 * on va donc transformer appUser en UserDetails.
		 * 
		 * pour cela on va creer un object de type User de Spring Security.
		 * et ce User reçoit en parametre : userName,PassWord et une collection
		 * de role.
		 * 
		 * les roles doivent etre dans une Collection<GrantedAuthority>
		 * ensuite faire le mapping de prendre le role de la BDD et de stocker
		 * dans l'objet GrantedAuthority et cela pour chaque role.
		 * 
		 * mon appUser.getAppRoles().forEach veut dire pour chaque role on cree 
		 * cree un objet SimpleGrantedAuthority avec pour parametre role.getRoleName().
		 * 
		 * car pour Spring Secuty le role s'appel GrantedAuthority.
		 
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		appUser.getAppRoles().forEach(role->{
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
			authorities.add(authority);
		});
		*/
		/*
		 * autre façon en utilisant STREAM
		 */
		Collection<GrantedAuthority> authorities2 = appUser.getAppRoles()
				.stream()
				.map(role-> new SimpleGrantedAuthority(role.getRoleName()))
				.collect(Collectors.toList());
		
		User userTransformedSpringSecurity = new User(appUser.getUserName(),appUser.getPassWord(),authorities2);
		return userTransformedSpringSecurity;
	}

}
