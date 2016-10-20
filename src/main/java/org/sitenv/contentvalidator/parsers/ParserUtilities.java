package org.sitenv.contentvalidator.parsers;

import org.apache.log4j.Logger;
import org.sitenv.contentvalidator.dto.ContentValidationResult;
import org.sitenv.contentvalidator.dto.enums.ContentValidationResultLevel;
import org.sitenv.contentvalidator.model.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

public class ParserUtilities {
	
	private static Logger log = Logger.getLogger(ParserUtilities.class.getName());

	public static void compareDataElement(CCDADataElement refCode, CCDADataElement submittedCode,
										  ArrayList<ContentValidationResult> results, String elementName) {
		
		// handle nulls.
		if((refCode != null) && (submittedCode != null) ) {

			if(refCode.matches(submittedCode, results, elementName)) {
				// do nothing since both match.
				log.info(" Both Submitted and Ref codes match for " + elementName);
			}

		}
		else if ((refCode == null) && (submittedCode != null)) {
			ContentValidationResult rs = new ContentValidationResult("The scenario does not require " + elementName + " data, but submitted file does have " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if((refCode != null) && (submittedCode == null)){
			ContentValidationResult rs = new ContentValidationResult("The scenario requires " + elementName + " data, but submitted file does not contain " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		} 
		else {
			// do nothing since both are null.
			log.info(" Both Submitted and Ref codes are null for " + elementName);
		}
	}
	
	public static void compareEffectiveTime(CCDAEffTime refTime, CCDAEffTime submittedTime,
											ArrayList<ContentValidationResult> results, String elementName) {
		
		// handle nulls.
		if((refTime != null) && (submittedTime != null) ) {

			refTime.compare(submittedTime, results, elementName);

		}
		else if ((refTime == null) && (submittedTime != null)) {
			ContentValidationResult rs = new ContentValidationResult("The scenario does not require " + elementName + " data, but submitted file does have " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if((refTime != null) && (submittedTime == null)){
			ContentValidationResult rs = new ContentValidationResult("The scenario requires " + elementName + " data, but submitted file does not contain " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		} 
		else {
			// do nothing since both are null.
			log.info(" Both Submitted and Ref times are null for " + elementName);
		}
	}

	public static void compareCode(CCDACode refCode, CCDACode submittedCode,
								   ArrayList<ContentValidationResult> results, String elementName) {
		
		// handle section code.
		if((refCode != null) && (submittedCode != null) ) {

			if(refCode.matches(submittedCode, results, elementName)) {
				// do nothing since both match.
				log.info(" Both Submitted and Ref codes match for " + elementName);
			}

		}
		else if ((refCode == null) && (submittedCode != null)) {
			ContentValidationResult rs = new ContentValidationResult("The scenario does not require " + elementName + " data, but submitted file does have " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if((refCode != null) && (submittedCode == null)){
			ContentValidationResult rs = new ContentValidationResult("The scenario requires " + elementName + " data, but submitted file does not contain " + elementName + " data", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		} 
		else {
			// do nothing since both are null.
			log.info(" Both Submitted and Ref codes are null for " + elementName);
		}
	}
	
	public static Boolean templateIdsAreFound(ArrayList<CCDAII> refList, ArrayList<CCDAII> submittedList) {
		
		log.info("Checking Template Ids lists ");
		
		if ((refList != null) && (submittedList != null)) {

			// Check to see if each of the templates in the reflist are part of
			// the submitted list.
			for (CCDAII r : refList) {

				if (!r.isPartOf(submittedList)) {
					return false;
				}
			}
			return true;
		} 
		else if ((refList == null) && (submittedList != null)) {
			return false;
		} 
		else if ((refList != null) && (submittedList == null)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static void compareTemplateIds(ArrayList<CCDAII> refList, ArrayList<CCDAII> submittedList,
										  ArrayList<ContentValidationResult> results, String elementName) {
		
		if((refList != null) && (submittedList != null)) {
		
			// Check to see if each of the templates in the reflist are part of the submitted list.
			for(CCDAII r : refList) {
			
				if(!r.isPartOf(submittedList)) {
					String error = "The " + elementName + " template id, Root Value = " 
							+ ((r.getRootValue() != null)?r.getRootValue():"None specified") + " and Extension Value = " 
					        + ((r.getExtValue() != null)?r.getExtValue():"No Extension value") + " is not present in the submitted CCDA's " + elementName;
					ContentValidationResult rs = new ContentValidationResult(error, ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
					results.add(rs);
				}
				else {
					log.info("Template Ids for " + elementName + " matched. ");
				}
			
			}
		}
		else if((refList == null) && (submittedList != null)) {

			ContentValidationResult rs = new ContentValidationResult("The scenario does not require " + elementName + " template ids, but submitted file does contain " + elementName + " template ids", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if ((refList != null) && (submittedList == null)){
			ContentValidationResult rs = new ContentValidationResult("The scenario requires " + elementName + " template ids, but submitted file does not containt " + elementName + " template ids", ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		} 
		else {
			// do nothing since both are null
			log.info("Both Ref and submitted CCDA have no templated Ids for " + elementName);
		}
	}
	
	public static CCDACode readCode(Element codeElement)
	{
		CCDACode code = null;
		if(codeElement != null)
		{
			code = new CCDACode();
			if(!isEmpty(codeElement.getAttribute("code")))
			{
				code.setCode(codeElement.getAttribute("code"));
			}
			if(!isEmpty(codeElement.getAttribute("codeSystem")))
			{
				code.setCodeSystem(codeElement.getAttribute("codeSystem"));
			}
			if(!isEmpty(codeElement.getAttribute("codeSystemName")))
			{
				code.setCodeSystemName(codeElement.getAttribute("codeSystemName"));
			}
			if(!isEmpty(codeElement.getAttribute("displayName")))
			{
				code.setDisplayName(codeElement.getAttribute("displayName"));
			}
			if(!isEmpty(codeElement.getAttribute("xsi:type")))
			{
				code.setXpath(codeElement.getAttribute("xsi:type"));
			}
		}
		return code;
	}
	
	public static CCDAII readTemplateID(Element templateElement)
	{
		CCDAII templateID = null;
		
		if(templateElement != null)
		{
			templateID = new CCDAII();
			if(!isEmpty(templateElement.getAttribute("root")))
			{
				templateID.setRootValue(templateElement.getAttribute("root"));
			}
			if(!isEmpty(templateElement.getAttribute("extension")))
			{
				templateID.setExtValue(templateElement.getAttribute("extension"));
			}
		}
		return templateID;
	}
	
	public static ArrayList<CCDAII> readTemplateIdList(NodeList templateIDNodeList)
	{
		ArrayList<CCDAII> templateList = null;
		if( ! isNodeListEmpty(templateIDNodeList))
		{
			templateList = new ArrayList<>();
		}
		Element templateElement;
		for (int i = 0; i < templateIDNodeList.getLength(); i++) {
			templateElement = (Element) templateIDNodeList.item(i);
			templateList.add(readTemplateID(templateElement));
		}
		return templateList;
	} 
	
	public static CCDAEffTime readEffectiveTime(Element effectiveTimeElement) throws XPathExpressionException
	{
		CCDAEffTime effectiveTime = null;
		
		if(effectiveTimeElement != null)
		{
			effectiveTime = new CCDAEffTime();
			
			effectiveTime.setLow(readDataElement((Element) CCDAConstants.REL_EFF_TIME_LOW_EXP.
	    				evaluate(effectiveTimeElement, XPathConstants.NODE)));
			effectiveTime.setHigh(readDataElement((Element) CCDAConstants.REL_EFF_TIME_HIGH_EXP.
	    				evaluate(effectiveTimeElement, XPathConstants.NODE)));
			
			if((effectiveTime.getLow() == null) && (effectiveTime.getHigh() == null)) {
				effectiveTime.setValue(readDataElement(effectiveTimeElement));
			}
			
			/*if(effectiveTime.getLow() != null)
			{
				effectiveTime.setLowPresent(true);
			}else
				effectiveTime.setLowPresent(false);
			
			if(effectiveTime.getHigh() != null)
			{
				effectiveTime.setHighPresent(true);
			}else
				effectiveTime.setHighPresent(false);*/
				
		}
		return effectiveTime;
	}
	
	public static CCDADataElement readDataElement(Element nodeElement)
	{
		
		CCDADataElement dataElement = null;
		if(nodeElement != null)
		{
			dataElement = new CCDADataElement();
			
			log.info(" Node name = " + nodeElement.getNodeName());
			
			// Handle element which has value attribute
			if(!isEmpty(nodeElement.getAttribute("value")))
			{
				log.info("Reading Value for node: " + nodeElement.getNodeName() + " = " + nodeElement.getAttribute("value"));
				dataElement.setValue(nodeElement.getAttribute("value"));
			}
			else if(nodeElement.getFirstChild() != null) {
				log.info("Reading Value for node: " + nodeElement.getNodeName() + " = " + nodeElement.getFirstChild().getNodeValue());
				dataElement.setValue(nodeElement.getFirstChild().getNodeValue());
			}
			
			
			if(!isEmpty(nodeElement.getAttribute("lineNumber")))
			{
				dataElement.setLineNumber(Integer.parseInt(nodeElement.getAttribute("lineNumber")));
			}
			if(!isEmpty(nodeElement.getAttribute("xpath")))
			{
				dataElement.setXpath(nodeElement.getAttribute("xpath"));
			}
			if(!isEmpty(nodeElement.getAttribute("use")))
			{
				dataElement.setUse(nodeElement.getAttribute("use"));
			}
		}
		return dataElement;
	}
	
	public static ArrayList<CCDADataElement> readDataElementList(NodeList dataElementNodeList)
	{
		ArrayList<CCDADataElement> dataElementList = null;
		if( ! isNodeListEmpty(dataElementNodeList))
		{
			dataElementList = new ArrayList<>();
		}
		Element dataElement;
		for (int i = 0; i < dataElementNodeList.getLength(); i++) {
			dataElement = (Element) dataElementNodeList.item(i);
			dataElementList.add(readDataElement(dataElement));
		}
		return dataElementList;
	}
	
	public static CCDAPQ readQuantity(Element quantityElement)
	{
		CCDAPQ quantity = null;
		if(quantityElement != null)
		{
			quantity = new CCDAPQ();
			if(!isEmpty(quantityElement.getAttribute("unit")))
			{
				quantity.setUnits(quantityElement.getAttribute("unit"));
			}
			if(!isEmpty(quantityElement.getAttribute("value")))
			{
				quantity.setValue(quantityElement.getAttribute("value"));
			}
			quantity.setXsiType("PQ");
		}
		return quantity;
	}
	
	public static ArrayList<CCDACode> readCodeList(NodeList codeNodeList)
	{
		ArrayList<CCDACode> codeList = null;
		if( ! isNodeListEmpty(codeNodeList))
		{
			codeList = new ArrayList<>();
		}
		Element codeElement;
		for (int i = 0; i < codeNodeList.getLength(); i++) {
			codeElement = (Element) codeNodeList.item(i);
			codeList.add(readCode(codeElement));
		}
		return codeList;
	}
	
	public static CCDAFrequency readFrequency(Element frequencyElement)
	{
		CCDAFrequency frequency = null;
		if(frequencyElement != null)
		{
			frequency =new CCDAFrequency();
			if(!isEmpty(frequencyElement.getAttribute("institutionSpecified")))
			{
				frequency.setInstitutionSpecified(Boolean.parseBoolean(frequencyElement.getAttribute("institutionSpecified")));
			}
			if(!isEmpty(frequencyElement.getAttribute("operator")))
			{
				frequency.setOperator(frequencyElement.getAttribute("operator"));
			}
			Element periodElement  = (Element) frequencyElement.getElementsByTagName("period").item(0);
			if(periodElement != null)
			{
				if(!isEmpty(periodElement.getAttribute("value")))
				{
					frequency.setValue(periodElement.getAttribute("value"));
				}
				if(!isEmpty(periodElement.getAttribute("unit")))
				{
					frequency.setUnit(periodElement.getAttribute("unit"));
				}
			}
		}
		
		return frequency;
	}
	
	public static boolean isEmpty(final String str)
	{
		return str == null || str.trim().length() == 0;
	}
	
	public static boolean isNodeListEmpty(final NodeList nodeList)
	{
		return nodeList == null || nodeList.getLength() == 0;
	}
	
	public static ArrayList<CCDAAddress> readAddressList(NodeList addressNodeList)throws XPathExpressionException
	{
		ArrayList<CCDAAddress> addressList = null;
		if( ! isNodeListEmpty(addressNodeList))
		{
			log.info("Address list found");
			addressList = new ArrayList<CCDAAddress>(); 
		}
		
		Element addrElement;
		for (int i = 0; i < addressNodeList.getLength(); i++) {
			
			log.info("Address found");
			addrElement = (Element) addressNodeList.item(i);
			addressList.add(readAddress(addrElement));
		}
		return addressList;
	}
	
	public static ArrayList<CCDADataElement> readTextContentList(NodeList inputNodeList)
	{
		ArrayList<CCDADataElement> dataList = null;
		if( ! isNodeListEmpty(inputNodeList))
		{
			dataList = new ArrayList<CCDADataElement>();
		}
		for (int i = 0; i < inputNodeList.getLength(); i++) {
			Element value = (Element) inputNodeList.item(i);
			dataList.add(readTextContext(value));
		}
		return dataList;
	}
	
	public static CCDADataElement readTextContext(Element element)
	{
		return element == null ? null : readDataElement(element) ;
	}
	
	public static CCDAAddress readAddress(Element addrElement)throws XPathExpressionException
	{
		CCDAAddress address = null;
		if(addrElement != null)
		{
			log.info("Creating address");
			address = new CCDAAddress();
			
			if(!isEmpty(addrElement.getAttribute("use")))
			{
				address.setPostalAddressUse(addrElement.getAttribute("use"));
			}
			
			address.setAddressLine1(readTextContext((Element) CCDAConstants.REL_STREET_ADDR1_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setAddressLine2(readTextContext((Element) CCDAConstants.REL_STREET_ADDR2_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setCity(readTextContext((Element) CCDAConstants.REL_CITY_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setState(readTextContext((Element) CCDAConstants.REL_STATE_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setPostalCode(readTextContext((Element) CCDAConstants.REL_POSTAL_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setCountry(readTextContext((Element) CCDAConstants.REL_COUNTRY_EXP.
	    				evaluate(addrElement, XPathConstants.NODE)));
		}
		return address;
	}
}
