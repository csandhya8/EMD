<!DOCTYPE HTML>
<HTML>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="com.EMD.LSDB.vo.common.ClauseVO"%>
<HEAD>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
	<link href="css/bootstrap.css" TYPE="text/css" rel="stylesheet"/>
    <link href="css/select2.css" TYPE="text/css" rel="stylesheet"/>
	<link href="css/select2-bootstrap.css" TYPE="text/css" rel="stylesheet"/>
	<link href="css/EMDCustom.css" rel="stylesheet"/>
	<link rel="shortcut icon" href="images/favicon.ico" />
	<script type="text/javascript" src="js/Others/jquerylatest.js"></script>
	<script type="text/javascript" src="js/Others/jquerymigrate.js"></script>
	<script type="text/javascript" src="js/Others/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/Others/select2.js"></script>
	<SCRIPT language="JavaScript" SRC="js/Emd_Lsdb.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="js/error.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="js/error_messages.js"></SCRIPT>
	<SCRIPT language="JavaScript" SRC="js/ModelSelectClauseRev.js"></SCRIPT>
	<script>
	       $(document).ready(function() { 
	       	$("#sltSection").select2({theme:'bootstrap'});
		   	$("#sltSubSection").select2({theme:'bootstrap'});
	 	   })
	</script>
