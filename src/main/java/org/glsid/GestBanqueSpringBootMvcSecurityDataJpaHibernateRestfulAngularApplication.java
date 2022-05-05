package org.glsid;

import org.glsid.security.service.ISecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
//@ImportResource(locations = {"classpath:/spring-beans.xml"})
//@ImportResource("spring-beans.xml")
public class GestBanqueSpringBootMvcSecurityDataJpaHibernateRestfulAngularApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestBanqueSpringBootMvcSecurityDataJpaHibernateRestfulAngularApplication.class, args);

	}
	/*
	 * Si on ne veut pas utiliser passWordEncoder par defaut(noop),on spécidie
	 * à spring le type de cryptage qu'on souhaite.
	 * 
	 * l'annotation @Bean ici veut dire au démarrage de creer un objet de type
	 * PasswordEncoder et de le placer dans le contex et devient un Bean Spring
	 * ce qui me permettra de l'utiliser n'importe où en faisant AUTOWIRED. exemple
	 * 
	 *  à chaquefois que j'utilise un objet PasswordEncoder ,il va utiliser comme
	 *  mot de passe ou retourner le type de mot de passe return new BCryptPasswordEncoder();
	 *  
	 *  autowired
	 *  PasswordEncoder metierCrypterUnTruc
	 */
	@Bean
	PasswordEncoder passWordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	/*
	 * ayant pas encore cree IHM 
	 * pour tester CRUD appUser et AppRole que je viens d'implementer dans
	 * le package de spring secuty.
	 * 
	 * Une fois que l'application demarrer je lui preciser l'interface que je veut
	 * utiliser et ici c'est : ISecurityService et fait automatique l'injection de dependance.
	 * 
	 * decommenter le @Bean pour utiliser CommandLineRunner saveUsers
	 */
	//@Bean
	CommandLineRunner saveUsers(ISecurityService securityService) {

	    return args -> {

	    	securityService.saveNewUser("David", "1234", "1234");
	    	securityService.saveNewUser("Pasto", "1234", "1234");
	    	securityService.saveNewUser("Mono", "1234", "1234");
	    	
	    	securityService.saveNewRole("USER", "");
	    	securityService.saveNewRole("ADMIN", "");
	    	
	    	securityService.addRoleToUser("David", "ADMIN");
	    	securityService.addRoleToUser("David", "USER");
	    	securityService.addRoleToUser("Pasto", "USER");
	    	securityService.addRoleToUser("Mono", "USER");

	    };

	}

}
