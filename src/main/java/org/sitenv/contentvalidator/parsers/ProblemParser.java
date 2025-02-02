package org.sitenv.contentvalidator.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sitenv.contentvalidator.model.CCDAProblem;
import org.sitenv.contentvalidator.model.CCDAProblemConcern;
import org.sitenv.contentvalidator.model.CCDAProblemObs;
import org.sitenv.contentvalidator.model.CCDARefModel;
import org.sitenv.contentvalidator.model.DiagnosisActDate;
import org.sitenv.contentvalidator.model.GoalObservation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;


public class ProblemParser {
	
	private static Logger log = LoggerFactory.getLogger(ProblemParser.class.getName());
	
	public static void parse(Document doc, CCDARefModel model, boolean curesUpdate, boolean svap2022)
			throws XPathExpressionException {
    	log.info(" *** Parsing Problems *** ");
    	model.setProblem(retrieveProblemDetails(doc));	
    	if( model.getProblem() != null)
    	{
    		model.getProblem().setPastIllnessProblems(readPastIllnessProblems(doc));
    	}
	}
    
    public static ArrayList<CCDAProblemObs> readPastIllnessProblems(Document doc) throws XPathExpressionException
	{
    	ArrayList<CCDAProblemObs> probs = new ArrayList<CCDAProblemObs>();
    	
    	Element sectionElement = (Element) CCDAConstants.PAST_ILLNESS_EXP.evaluate(doc, XPathConstants.NODE);
		
		if(sectionElement != null)
		{
			log.info(" Found Past Illness Section ");
			
			probs = readProblemObservation((NodeList) CCDAConstants.PAST_ILLNESS_PROBLEM_OBS_EXPRESSION.
					evaluate(sectionElement, XPathConstants.NODESET));
		}
    	
    	return probs;
	}
	
	public static CCDAProblem retrieveProblemDetails(Document doc) throws XPathExpressionException
	{
		CCDAProblem problems = null;
		Element sectionElement = (Element) CCDAConstants.PROBLEM_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		
		if(sectionElement != null)
		{
			log.info(" Found Problem Section ");
			problems = new CCDAProblem();
			problems.setSectionTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
											evaluate(sectionElement, XPathConstants.NODESET)));
			
			problems.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			
			problems.setProblemConcerns(readProblemConcern((NodeList) CCDAConstants.REL_ACT_ENTRY_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
			
			problems.setAuthor(ParserUtilities.readAuthor((Element) CCDAConstants.REL_AUTHOR_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
		}
		return problems;
	}
	
	public static ArrayList<CCDAProblemConcern> readProblemConcern(NodeList problemConcernNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAProblemConcern> problemConcernList = new ArrayList<>();
		CCDAProblemConcern problemConcern;
		for (int i = 0; i < problemConcernNodeList.getLength(); i++) {
			
			log.info("Adding Problem Concern ");
			problemConcern = new CCDAProblemConcern();
			Element problemConcernElement = (Element) problemConcernNodeList.item(i);
			
			problemConcern.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
										evaluate(problemConcernElement, XPathConstants.NODESET)));
			
			problemConcern.setConcernCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setStatusCode(ParserUtilities.readCode((Element) CCDAConstants.REL_STATUS_CODE_EXP.
					evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
								evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setProblems(readProblemObservation((NodeList) CCDAConstants.REL_PROBLEM_OBS_EXPRESSION.
					evaluate(problemConcernElement, XPathConstants.NODESET)));
			
			problemConcern.setAuthor(ParserUtilities.readAuthor((Element) CCDAConstants.REL_AUTHOR_EXP.
					evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcernList.add(problemConcern);
		}
		return problemConcernList;
	}
	
	public static ArrayList<CCDAProblemObs> readProblemObservation(NodeList problemObservationNodeList) throws XPathExpressionException
	{
		
		ArrayList<CCDAProblemObs> problemObservationList = null;
		if(problemObservationNodeList.getLength() > 0)
		{
			problemObservationList = new ArrayList<>();
		}
		CCDAProblemObs problemObservation;
		for (int i = 0; i < problemObservationNodeList.getLength(); i++) {
			
			log.info(" Adding Problem Observation as part of encounter ");
			problemObservation = new CCDAProblemObs();
			
			Element problemObservationElement = (Element) problemObservationNodeList.item(i);
			problemObservation.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(problemObservationElement, XPathConstants.NODESET)));
			
			problemObservation.setProblemType(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
									evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setTranslationProblemType(ParserUtilities.readCodeList((NodeList) CCDAConstants.REL_CODE_TRANS_EXP.
									evaluate(problemObservationElement, XPathConstants.NODESET)));
			
			problemObservation.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
										evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setProblemCode(ParserUtilities.readCode((Element) CCDAConstants.REL_VAL_EXP.
					evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setAuthor(ParserUtilities.readAuthor((Element) CCDAConstants.REL_AUTHOR_EXP.
					evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setAssessmentScaleObservations(ParserUtilities.retrieveAssessmentScaleObservations((NodeList) CCDAConstants.REL_ASSESSMENT_SCALE_OBS_EXP.
					evaluate(problemObservationElement, XPathConstants.NODESET)));
			
			Element ddActElement = (Element)(CCDAConstants.REL_DIAGNOSIS_DATE_ACT_EXP.
				evaluate(problemObservationElement, XPathConstants.NODE));
			problemObservation.setDateOfDiagnosis(retrieveDiagnosisDateAct(ddActElement));
			
			problemObservationList.add(problemObservation);
		}
		
		return problemObservationList;
		 
	}
	

	public static DiagnosisActDate retrieveDiagnosisDateAct(Element ddActElement) throws XPathExpressionException
	{
		DiagnosisActDate ddAct = null;
		if(ddActElement != null) {
			ddAct = new DiagnosisActDate();
		
	        log.info("Adding Diagnosis Act Date  ");
			
	        ddAct.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
										evaluate(ddActElement, XPathConstants.NODESET)));
			
	        ddAct.setDiagnosisActCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(ddActElement, XPathConstants.NODE)));
			
	        ddAct.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
								evaluate(ddActElement, XPathConstants.NODE)));
		
		}
		return ddAct;
	}
}
