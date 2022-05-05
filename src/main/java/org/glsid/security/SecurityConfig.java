package org.glsid.security;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.glsid.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*@Configuration
 * toute class qui utilise @Configuration,spring va l'instancier en premier
 * lieu lors du démarrage de l'application.
 * Généralement utilisé pour faire de la config
 */
/*@EnableWebSecurity
 * c'est signaler à Spring que on veut activer la sécuté Web
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	private final org.apache.logging.log4j.Logger log = LogManager.getLogger(SecurityConfig.class.getName());

	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * cette méthode indique à Spring Security comment il va chercher les 
	 * utilisateurs et les roles.
	 * est-ce que on veut que Spring security utilisé :
	 * Soit Une BDD pour aller chercher ses utilisateur 
	 * Soit les utilisateurs en mémoire (InMemory)
	 * Soit utiliser un Annuaraire de l'entreprise par exemple :
	 * LDAP(qui gere deja les utilisateur et les roles d'acces pour les 
	 * application d'entreprise)
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder  auth) throws Exception 
	{
		/*
		 * auth.inMemoryAuthentication():les utilisateurs qui ont le droit 
		 * d'acceder à l'application sont stockés en mémoire et il faut les
		 * preciser avec withUser("user1").password("1234").roles("USER").
		 * 
		 * pour ajouter les utilisateur ,2 façons : 
		 * soit pour ajouter mettre un and() 
		 * soit l'autre face avec l'objet auth.inMemoryAuthentication()
		
		auth.inMemoryAuthentication()
		.withUser("user1").password("1234").roles("USER")
		.and()
		.withUser("admin").password("1234").roles("USER","ADMIN");
		* 
		* pour spécifier à spring security ne pas encoder ou crypter 
		* les mots de passse par defaut avec passWordEncoded
		* utilise ceci devant la chaine {noop}
		 */
		//crypter le mot de passe avec BCryptPasswordEncoder
		 
		
		//String  pwhashedPWD=passwordEncoder.encode("1234");
		
		/*
		 *1e façon d'authentification : inMemoryAuthentication
		String encodePWD= passwordEncoder.encode("1234");
		
		log.info("\n\n\n le mot de passe crypté 1234 est : "+encodePWD+"\n\n\n");
		
		auth.inMemoryAuthentication()
		.withUser("user1").password(encodePWD).roles("USER");
		
		auth.inMemoryAuthentication()
		.withUser("user2").password("{noop}1234").roles("USER");
		auth.inMemoryAuthentication()
		.withUser("admin").password(encodePWD).roles("USER","ADMIN");
		*/
		// 2e façon d'authentification : BDD
		/*
		 * preciser le dataSource du context c ad application.properties
		 * renommer les colonnes userName et passWord en pricipal et credentials
		 * pour la comprehension de spring security puisque pour lui userName et
		 * passWord c'est pricipal et credentials.
		 * 
		 * Ensuite charger les roles avec la 2ieme requete authoritiesByUsernameQuery
		 * 
		 * Comme apres celà Spring Security gene une session donc pour avoir
		 * dans la session Role_xxx ou User_xxx, il faut prefix avec  rolePrefix
		 * 
		 * Enfin préciser le moteur d'encodage , ici on utilise BCryptPasswordEncoder.
		 *  NB : le mode de cryptage est irreversible c'est-à-dire il est impossible
		 *  de trouver ou de revenir à partir du passWord crypté. 
		 *  
		 *  Car Spring Security crypte le mot de passe de l'utilisateur et le compare 
		 *  avec celui de la BDD déjà crypté.  
		
		
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select userName as principal,"
				+ "passWord as credentials, active from users"
				+ " where userName=?")
		.authoritiesByUsernameQuery("select userName as principal,"
				+ "role as role from users_roles where userName=?")
		.rolePrefix("ROLE_")
		.passwordEncoder(passwordEncoder);
		 
		*/
		/*3e façon d'authentification : userDetailsService
		 * ici l'opération d'authentification c'est la couche service
		 * qui s'en occupe(notre application qui va gerer les authentification,role,etc).
		 * car avant c'était Spring Security qui utilisait s'est mécanisme pour
		 * se connecter à BDD pour l'opération d'authentification.
		 * 
		 * ICI je dis à pring : quand l'utilisateur va saisir son userName,il va 
		 * faire appel à cet object userDetailsServiceImpl reçu par injection @autowired
		 */
		auth.userDetailsService(userDetailsServiceImpl);
		
	}
	/*
	 * la deuxime methode configure ici c'est pour Specifier les 
	 * droits d'accès 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		/*
		 * On precise à spring security qu'on veut utliser un formulaire
		 * d'authentification sinon il va utiliser un formulaire par default.
		 * rajouter loginPage("/pageLogin") cela implique d'aller rajouter
		 * le chemin dans le Controller et creer la pageLogin dans le moteur
		 * template Thymleaf
		 */
		http.formLogin();
		/*
		 * permitAll() :veut dire que ça ne necessite pas une authentification
		 * pour la page ou url concerne.
		 * 
		 * je rajoute /webjars/** car spring security le bloc par default. sinon
		 * URL AVEC / spring security bloc les ressources CSS etc
		 */
		http.authorizeRequests().antMatchers("/").permitAll();
		/*
		 * Pour resourdre une faille de sécurité qui permet a un utilisateur
		 * ne possedant pas le droit d'acces sur une ressource ou action de 
		 * modifier l'url pour avoi acces. par exemple un utilsateur qui
		 * n'a pas le droit de supprimer,peut modifeir l'URL et le faire en supprime
		 * pour eviter cela on fait : 
		 * http.authorizeRequests().antMatchers("/delete/**").hasRole("ADMIN")
		 * on precise à spring securite que toutes les requetes qui contiennent
		 *  /delete/** ou /edit/**
		 * doit etre admin pour effectuer cette operation
		 * 
		 * il faut organiser les URLs selon les roles.
		 * 
		
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
		http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
		 */
		
		/*
		 * comme j'utilise userDetailsServiceImp alors au lieu d'utiliser
		 * .hasRole("ADMIN") ,il faut plutot faire
		 */
		http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");
		http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER");
		/*
		 * autoriser l'acces aux ressources static notamment ici le css de Bootstrap de Webjars
		 */
		http.authorizeRequests().antMatchers("/webjars/**").permitAll();
		/*
		 * la gestion des droits d'acces :
		 * l'authentification la plus restrictive si on utilise :
		 * http.authorizeRequests().anyRequest().authenticated() (toutes les
		 * requetes HTTP necissite une authentification)
		 * 
		 * tout le reste nécessite une authentification
		 * 
		 */
		http.authorizeRequests().anyRequest().authenticated();
		/*
		 * configuration des exception.
		 * pour eviter d'afficher au client la page ou l'erreur par default 
		 * lorsque un utilisation n'a pas le droit d'acces sur une ressource
		 * par exemple FORBIDDEN 403. on doit personaliser l'erreur.
		 */
		http.exceptionHandling().accessDeniedPage("/403");
		
	}
	
	
}
