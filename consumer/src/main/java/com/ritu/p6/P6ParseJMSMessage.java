package com.ritu.p6;

import java.util.HashMap;
import java.util.Map;

public class P6ParseJMSMessage {

	public static void main(String[] args) {

	}

	public Map<String,Map<String,String>> parseJMSxml(String jmsXMLMessage){
		/* jmsXMLMessage sample :
		<?xml version="1.0" encoding="UTF-8"?><MessagingObjects xmlns="http://xmlns.oracle.com/Primavera/P6/V16.2/Common/Event" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<ProjectCodeAssignmentUpdated>
			<ProjectCodeObjectId>1487</ProjectCodeObjectId>
			<ProjectCodeTypeObjectId>259</ProjectCodeTypeObjectId>
			<ProjectObjectId>4850</ProjectObjectId>
			<NewValues>
				<ProjectCodeObjectId>1487</ProjectCodeObjectId>
			</NewValues>
			<OldValues>
				<ProjectCodeObjectId>1486</ProjectCodeObjectId>
			</OldValues>
		</ProjectCodeAssignmentUpdated></MessagingObjects>
*/

		Map<String,Map<String,String>> xmlMessage = new HashMap<String,Map<String,String>>(); // HashMap e.g. {Project= {id=1234,name=abcd,status=active},WBS= {id=wbs-1,wbsname=xyz,wbscode=1}}
		
		
		return xmlMessage;
		
	}
}
