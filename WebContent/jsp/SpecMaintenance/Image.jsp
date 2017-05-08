<!DOCTYPE HTML>
<HTML>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/EMDCustomTags.tld" prefix="LSDB"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<HEAD>
<link href="css/bootstrap.css" TYPE="text/css" rel="stylesheet"/>
<link href="css/EMDCustom.css" rel="stylesheet"/>
<script type="text/javascript" src="js/Others/jquerylatest.js"></script>
<script type="text/javascript" src="js/Others/jquerymigrate.js"></script>
<script type="text/javascript" src="js/Others/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/vtip.css" />
<script type="text/javascript" src="js/Others/jquery.vtip.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="js/Appendix.js"></SCRIPT>
</HEAD>
<TITLE>Electro Motive Diesel - Locomotive Spec Database</TITLE>

<BODY topmargin=0 leftmargin=0>
	<div class="container" width="80">
		<html:form action="/showAppendix" method="post" styleClass="form-horizontal">
			
			<div class="spacer50"></div>
			<div class="row">
				<div class="col-sm-12 bg-info text-center">
					<div class=" push-text-down"><strong>Image Name : </strong>
					<bean:write name="AppendixForm" property="imageName" />
					</div>
				</div>
			</div>
			<div class="spacer30"></div>
			<bean:define id="imageName" name="AppendixForm" property="imageName" />
			<bean:define id="imageSeqNo" name="AppendixForm" property="imageSeqNo" />
			<div class="form-group">
				<div class="col-sm-12 text-center">
					<div id="Image_container">
						<a title="Click to Save this Image." class="vtip" href="javascript:fnSaveImages(<%=imageSeqNo%>,'<%=String.valueOf(imageName)%>');">
						<img src="<%=request.getContextPath()%>/DownLoadImage.do?method=downloadImage&ImageSeqNo='<%=imageSeqNo%>"
							border="0" ></a>
					</div>
				</div>
			</div>
			<div class="spacer30"></div>
			<div class="form-group">
				<div class="col-sm-12 text-center">
					<p><bean:message key="AppendixImageMessage" /></p>
					<button class="btn btn-primary" type='button' ONCLICK="javascript:fnClose()">Close</button>
				</div>
			</div>
		</html:form>
	</div>
</BODY>
</HTML>
