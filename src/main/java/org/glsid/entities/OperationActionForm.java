package org.glsid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class OperationActionForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long numOp;
	
	@NotNull(message = "Montant ne doit pas etre vide et inferieurs à 5")
	@Min(value = 5)
	private double montantOp;
	
	private Date dateOp;
	
	@NotEmpty
	private String codeCompte1;
	
	@NotEmpty
	private String codeCompte2;
	
	@NotNull(message = "codeEmp ne doit pas etre vide.")
	private Long codeEmp;
	
	private Character type;
	private boolean edit;
	private int pageActuelle;
	private String motCleCodeCompe;
	
	
	
	
	

	
	public int getPageActuelle() {
		return pageActuelle;
	}

	public void setPageActuelle(int pageActuelle) {
		this.pageActuelle = pageActuelle;
	}

	public String getMotCleCodeCompe() {
		return motCleCodeCompe;
	}

	public void setMotCleCodeCompe(String motCleCodeCompe) {
		this.motCleCodeCompe = motCleCodeCompe;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public OperationActionForm() 
	{
		super();
	}
	
	public OperationActionForm(Character type) 
	{
		this.type = type;
	}
	
	
	public Character getType() {
		return type;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public Long getNumOp() {
		return numOp;
	}

	public void setNumOp(Long numOp) {
		this.numOp = numOp;
	}

	public double getMontantOp() {
		return montantOp;
	}

	public void setMontantOp(double montantOp) {
		this.montantOp = montantOp;
	}

	public Date getDateOp() {
		return dateOp;
	}

	public void setDateOp(Date dateOp) {
		this.dateOp = dateOp;
	}

	public String getCodeCompte1() {
		return codeCompte1;
	}

	public void setCodeCompte1(String codeCompte1) {
		this.codeCompte1 = codeCompte1;
	}

	public String getCodeCompte2() {
		return codeCompte2;
	}

	public void setCodeCompte2(String codeCompte2) {
		this.codeCompte2 = codeCompte2;
	}

	public Long getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(Long codeEmp) {
		this.codeEmp = codeEmp;
	}
	
	

	
	

}
