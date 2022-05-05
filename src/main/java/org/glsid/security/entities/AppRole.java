package org.glsid.security.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * pourquoi le nom AppRole car Spring à déjà une class avec le nom Role et
 * donc pour éviter de confondre ,j'utilise AppRole
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppRole 
{
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long roleId;
	@Column(unique = true)
	private String roleName;
	private String description;

}
