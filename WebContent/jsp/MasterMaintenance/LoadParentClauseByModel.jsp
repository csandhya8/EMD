<!DOCTYPE HTML>
<HTML>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="com.EMD.LSDB.vo.common.ClauseVO"%>
<%@page import="com.EMD.LSDB.vo.common.CompGroupVO"%>
<HEAD>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
	<link href="css/bootstrap.css" TYPE="text/css" rel="stylesheet"/>
    <link href="css/select2.css" TYPE="text/css" rel="stylesheet"/>
	<link href="css/select2-bootstrap.css" TYPE="text/css" rel="stylesheet"/>
	<link REL="stylesheet" TYPE="text/css" HREF="css/jquery-ui-custom.min.css">
	<link href="css/EMDCustom.css" rel="stylesheet"/>
	<link href="css/dataTables.bootstrap.css" TYPE="text/css" rel="stylesheet"/>
	<link rel='stylesheet' type='text/css' href='css/confirm.css' />
	<link rel="shortcut icon" href="images/favicon.ico" />
	<script type="text/javascript" src="js/Others/jquerylatest.js"></script>
	<script type="text/javascript" src="js/Others/jquerymigrate.js"></script>
	<script type="text/javascript" src="js/Others/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/Others/select2.js"></script>
	<script type="text/javascript" src="js/Others/jquery.datatables.js"></script>
	<SCRIPT type="text/javascript" SRC="js/Emd_Lsdb.js"></SCRIPT>
	<SCRIPT type="text/javascript" SRC="js/error.js"></SCRIPT>
	<SCRIPT type="text/javascript" SRC="js/error_messages.js"></SCRIPT>
	<script type="text/javascript" src="js/Others/jquery.bootbox.min.js"></script>
	<SCRIPT language="JavaScript" SRC="js/AddClause.js"></SCRIPT>
