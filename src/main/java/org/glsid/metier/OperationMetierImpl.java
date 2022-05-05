package org.glsid.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.glsid.dao.ICompteRepository;
import org.glsid.dao.IEmployeRepository;
import org.glsid.dao.IOperationRepository;
import org.glsid.entities.Compte;
import org.glsid.entities.Employe;
import org.glsid.entities.Operation;
import org.glsid.entities.OperationActionForm;
import org.glsid.entities.OperationTransportDataSet;
import org.glsid.entities.Retrait;
import org.glsid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OperationMetierImpl implements IOperationMetier 
{
	private final org.apache.logging.log4j.Logger log = LogManager.getLogger(OperationMetierImpl.class.getName());


	@Autowired
	private IOperationRepository operationRepository;
	@Autowired
	private ICompteRepository compteRepository;
	@Autowired
	private IEmployeRepository employeRepository;
	
	@Transactional
	@Override
	public boolean verser(String codeCompte, double montant, Long codeEmp) 
	{

		if(null!=codeCompte && null!=codeEmp)
		{
			Optional<Compte>  optionalCompte = compteRepository.findById(codeCompte);
			Optional<Employe> optionEmp = employeRepository.findById(codeEmp);
		
		if(optionalCompte.isPresent()&&	 optionEmp.isPresent())
		{
			Compte compte = optionalCompte.get();
			Employe employe = optionEmp.get();
			Operation operation = new Versement();
			compte.setSolde(compte.getSolde()+ montant);
			
			operation.setCompte(compte);
			operation.setDateOp(new Date());
			operation.setEmploye(employe);
			operation.setMontantOp(montant);
			log.info("\n\n\n");
			log.info("---------------------"+operation.getClass().getSimpleName());
			log.info("\n\n\n");
			operationRepository.save(operation);
			
			log.info("\n\n\n");
			log.info("Operation du versement est un success");
			log.info("--------------------------------");
			return true;
		}else
		{
			log.info("\n\n\n");
			log.error("objet compte ou employe  n'existe pas avec codeCompte ou CodeEmp : "+codeCompte+ " ou "+codeEmp);
			 throw new NullPointerException("\n\"objet compte ou employe  n'existe pas avec codeCompte ou CodeEmp : \"+codeCompte+ \" ou \"+codeEmp");
		}
	}else
	{
		throw new RuntimeException("=codeCompte est : "+codeCompte+"codeEmp est : "+codeEmp + "montantOp est : "+montant);
	}
	}

	@Transactional
	@Override
	public boolean retirer(String codeCompte, double montant, Long codeEmp) 
	{
		if(null!=codeCompte && null!=codeEmp)
		{
		Optional<Compte> optionalCompte = compteRepository.findById(codeCompte);
		Optional<Employe> optionEmp = employeRepository.findById(codeEmp);
		
		if(optionalCompte.isPresent()&&optionEmp.isPresent())
		{
			Compte compte = optionalCompte.get();
			Employe employe = optionEmp.get();
			Operation operation = new Retrait();
			
			if(compte.getSolde()<montant) throw new RuntimeException("Solde insuffissant ");
			
			compte.setSolde(compte.getSolde()-montant);
			
			operation.setCompte(compte);
			operation.setDateOp(new Date());
			operation.setEmploye(employe);
			operation.setMontantOp(montant);
			
			operationRepository.save(operation);
			
			log.info("\n\n\n");
			log.info("Operation du Retrait est un success");
			log.info("--------------------------------");
			return true;
		}else
		{
			log.info("\n\n\n");
			log.error("objet compte ou employe  n'existe pas avec codeCompte ou CodeEmp : "+codeCompte+ " ou "+codeEmp);
			 throw new NullPointerException("\n\"objet compte ou employe  n'existe pas avec codeCompte ou CodeEmp : \"+codeCompte+ \" ou \"+codeEmp");
		}
	}else
	{
		throw new RuntimeException("=codeCompte est : "+codeCompte+"codeEmp est : "+codeEmp + "montantOp est : "+montant);
	}

	}

	@Transactional
	@Override
	public boolean virement( String CodeCpte1, String CodeCpte2,double montant, Long codeEmp)
	{
		if(null!=CodeCpte1 && null!=CodeCpte2 && null!=codeEmp && 0< montant)
		{
		 retirer(CodeCpte1,montant,codeEmp);
		 verser(CodeCpte2,montant,codeEmp);
		 log.info("\n\n\n");
		 log.info("Virement effectué avec success");
		return true;
		
		}else
		{
			throw new RuntimeException("=codeCompte est : "+CodeCpte1+" codeCompte2 est : "+CodeCpte2+"codeEmp est : "+codeEmp + "montantOp est : "+montant);

		}
	}

	@Override
	public PageOperation getOperations(String codeCompte, int pageActuelle, int nombreOperations) 
	{
        Pageable page = PageRequest.of(pageActuelle, nombreOperations, Sort.by("dateOp").descending());

	  Page<Operation>	_dataPageOperations1 = operationRepository.findByOperationCodeCompte(codeCompte,page);
	  
	  PageOperation _VpageOperation = new PageOperation();
	  List<OperationTransportDataSet> listeOperationTransportDataSet = new ArrayList<OperationTransportDataSet>();
	  log.info("\n\n\n");
	  log.info("liste des operation recupere");

	  for(Operation op :  _dataPageOperations1.getContent())
	  {
		  listeOperationTransportDataSet.add
		  (new OperationTransportDataSet(op.getNumOp(),op.getMontantOp(),op.getDateOp(),op.getCompte().getIdCompte(),op.getEmploye().getNomEmp(),op.getClass().getSimpleName()));
		  log.info("------type d'operation------");
		  log.info("------"+op.getClass().getSimpleName()+"------");
		  log.info("------code compte------");
		  log.info("------"+op.getCompte().getIdCompte()+"------");
	  }
	  log.info("\n\n\n");
	  
	  _VpageOperation.setListOperations(listeOperationTransportDataSet);
	  _VpageOperation.setNombreOperations(_dataPageOperations1.getNumberOfElements());
	  _VpageOperation.setTotalOperations(_dataPageOperations1.getTotalElements());
	  _VpageOperation.setPageActuelle(_dataPageOperations1.getNumber());
	  _VpageOperation.setTotalPages(_dataPageOperations1.getTotalPages());
	  _VpageOperation.setMotCleCodeCompe(codeCompte);
		return _VpageOperation;
		
		
		/*
		 * utilisation de l'autre methode
		 * 
		 * qui n'est pas une bonne methode car on a fait une requete a la bdd pour
		 * obtenir l'objet Compte
		 * et le resultat est redonné à une autre requete findByCompte qui va encore faire une 
		 * dans la BDD (ce n'est pas une bonne pratique surtout en terme de performance)		
		 
		
		Optional<Compte> optionalComptePage = compteRepository.findById(codeCompte);

		if(optionalComptePage.isPresent())
		{
			Compte compte = optionalComptePage.get();
		
			  Page<Operation>	pageOperations2 = operationRepository.findByCompte(compte,page);

			log.info("\n\n\n");
			log.info("Compte trouve avec  success");
			log.info("--------------------------------");
		}else
		{
			log.info("\n\n\n");
			log.error("objet compte   n'existe pas avec codeCompte : "+codeCompte+ " ou ");
			 throw new NullPointerException("\n\"objet compte n'existe pas avec codeCompte : \"+codeCompte+ \"");
		}
		*/

		
	}

	@Override
	public void deleteOperationCompte(Long numOp) 
	{
		 log.info("------numéro operation------");
		 log.info("------+numOp+------");
		operationRepository.deleteById(numOp);
		 log.info("------suppression effectué------");

	}

	@Override
	public OperationActionForm findById(Long numOp) 
	{
		/*
		 * retourne un objet operation s'il trouve sinon nul
		 */
		log.info("------------------------Couche métier-OperationActionForm findById ------------------");
		Operation operation = operationRepository.findById(numOp).orElse(null);
		if(operation==null)
    	{
    		throw new RuntimeException("Operation introuvable avec le numOperation"+numOp);
    	}
		OperationActionForm operationActionForm = new OperationActionForm();
		
		operationActionForm.setCodeCompte1(operation.getCompte().getIdCompte());
		operationActionForm.setCodeEmp(operation.getEmploye().getCodeEmp());
		operationActionForm.setMontantOp(operation.getMontantOp());
		operationActionForm.setNumOp(operation.getNumOp());
		log.info("rechercher le type"+operation.getClass().getSimpleName().charAt(0));
		
		operationActionForm.setType(operation.getClass().getSimpleName().charAt(0));
;    	return operationActionForm;
	}

	@Override
	public OperationActionForm editerOperation(OperationActionForm operationActionForm) 
	{
		log.info("------------------------Couche métier-OperationActionForm editerOperation ------------------");

		Operation operation = null;
		if(operationActionForm==null )
		{
			throw new RuntimeException("operationActionForm n'existe pas le codeCompte ou employe"+operationActionForm);
		}
		if(operationActionForm.getType()=='R')
		{
			 operation = new Retrait();
		}else if(operationActionForm.getType()=='V')
		{
			 operation = new Versement();
		}
		
		Compte cpt1 = compteRepository.getById(operationActionForm.getCodeCompte1());
		Employe emp = employeRepository.getById(operationActionForm.getCodeEmp());
		if(cpt1==null && emp==null)
		{
			throw new RuntimeException("le compte n'existe pas le codeCompte ou employe"+operationActionForm.getCodeCompte1()+" ou code emp"+operationActionForm.getCodeEmp());
		}
		
		operation.setCompte(cpt1);
		operation.setDateOp(new Date());
		operation.setEmploye(emp);
		operation.setMontantOp(operationActionForm.getMontantOp());
		operation.setNumOp(operationActionForm.getNumOp());
		operationRepository.save(operation);
		
		return operationActionForm;
	}
	
	
	

}
