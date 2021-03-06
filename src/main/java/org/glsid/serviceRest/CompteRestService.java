package org.glsid.serviceRest;

import java.util.List;

import org.glsid.entities.Compte;
import org.glsid.metier.ICompteMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompteRestService 
{
	@Autowired
	private ICompteMetier compteMetier;

	@RequestMapping(value = "/comptes",method = RequestMethod.POST)
	public Compte saveCompte(@RequestBody Compte c) {
		return compteMetier.saveCompte(c);
	}

	@RequestMapping(value = "/comptes/{IdCompte}",method = RequestMethod.GET)
	public Compte getCompte(@PathVariable String IdCompte) {
		return compteMetier.getCompte(IdCompte);
	}

	@RequestMapping(value = "/comptes",method = RequestMethod.GET)
	public List<Compte> listCompte() {
		return compteMetier.listCompte();
	}
	

}
