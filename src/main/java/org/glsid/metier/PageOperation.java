package org.glsid.metier;

import java.io.Serializable;
import java.util.List;

import org.glsid.entities.OperationTransportDataSet;


public class PageOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<OperationTransportDataSet> listOperations;
	
	private int pageActuelle;
	
	private int nombreOperations;
	
	private Long totalOperations;
	
	private int totalPages;
	
	private String motCleCodeCompe;
	

	public List<OperationTransportDataSet> getListOperations() {
		return listOperations;
	}

	public void setListOperations(List<OperationTransportDataSet> listOperations) {
		this.listOperations = listOperations;
	}

	public int getPageActuelle() {
		return pageActuelle;
	}

	public void setPageActuelle(int pageActuelle) {
		this.pageActuelle = pageActuelle;
	}

	public void setTotalOperations(Long totalOperations) {
		this.totalOperations = totalOperations;
	}

	public int getNombreOperations() {
		return nombreOperations;
	}

	public void setNombreOperations(int nombreOperations) {
		this.nombreOperations = nombreOperations;
	}

	public Long getTotalOperations() {
		return totalOperations;
	}

	public void setTotalOperations(long l) {
		this.totalOperations = l;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) 
	{
		/*
		 * comme je veux commencer avec 0 dans IHM, je fais donc totalPages-1   
		 */
		if(totalPages>0)
		{
			this.totalPages=totalPages-1;
		}else 
		{
			this.totalPages = totalPages;
		}
		
	}

	public String getMotCleCodeCompe() {
		return motCleCodeCompe;
	}

	public void setMotCleCodeCompe(String motCleCodeCompe) {
		this.motCleCodeCompe = motCleCodeCompe;
	}

	
	

}
