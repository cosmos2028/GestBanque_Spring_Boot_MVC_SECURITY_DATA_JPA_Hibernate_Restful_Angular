package org.glsid.utili;

public interface ContantesPageOperation 
{
			String Page_OP_DEFAULTVALUE_PAGEACTUEL = "0";
			String Page_OP_DEFAULTVALUE_NBELEMENTPAGEACTUEL = "5";
			String Page_OP_DEFAULTVALUE_MOTCLECODECOMPTEPAGEACTUEL = "";
			Character Page_OP_DEFAULTVALUE_TYPEOPERATION_VERSER = 'V';
			Character Page_OP_DEFAULTVALUE_TYPEOPERATION_RETRAIT = 'R';
			Character Page_OP_DEFAULTVALUE_TYPEOPERATION_VIREMENT = 'T';


			
			String Page_OP_NAME_PAGEACTUEL = "pageActuel";
			String Page_OP_NAME_NBELEMENTPAGEACTUEL = "nbElementPageActuel";
			String Page_OP_NAME_MOTCLE = "motCle";
			String Page_OP_NAME_NUMOP = "numOp";
			String Page_OP_NAME_TYPEOPERATION = "typeOperation";

			
			String listeOperationVue="/user/listeOperation";
			String INDEXVue="/user/index";
			String ACCUEILVue="accueil";
			String DELETEVue="/admin/deleteOperation";
			String EDITEOPERATIONVue="/admin/editerOperation";
			String OPERATION_ACTION_EDITER_FORMVue="operationActionEditerForm";
			String OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORMVue="operationActionVerserRetraitVirementForm";
			String OPERATION_ACTION_VERSER_RETRAIT_VIREMENT_FORM_URL="/admin/operationActionVerserRetraitVirementForm";

			String ENREGISTRERVue="/admin/enregistrer";


			

			
			int  _INT_ZERO_VALUE = Integer.parseInt(ConstantesConfigurations.getInstance().getProperty(Properties._INT_ZERO_VALUE)); 



	

}
