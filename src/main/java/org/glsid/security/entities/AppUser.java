package org.glsid.security.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * pourquoi le nom AppUser car Spring à déjà une class avec le nom User et
 * donc pour éviter de confondre ,j'utilise AppUser
 * 
 * ici je vais utiliser les annotation de API lombock :
 * @Data pour generer les getters et setters.
 * @NoArgsConstructor : generer un constructeur sans argument
 * @AllArgsConstructor : generer un constructeur avec argument
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppUser 
{
	@Id
	private String userId;
	@Column(unique = true)
	private String userName;
	private String passWord;
	private boolean active;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role")
	private List<AppRole> appRoles = new ArrayList<AppRole>();
	

}