<TITLE>Electro Motive Diesel - Locomotive Spec Database</TITLE>
</HEAD>
<BODY CLASS='main'>
	<div class="container" width="100%">
		<html:form action="/modelClauseDescPopupAction.do">
				<h4 class="text-center">Select Clause</h4>
				<div class="row">
					<div class="spacer10"></div>
				</div>
				<div class="row">
					<div class="errorlayerhide" id="errorlayer"></div>
				</div>
				<logic:present name="ModelClauseDescPopUpForm" property="messageID" scope="request">
					<bean:define id="messageID" name="ModelClauseDescPopUpForm" property="messageID"/>
		            <input type="hidden" name="messageID" id="messageID" value="<bean:write name="messageID"/>" >
		            <input type="hidden" name="errorElement" id="errorelement" value="<bean:message name="messageID" bundle="ServerMessageBundle"/>"/>
				</logic:present>
				
				<!-- Logic Tag Check For Display The Error Message After Add Functionality Starts Here -->
				<logic:present name="ModelClauseDescPopUpForm"
					property="errorMessage" scope="request">
					<script>
                            hideErrors();
                            addMessage('<bean:write name="ModelClauseDescPopUpForm" property="errorMessage"/>');
                            showScrollErrors();
                        </script>
				</logic:present> 
				<!-- Logic Tag Check For Display The Error Message After Add Functionality Ends Here -->
				
				<div class="spacer10"></div>
				
				<div class="form-horizontal">
				 		 <div class="form-group">
							<label class="col-sm-5 control-label">Specification Type</label>
							<div class="col-sm-2">					
									<label class="form-control-static"><bean:write name="ModelClauseDescPopUpForm" property="specType" scope="request" /></label>
							</div>
						</div>
						<div class="form-group">			
								<label class="col-sm-5 control-label">Model</label>
								<div class="col-sm-2">					
									<label class="form-control-static"><bean:write name="ModelClauseDescPopUpForm" property="modelName" scope="request" /></label>
								</div>
						</div>
						<div class="form-group required-field">
								<label class="col-sm-5 control-label">Section</label>
								<div class="col-sm-1">
								<html:select styleClass="form-control"
									name="ModelClauseDescPopUpForm" property="sectionSeqNo"
									onchange="javascript:fnLoadSubSection()"  styleId="sltSection">
									<html:option value="-1">---Select---</html:option>
									<logic:present name="ModelClauseDescPopUpForm"
										property="sectionList">
										<html:optionsCollection name="ModelClauseDescPopUpForm"
											property="sectionList" label="sectionDisplay"
											value="sectionSeqNo" />
									</logic:present>
								</html:select>
								</div>
						</div>
						<div class="form-group required-field">
								<label class="col-sm-5 control-label">SubSection</label>
								<div class="col-sm-1">
								<html:select styleClass="form-control"
									name="ModelClauseDescPopUpForm" property="subSecSeqNo"
									onchange="javascript:fnLoadClauseDesc()" styleId="sltSubSection">
									<html:option value="-1">---Select---</html:option>
									<logic:present name="ModelClauseDescPopUpForm"
										property="subSectionList">
										<html:optionsCollection name="ModelClauseDescPopUpForm"
											property="subSectionList" label="subSecDisplay"
											value="subSecSeqNo" />
									</logic:present>
								</html:select>
								</div>
						</div>
				</div>
				<div class="spacer10"></div>	
				<logic:present name="ModelClauseDescPopUpForm" property="componentVO">
						<table class="table table-bordered table-hover">
					    <thead>
					        <tr>
								<TH>Select</TH>
								<TH>Clause No</TH>
								<TH>Clause Description</TH>
								<TH>Engineering Data</TH>
								<TH>Component</TH>
							</tr>	
						</thead>
					    <tbody class="text-center">	
					    	<logic:iterate id="compParent" name="ModelClauseDescPopUpForm"
									property="componentVO" scope="request"
									type="com.EMD.LSDB.vo.common.ClauseVO" indexId="clausecount">
									<TR>
										<TD CLASS="v-midalign"><html:radio
											property="clauseSeqNo" 
											value='<%=String.valueOf(compParent.getClauseSeqNo())%>' /></TD>
										<!---
												*	Clause Number Field is added as per the requirement of 	LSDB_CR-50
												*	Added on 26-June-08 
												*	by ps57222
												-->
										<TD CLASS="v-midalign"><logic:present name="compParent"
											property="clauseNum">
											<bean:write name="compParent" property="clauseNum" />
										</logic:present></TD>
			
										<html:hidden name="compParent" property="versionNo" />
										<TD align="left"><span style="width:435px">
										<div id="clauseID<%=clausecount%>" align="left"
											STYLE="solid;height:80px;overflow:auto;"><bean:define
											name="compParent" property="clauseDesc" id="clauseDesc" /> 
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
							<html:hidden name="compParent" property="clauseDesc" /></div>
							</span></TD>
							<TD CLASS="v-midalign">
							<DIV STYLE="height:63;width:120;overflow-Y: auto ; height:63;width:120;overflow-X: auto ; ">

									<!--<logic:present name="compParent" property="refEDLNO">
										<logic:iterate id="engDataRefEdlNo" name="compParent"
											property="refEDLNO">

											<!--  <logic:notEqual name="engDataRefEdlNo" value="0"> -->

										<!-- 	<bean:message key="screen.refEdl" />
											<bean:write name="engDataRefEdlNo" />
											<br>

											<!--  </logic:notEqual> -->

										<!--</logic:iterate>

									</logic:present>-->
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
									<!-- CR 87 -->
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

										<logic:notEqual name="compParent" property="dwONumber"
											value="0">

											<bean:message key="screen.partNo" />
											<bean:write name="compParent" property="partNumber" />
											<br>

										</logic:notEqual>

									</logic:present> <!--Price Book Number is added in the Engineering data Section, modified on 30-09-08 by ps5722-->


									<logic:present name="compParent" property="priceBookNumber">

										<logic:notEqual name="compParent" property="priceBookNumber"
											value="0">

											<bean:message key="screen.priceBookNo" />&nbsp; <bean:write
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
								<TD class="v-midalign"><logic:iterate
									id="compList" name="compParent" property="compGroupVO"
									type="com.EMD.LSDB.vo.common.CompGroupVO" scope="page"
									indexId="compntIndex">
									<logic:present name="compList" property="compVO">
										<bean:define id="comp" name="compList" property="compVO" />
										<logic:notEqual name="compntIndex" value="0"> ;<br>
										</logic:notEqual>
										<bean:write name="comp" property="componentName" />
									</logic:present>
								</logic:iterate></TD>
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
				  		<logic:notEmpty name="ModelClauseDescPopUpForm" property="hdnClaChrstcFlag">
							<logic:match name="ModelClauseDescPopUpForm" property="hdnClaChrstcFlag" value="Y">
						       <button class="btn btn-primary" type='button' id="okButton" ONCLICK="javascript:SelectClauseDesc()">OK</button>
							</logic:match>
							<logic:match name="ModelClauseDescPopUpForm" property="hdnClaChrstcFlag" value="N">
								<button class="btn btn-primary" type='button' id="okButton" ONCLICK="javascript:SelectNonCharClauseDesc()">OK</button>
							</logic:match>
						</logic:notEmpty>
						<logic:empty name="ModelClauseDescPopUpForm" property="hdnClaChrstcFlag">
							<button class="btn btn-primary" type='button' id="okButton" ONCLICK="javascript:fnSelectClauseDescImage()">OK</button>
						</logic:empty>		
				        <button class="btn btn-primary" type='button' id="cancelButton" ONCLICK="javascript:Cancelpopup()">Cancel</button>
					</div>   
			   	</div>
				<div class="spacer50"></div>
				
				<html:hidden property="modelSeqNo" />
				<html:hidden property="hdnModelName" />
				<html:hidden property="hdnspecType" />
				<!-- Added For CR_81 Locomotive and Power Products Enhancements by RR68151 -->
				<html:hidden property="hdnClaChrstcFlag"/>
				<html:hidden property="linkClaSeqNo"/>	
				<html:hidden property="clauseDes" />
		</html:form>
	</div>
</BODY>
</HTML>