<TITLE>Electro Motive Diesel - Locomotive Spec Database</TITLE>
</HEAD>
<BODY CLASS='main'>
	<div class="container" width="100%">
		<html:form action="/parentClauseSearchAction.do">
			<h4 class="text-center">Clause Maintenance - Parent Clause</h4>
				<div class="spacer10"></div>
				<div class="row">
					<div class="col-sm-4 col-sm-offset-4">
						<span id="errorlayer"></span>
					</div>
				</div>
				<logic:present name="ParentClauseForm" property="messageID" scope="request">
					<bean:define id="messageID" name="ParentClauseForm" property="messageID"/>
		            <input type="hidden" name="messageID" id="messageID" value="<bean:write name="messageID"/>" >
		            <input type="hidden" name="errorElement" id="errorelement" value="<bean:message name="messageID" bundle="ServerMessageBundle"/>"/>
		         </logic:present> 
		         <!-- Logic Tag Check For Display The Success Message After Add Functionality Ends Here -->
				<!-- Logic Tag Check For Display The Error Message After Add Functionality Starts Here -->
				<logic:present name="ParentClauseForm" property="errorMessage"
					scope="request">
					<script>
	                    hideErrors();
	                    addMessage('<bean:write name="ParentClauseForm" property="errorMessage"/>');
	                    showScrollErrors();
	                </script>
	
				</logic:present> <!-- Logic Tag Check For Display The Error Message After Add Functionality Ends Here -->
			<div class="spacer10"></div>
				
				<div class="row">
					<div class="col-sm-2 col-sm-offset-3 text-right text-nowrap"><strong>Specification Type</strong></div>
					<div class="col-sm-1 text-center">:</div>
					<div class="col-sm-2 text-left">
						<bean:write name="ParentClauseForm" property="specType" scope="request" />
					</div>
				</div>
				<div class="spacer10"></div>
				<div class="row">
					<div class="col-sm-2 col-sm-offset-3 text-right"><strong>Model</strong></div>
					<div class="col-sm-1 text-center">:</div>
					<div class="col-sm-2 text-left">
						<bean:write name="ParentClauseForm" property="modelName" scope="request" />
					</div>
				</div>	
				<div class="spacer10"></div>
				<div class="row">
					<div class="col-sm-2 col-sm-offset-3 text-right"><strong>Section</strong></div>
					<div class="col-sm-1 text-center">:</div>
					<div class="col-sm-2 text-left text-nowrap">
						<bean:write name="ParentClauseForm" property="sectionName" scope="request" />
					</div>
				</div>	
				<div class="spacer10"></div>
				<div class="row">
					<div class="col-sm-2 col-sm-offset-3 text-right"><strong>Sub Section</strong></div>
					<div class="col-sm-1 text-center">:</div>
					<div class="col-sm-2 text-left text-nowrap">
						<bean:write name="ParentClauseForm" property="subSectionName" scope="request"/>
					</div>
				</div>	
				<div class="spacer30"></div>	
				<logic:present name="ParentClauseForm" property="componentVO">
					<table class="table table-bordered table-hover" width="100%">
					    <thead>
					        <tr>
								<TH CLASS="text-center" WIDTH="2%">Select</TH>
								<TH CLASS="text-center text-nowrap"	WIDTH="5%">Clause No</TH>
								<TH CLASS="text-center text-nowrap"	WIDTH="15%">Clause Description</TH>
								<TH CLASS="text-center text-nowrap"	WIDTH="5%">Engineering Data</TH>
								<TH CLASS="text-center text-nowrap" WIDTH="5%">Component</TH>
							</tr>	
						</thead>
					    <tbody class="text-center">	
					    	<logic:iterate id="compParent" name="ParentClauseForm" property="componentVO" scope="request"
								type="com.EMD.LSDB.vo.common.ClauseVO" indexId="clausecount">
								<TR>
								<TD CLASS="v-midalign">
									<html:radio property="clauseSeqNo" value='<%=String.valueOf(compParent.getClauseSeqNo())%>' />
								</TD>
									<!---
									*	Clause Number Field is added as per the requirement of LSDB_CR-50
									*	Added on 26-June-08 
									*	by ps57222
								-->
								<TD CLASS="v-midalign">
									<logic:present name="compParent" property="clauseNum">
										<bean:write name="compParent" property="clauseNum" />
									</logic:present>
								</TD>
								<!-- <html:hidden property="clauseVersionNo"/> -->
								<TD>
									<div id="clauseID<%=clausecount%>" align="left"
										STYLE="solid;height:80px;overflow:auto;">
										<bean:define name="compParent" property="clauseDesc" id="clauseDesc" />
										<!-- CR-128 - Updated for Pdf issue -->
										<%String strClauseDesc =  String.valueOf(clauseDesc);
										if(strClauseDesc.startsWith("<p>")){%>
											<%=(String.valueOf(clauseDesc))%>
										<%}else{ %>	
											<%=(String.valueOf(clauseDesc)).replaceAll("\n","<br>")%>							
										<%}%>
										<!-- CR-128 - Updated for Pdf issue Ends Here-->
										
									
										<logic:notEmpty name="compParent" property="tableArrayData1">
												<table class="table table-bordered text-center">
		
						 						<logic:iterate id="outter" name="compParent"
												property="tableArrayData1" indexId="counter">
												<!-- Added  For CR_93 -->
												<bean:define name="compParent" property="tableDataColSize" id="tableDataColSize" />
												<bean:define id="row" name="counter" />
												<tr>
													<logic:iterate id="tabrow" name="outter" length="tableDataColSize">
		
														<logic:equal name="row" value="0">
															<td valign="top" width="5%"><b><font
																color="navy"><%=(tabrow==null)? "&nbsp;":tabrow%></b></font>
															</td>
														</logic:equal>
		
														<logic:notEqual name="row" value="0">
															<td valign="top" width="5%"><%=(tabrow==null)? "&nbsp;":tabrow%>
															</td>
														</logic:notEqual>
		
		
													</logic:iterate>
												</tr>
												
											</logic:iterate>
											</table>
										</logic:notEmpty>
		
									<bean:define id="cla" name="compParent" property="clauseDesc" />
									<bean:define id="tab" name="compParent"
										property="tableArrayData1" /> <input type="hidden"
										name="clauseDescTmp" value="<%=cla%>"> <input type="hidden"
										name="tableArrayData1Tmp" value="<%=tab%>"> <!-- <html:hidden  name="compParent" property="clauseDesc"/> 	
							   			<html:hidden name="compParent" property="tableArrayData1"/> -->
									</div>
									</TD>
		
									<TD CLASS="v-midalign">
										<DIV STYLE="height:63;width:120;overflow-Y: auto ; height:63;width:120;overflow-X: auto ; ">
												<logic:present name="compParent"
													property="edlNO">
			
													<logic:iterate id="engDataEdlNo" name="compParent"
														property="edlNO">
			
														<!--   <logic:notEqual name="engDataEdlNo" value="0"> -->
			
														<bean:message key="screen.edl" />
														<bean:write name="engDataEdlNo" />
														<br>
			
														<!--  </logic:notEqual> -->
			
													</logic:iterate>
			
												</logic:present> 
												<!--  CR 87 -->
												
												<logic:present name="compParent" property="refEDLNO">
			
													<logic:iterate id="engDataRefEdlNo" name="compParent"
														property="refEDLNO">
			
														<!--  <logic:notEqual name="engDataRefEdlNo" value="0"> -->
			
														<bean:message key="screen.refEdl.start" />
														<bean:write name="engDataRefEdlNo" />
														<bean:message key="screen.refEdl.end" />
														<br>
			
														<!--  </logic:notEqual> -->
			
													</logic:iterate>
			
												</logic:present> 
												<logic:present name="compParent"
													property="partOF">
			
													<logic:iterate id="engPartOfCd" name="compParent"
														property="partOF">
			
														<logic:notEqual name="engPartOfCd" value="0">
			
															<bean:message key="screen.partOf" />
															<bean:write name="engPartOfCd" />
															<br>
			
														</logic:notEqual>
			
													</logic:iterate>
			
												</logic:present> <logic:present name="compParent"
													property="dwONumber">
			
													<logic:notEqual name="compParent" property="dwONumber"
														value="0">
			
														<bean:message key="screen.dwoNo" />
														<bean:write name="compParent" property="dwONumber" />
														<br>
			
													</logic:notEqual>
			
												</logic:present> <logic:present name="compParent"
													property="partNumber">
													<!--  Modified since it was checking dWonumber instead of part number -->
													<logic:notEqual name="compParent" property="partNumber"
														value="0">
			
														<bean:message key="screen.partNo" />
														<bean:write name="compParent" property="partNumber" />
														<br>
			
													</logic:notEqual>
			
												</logic:present> <!--Price Book Number is added in the Engineering data Section, modified on 30-09-08 by ps5722-->
			
												<logic:present name="compParent" property="priceBookNumber">
			
													<logic:notEqual name="compParent" property="priceBookNumber"
														value="0">
			
														<bean:message key="screen.priceBookNo" /> &nbsp;<bean:write
															name="compParent" property="priceBookNumber" />
														<br>
			
													</logic:notEqual>
			
												</logic:present> <logic:present name="compParent"
													property="standardEquipmentDesc">
			
													<bean:write name="compParent" property="standardEquipmentDesc" />
													<br>
			
												</logic:present> <logic:present name="compParent"
													property="engDataComment">
													<bean:define id="engDatCmnt" name="compParent"
														property="engDataComment" />
													<%=engDatCmnt %>
													<br>
			
												</logic:present>
										</DIV>
									</TD>
		
		
									<logic:notEmpty name="compParent" property="compGroupVO">
										<TD class="v-midalign" valign="top">
											<logic:iterate id="compList" name="compParent" property="compGroupVO"
												type="com.EMD.LSDB.vo.common.CompGroupVO" scope="page"
												indexId="compntIndex">
												<logic:present name="compList" property="compVO">
													<bean:define id="comp" name="compList" property="compVO" />
													<logic:notEqual name="compntIndex" value="0"> ;<br>
													</logic:notEqual>
													<bean:write name="comp" property="componentName" />
												</logic:present>
											</logic:iterate>
										</TD>
									</logic:notEmpty>
		
									<logic:empty name="compParent" property="compGroupVO">
										<td class="v-midalign">&nbsp;</td>
									</logic:empty>
								</TR>	
							</logic:iterate>
						</tbody>	
					 </table>   
				</logic:present>
				<div class="spacer10"></div>
				
				<div class="form-group">
				  <div class="col-sm-12 text-center">
				       <button class="btn btn-primary" type='button' id="okButton" ONCLICK="javascript:SelectParentClauses()">OK</button>
   				       <button class="btn btn-primary" type='button' id="cancelButton" ONCLICK="javascript:window.close();">Cancel</button>
					</div>   
			   	</div>
				<div class="spacer50"></div>
		</html:form>
	</div>
</BODY>
</HTML>		