package org.glsid.entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
public class Retrait extends Operation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
