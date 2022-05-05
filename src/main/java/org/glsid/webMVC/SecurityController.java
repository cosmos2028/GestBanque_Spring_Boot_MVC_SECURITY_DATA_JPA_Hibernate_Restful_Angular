package org.glsid.webMVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
 * Toujours creer un controleur pour gener uniquement les 
 * pages Spring Security par exemple les pages erreurs etc.
 */
@Controller
public class SecurityController 
{
	/*
	 * lorsqu'un utilisateur essaye d'acceder à une ressource qui n'a pas le 
	 * droit ,Spring retourne cette page ,appele ici 403
	 */
	@GetMapping("/403")
	public String pasAutorie() 
	{
		return "403";
	}

}
