package org.glsid.webMVC;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.glsid.entities.OperationActionForm;
import org.glsid.entities.OperationTransportDataSet;
import org.glsid.metier.ICompteMetier;
import org.glsid.metier.IOperationMetier;
import org.glsid.metier.PageOperation;
import org.glsid.utili.ContantesPageOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OperationCompteController {
	private final org.apache.logging.log4j.Logger log = LogManager.getLogger(OperationCompteController.class.getName());

	ICompteMetier compteMetier;
	IOperationMetier operationMetier;

	/*
	 * 2 façons de faire de l'injection de dependance :
	 * -soit en utilisant l'annotation @autowired
	 * -soit utiliser un constructeur comme en dessous
	 */
	public OperationCompteController(ICompteMetier compteMetier, IOperationMetier operationMetier) {
		this.compteMetier = compteMetier;
		this.operationMetier = operationMetier;
	}

	/*
	 * retour une page avec la liste des operation filtre avec le mot cle passe en parametre
	 * si aucun parametre utilise les parametre par default
	 */
	@GetMapping(path = ContantesPageOperation.INDEXVue)
	public String getCompte(Model model,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_PAGEACTUEL, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_PAGEACTUEL) int pageActuel,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_NBELEMENTPAGEACTUEL, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_NBELEMENTPAGEACTUEL) int nbElementPageActuel,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_MOTCLE, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL) String motCleCodeCompte) {
		PageOperation pageOperation = operationMetier.getOperations(motCleCodeCompte, pageActuel, nbElementPageActuel);

		// List<Compte> listCompte = compteMetier.listCompte();
		model.addAttribute("pageOperation", pageOperation);

		return "operationComptes";
	}

	/*
	 * Supprimer une operation en donnant son numéro d'operation en parametre
	 */
	@GetMapping(path = ContantesPageOperation.DELETEVue)
	public String delete(@RequestParam(name = ContantesPageOperation.Page_OP_NAME_NUMOP) Long numOp,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_PAGEACTUEL) int pageActuel,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_MOTCLE) String motCleCodeCompte)

	{
		// suppression d'une operation appartenant à un numOp
		operationMetier.deleteOperationCompte(numOp);

		return pageActuelleSiNBelementVide(pageActuel, motCleCodeCompte);

	}

	/*
	 * Pour editer une operation :
	 * àtravers son numOp on recupere l'operation
	 * si elle existe on retourne la page avec les informations de l'operation
	 * 
	 */
	@GetMapping(path = ContantesPageOperation.EDITEOPERATIONVue)
	public String editerOperation(Model modele,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_NUMOP) Long numOp,

			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_MOTCLE, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL) String motCleCodeCompte,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_PAGEACTUEL, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_PAGEACTUEL) int pageActuel)

	{
		OperationActionForm operationActionForm = operationMetier.findById(numOp);
		if (operationActionForm == null) {
			throw new RuntimeException("Operation introuvable avec le numOperation" + numOp);
		}
		operationActionForm.setEdit(true);
		operationActionForm.setPageActuelle(pageActuel);
		operationActionForm.setMotCleCodeCompe(motCleCodeCompte);
		modele.addAttribute("operationActionForm", operationActionForm);

		return ContantesPageOperation.OPERATION_ACTION_EDITER_FORMVue;
	}

	/*
	 * page par default
	 */
	@GetMapping("/")
	public String home() {
		return ContantesPageOperation.ACCUEILVue; 
	}
	/*
	 * au lieu de renvoyer une page ,on peut spécifier à spring MVC(@Controller) de
	 * renvoyer les données dans le corps de la requete avec @ResponseBody en le
	 * serialisant au format JSON. exemple ci dessous il renvoi les données au
	 * format JSON et la on peut travail coté client si on ne veut pas utiliser
	 * THYMELEAF et vouiloir utiliser ANGULAR par exemple
	 */

	@GetMapping(ContantesPageOperation.listeOperationVue)
	@ResponseBody
	public List<OperationTransportDataSet> getListeOperation() {
		int nbElementPageActuelle = Integer.parseInt(ContantesPageOperation.Page_OP_DEFAULTVALUE_NBELEMENTPAGEACTUEL);

		int pageActuelle = Integer.parseInt(ContantesPageOperation.Page_OP_DEFAULTVALUE_PAGEACTUEL);

		if (null != operationMetier.getOperations(
				ContantesPageOperation.Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL, pageActuelle,
				nbElementPageActuelle)) {
			return operationMetier.getOperations(ContantesPageOperation.Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL,
					pageActuelle, nbElementPageActuelle).getListOperations();
		} else {
			return null;
		}

	}

	/*
	 * methode appele pour effectuer un versement,retrait et virement considerer 
	 * comme une operation sur le compte
	 * 
	 * Retourne la page avec le formulaire de saisi selon le type d'operation : verser,retirer ou virement
	 *  et le model
	 */
	@GetMapping(path = ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORM_URL)
	public String formOperation(Model model,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_TYPEOPERATION) Character typeOperation) {
		if (typeOperation.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER)) {
			OperationActionForm OperationActionForm = new OperationActionForm(
					ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER);
			model.addAttribute("operationActionForm", OperationActionForm);

			return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;
		}if (typeOperation.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT)) 
		{
			OperationActionForm OperationActionForm = new OperationActionForm(
					ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT);
			model.addAttribute("operationActionForm", OperationActionForm);

			return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;
		}if (typeOperation.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT)) 
		{
			OperationActionForm OperationActionForm = new OperationActionForm(
					ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT);
			model.addAttribute("operationActionForm", OperationActionForm);

			return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;
		} 
		else {
			return "/";
		}

	}

	/*
	 * cette methode va enregistrer 3 action verser,retirer et virement, c'est
	 * pourquoi je vais vérifier son type pour l'orienter sur la bonne méthode
	 * 
	 * pour effectuer la validation des champs d'une entite: dejà il faut renseigner
	 * la dependance dans le POM ensuite mettre les annotation de validation sur les
	 * champs d'entité utilisé(model ou bdd) enfin le mettre @Valid sur l'object
	 * model utilise dans la vue pour verifier la validation des champs cote
	 * utilisateur exemple annotation : @Notempty,@DecimalMin,@size(min=2,max=5)
	 * 
	 * BindingResult : en cas d'erreur ou les critere de validation d'un champ n'est
	 * pas respecte stock cette erreur dans cet object.
	 * 
	 * pour resumer dependance valiidation est utilise EntiTIES : exemple annotation
	 * : @Notempty,@DecimalMin,@size(min=2,max=5)
	 * 
	 * @Controller :exemple @Valid et capture l'erreur dans l'objet BindingResult
	 * dans la vue : exemple th:error=
	 */
	@PostMapping(path = ContantesPageOperation.ENREGISTRERVue)
	public String enregistrer(@Valid @ModelAttribute("operationActionForm") OperationActionForm operationActionFormModel,
			BindingResult bindingResult, Model model,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_PAGEACTUEL, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_PAGEACTUEL) int pageActuel,
			@RequestParam(name = ContantesPageOperation.Page_OP_NAME_MOTCLE, defaultValue = ContantesPageOperation.Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL) String motCleCodeCompte)

	{
		/*
		 * on peut faire mieux et propre en utilisant les Group et @validation de spring
		 * pour gerer la validation d'un group d'un champ dans une entite
		 * 
		 * mais ici je vais utiliser une liste pour stocker les champs en erreurs et je
		 * fai le traitement dessus selon mon besoin
		 */
		List<String> champsVerserRetrait = new ArrayList<>(List.of("montantOp", "codeCompte1", "codeEmp"));

		Boolean champsErreurs = false;
		Boolean champsCodeCompte2=false;

		if (null != bindingResult) {
			log.info("\n\n\n");
			log.info("Liste des champs erronés");
			log.info("--------------------------------");
			for (ObjectError error : bindingResult.getAllErrors()) {
				String fieldErrors = ((FieldError) error).getField();
				if (champsVerserRetrait.contains(fieldErrors)) 
				{
					champsErreurs = true;
				} 
				if(fieldErrors.equals("codeCompte2")) 
				{
					champsCodeCompte2=true;
				}

				log.info(fieldErrors);

			}
		}
		/*
		 * Retourne la page avec les erreurs si les criteres de validations des champs ne sont pas respecte.
		 * 
		 * ici dans mes conditions de IF je precise le type operation  car les champs pour verser,retirer ou virement ne sont
		 * pas pareil, c'est pourquoi je verifie si les champs des erreurs corresponds bien au type d'operation puisque
		 * mon MODEL regroupe les attributs de tous mes operation(verser,retrait et virement) et generer une erreur qui ne
		 * correspond pas au type d'operation.
		 */
		if ( ((champsErreurs) 
				&& (null != operationActionFormModel)
				&& (operationActionFormModel.getType()
						.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER))) ||
				
				((champsErreurs) 
						&& (null != operationActionFormModel)
						&& (operationActionFormModel.getType()
								.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT)))||
				
				(((champsErreurs)||(champsCodeCompte2)) 
						&& (null != operationActionFormModel)
						&& (operationActionFormModel.getType()
								.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT)))
		    ) 
		
		{
			log.info("je suis entrer dans la condition enregistrer avec erreur : " + bindingResult.hasErrors());

			return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;

		} else if (!((champsErreurs)) && null != operationActionFormModel
				&& operationActionFormModel.getType()
						.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER)
				&& !operationActionFormModel.isEdit()) {
			operationMetier.verser(operationActionFormModel.getCodeCompte1(), operationActionFormModel.getMontantOp(),
					operationActionFormModel.getCodeEmp());

			return "redirect:" + ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue + "?typeOperation="
					+ ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER;

		} else if (!((champsErreurs)) && null != operationActionFormModel
				&& operationActionFormModel.getType()
						.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT)
				&& !operationActionFormModel.isEdit()) 
		{
			/*
			 * Capturer l'eereur venu de la couche metier et modifer mon bindingResult
			 * pour l'afficher l'erreur sur le champ ou attribut correspond au niveau de 
			 * l'IHM pour exemple ci-dessous c'est le montant pour le retrait ne doit pas 
			 * etre superieur au solde du compte
			 */
			try 
			{
				operationMetier.retirer(operationActionFormModel.getCodeCompte1(), operationActionFormModel.getMontantOp(),
						operationActionFormModel.getCodeEmp());

				return "redirect:" + ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue + "?typeOperation="
						+ ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT;
				
			} catch (Exception e) 
			{
				bindingResult.rejectValue("montantOp", "error.operationActionForm",e.getMessage());

				return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;

			}
			

		}else if ((!(champsErreurs)&&!(champsCodeCompte2)) && (null != operationActionFormModel)
				&& (operationActionFormModel.getType()
				.equals(ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT))
		&& (!operationActionFormModel.isEdit())) 
{
			log.info("\n\nje suis dans virement" + "compte2 est : " + operationActionFormModel.getCodeCompte2()+"\n");

	/*
	 * Capturer l'eereur venu de la couche metier et modifer mon bindingResult
	 * pour l'afficher l'erreur sur le champ ou attribut correspond au niveau de 
	 * l'IHM pour exemple ci-dessous c'est le montant pour le retrait ne doit pas 
	 * etre superieur au solde du compte
	 */
	try 
	{
		operationMetier.virement(operationActionFormModel.getCodeCompte1(),
				operationActionFormModel.getCodeCompte2(),
				operationActionFormModel.getMontantOp(),
				operationActionFormModel.getCodeEmp());

		return "redirect:" + ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue + "?typeOperation="
				+ ContantesPageOperation.Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT;
		
	} catch (Exception e) 
	{
		bindingResult.rejectValue("montantOp", "error.operationActionForm",e.getMessage());

		return ContantesPageOperation.OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue;

	}
	

}else if (!((champsErreurs)) && null != operationActionFormModel && operationActionFormModel.isEdit()) {
			log.info("je suis dans editer" + "numOp" + operationActionFormModel.getNumOp());

			operationMetier.editerOperation(operationActionFormModel);

			return pageActuelleSiNBelementVide(pageActuel, motCleCodeCompte);
		} else {
			return "/";
		}
	}

	/*
	 * si la pade actuelle ou courante n'a pas d'element dans le tableau ou getNombreOperations  apres la
	 * suppression alors retourner à la page 0 avec le mot clé donné
	 */
	private String pageActuelleSiNBelementVide(int pageActuel, String motCleCodeCompte) {
		int nbElementPageActuelle = Integer.parseInt(ContantesPageOperation.Page_OP_DEFAULTVALUE_NBELEMENTPAGEACTUEL);

		PageOperation pageOperation = operationMetier.getOperations(motCleCodeCompte, pageActuel,
				nbElementPageActuelle);

		if (pageOperation.getPageActuelle() > ContantesPageOperation._INT_ZERO_VALUE
				&& pageOperation.getNombreOperations() == ContantesPageOperation._INT_ZERO_VALUE) {
			return "redirect:" + ContantesPageOperation.INDEXVue + "?pageActuel="
					+ ContantesPageOperation.Page_OP_DEFAULTVALUE_PAGEACTUEL + "&motCle=" + motCleCodeCompte;
		} else {
			return "redirect:" + ContantesPageOperation.INDEXVue + "?pageActuel=" + pageActuel + "&motCle="
					+ motCleCodeCompte;

		}
	}
}
