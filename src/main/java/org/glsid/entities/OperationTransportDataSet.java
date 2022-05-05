package org.glsid.entities;

import java.io.Serializable;
import java.util.Date;


public class OperationTransportDataSet implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long numOp;
	
	private double montantOp;
	
	private Date dateOp;
	
	private String  codeCompte;
	
	private String nomEmploye;
	
	private String typeOperation;
	
	public OperationTransportDataSet() {
	}

	public OperationTransportDataSet(Long numOp, double montantOp, Date dateOp, String codeCompte, String nomEmploye,
			String typeOperation) {
		super();
		this.numOp = numOp;
		this.montantOp = montantOp;
		this.dateOp = dateOp;
		this.codeCompte = codeCompte;
		this.nomEmploye = nomEmploye;
		this.typeOperation = typeOperation;
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

	public String getCodeCompte() {
		return codeCompte;
	}

	public void setCodeCompte(String codeCompte) {
		this.codeCompte = codeCompte;
	}

	public String getNomEmploye() {
		return nomEmploye;
	}

	public void setNomEmploye(String nomEmploye) {
		this.nomEmploye = nomEmploye;
	}

	public String getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}
	
	

}
