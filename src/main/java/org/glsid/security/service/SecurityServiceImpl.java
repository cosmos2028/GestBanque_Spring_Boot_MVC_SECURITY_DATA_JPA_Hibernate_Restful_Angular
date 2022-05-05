package org.glsid.security.service;

import java.util.UUID;



import org.glsid.security.entities.AppRole;
import org.glsid.security.entities.AppUser;
import org.glsid.security.repository.IAppRoleRepository;
import org.glsid.security.repository.IAppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*
 * La couche service concernant la partie Sécurity
 */
/*
 * @Slf4j
 * pour logger les messages on peut utiliser l'annotation lombock qui
 * va nous retourner un attribut qui nous permet de logger
 * 
 * soit utiliser la methode classique avec :
 * 	private final org.apache.logging.log4j.Logger log = LogManager.getLogger(ISecurityServiceImpl.class.getName());
 * 
 * pour faire l'injection de dépendance :
 * soit avec @autowired
 * soit avec le constructeur et ici on utilise l'annotation lombock :@AllArgsConstructor
 */
@Service
@Slf4j @AllArgsConstructor
@Transactional
public class SecurityServiceImpl implements ISecurityService
{
	private IAppUserRepository appUserRepository;
	private IAppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder ; 
	

	@Override
	public AppUser saveNewUser(String userName, String passWord, String verifyPassWord) {
		/*
		 * le nom doit etre unique dans BDD
		 */
		AppUser appUser = appUserRepository.findByUserName(userName);
		
		if(null!=appUser)throw new RuntimeException("UserName : "+userName+" already exist");
		
		if(!(passWord.equals(verifyPassWord)))throw new RuntimeException("PassWord not match");
		String hashedPWD = passwordEncoder.encode(passWord);
		appUser = new AppUser();
		/*
		 * Generer un UserID.
		 * UUID : permet de gener une chaine de caractere aleatoire qui 
		 * depend de la date systeme(dateTime) et sera unique .
		 * donc pour gener les ID sous forme de String on utilise UUID.
		 * 
		 * UUID se base de la date systeme pour gener un identifiant unique
		 */
		appUser.setUserId(UUID.randomUUID().toString());
		appUser.setUserName(userName);
		appUser.setPassWord(hashedPWD);
		appUser.setActive(true);
		AppUser saveAppUser = appUserRepository.save(appUser);
		
		 log.info("\n Save user succesful with userName"+userName+"with userID genered by UUID :"+appUser.getUserId());
		return saveAppUser;
	}

	@Override
	public AppRole saveNewRole(String roleName, String description) 
	{
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		
		if(null!=appRole)throw new RuntimeException("RoleName : "+roleName+" already exist");
		
		appRole = new AppRole();
		appRole.setRoleName(roleName);
		appRole.setDescription(description);
		
		AppRole saveAppRole = appRoleRepository.save(appRole);
		 log.info("\n Save Role succesful with RoleName"+roleName);

		return saveAppRole;
	}

	@Override
	public void addRoleToUser(String userName, String roleName) 
	{
	
		AppUser appUser = appUserRepository.findByUserName(userName);
		if(null==appUser)throw new RuntimeException("userName : "+userName+" not found");
		log.info("\n recuperation de l'object user avec  userName"+userName+"object :"+appUser.toString() );

		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		if(null==appRole)throw new RuntimeException("RoleName : "+roleName+" not found");

		log.info("\n recuperation de l'object role avec  roleName"+roleName+"object :"+appRole.toString() );

		/*
		 * ce n'est plus nessaire d'utiliser appRoleRepository.save(appRole)
		 * car il sera ajouter automatiquement dans la table d'association à condition 
		 * d'ajouter l'annotion @Transactionnal de Spring pour faire la mise à
		 * jour de l'object en faisant u commit(ainsi toutes les modification 
		 * faites dans appUser et appRole JPA/hibernate fait un update ) s'il n' y a pas erreur ou un rollBack en 
		 * cas d'erreur.
		 */
		appUser.getAppRoles().add(appRole);
		 log.info("\n Save Role succesful with RoleName"+roleName);


		
	}

	@Override
	public void removeRoleFromUser(String userName, String roleName) 
	{
		AppUser appUser = appUserRepository.findByUserName(userName);
		if(null==appUser)throw new RuntimeException("userName : "+userName+" not found");
		log.info("\n recuperation de l'object user avec  userName"+userName+"object :"+appUser.toString() );

		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		if(null==appRole)throw new RuntimeException("RoleName : "+roleName+" not found");

		log.info("\n recuperation de l'object role avec  roleName"+roleName+"object :"+appRole.toString() );

		/*
		 * ce n'est plus nessaire d'utiliser appRoleRepository.save(appRole)
		 * car il sera ajouter automatiquement dans la table d'association à condition 
		 * d'ajouter l'annotion @Transactionnal de Spring pour faire la mise à
		 * jour de l'object en faisant u commit(ainsi toutes les modification 
		 * faites dans appUser et appRole JPA/hibernate fait un update ) s'il n' y a pas erreur ou un rollBack en 
		 * cas d'erreur.
		 */
		appUser.getAppRoles().remove(appRole);
		 log.info("\n Remove Role succesful with RoleName"+roleName+" from user :"+userName);
		
	}

	@Override
	public AppUser loadUserByUserName(String userName) 
	{
		AppUser appUser = appUserRepository.findByUserName(userName);
		if(null==appUser)throw new RuntimeException("userName : "+userName+" not found");
		log.info("\n recuperation de l'object user avec  userName"+userName+"object :"+appUser.toString() );

		return appUser;
	}

	
}
