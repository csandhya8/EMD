/**
 * 
 */
package com.EMD.LSDB.dao.MasterMaintenance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.tomcat.dbcp.dbcp2.DelegatingConnection;//Added for Tomcat

import oracle.jdbc.driver.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import com.EMD.LSDB.common.constant.ApplicationConstants;
import com.EMD.LSDB.common.constant.DatabaseConstants;
import com.EMD.LSDB.common.errorhandler.ErrorInfo;
import com.EMD.LSDB.common.exception.ApplicationException;
import com.EMD.LSDB.common.exception.BusinessException;
import com.EMD.LSDB.common.exception.DataAccessException;
import com.EMD.LSDB.common.exception.EMDException;
import com.EMD.LSDB.common.logger.LogUtil;
import com.EMD.LSDB.dao.common.DBHelper;
import com.EMD.LSDB.dao.common.EMDDAO;
import com.EMD.LSDB.dao.common.EMDQueries;
import com.EMD.LSDB.vo.common.ClauseTableDataVO;
import com.EMD.LSDB.vo.common.ClauseVO;
import com.EMD.LSDB.vo.common.StandardEquipVO;

/**
 * @author ps57222
 * 
 */
public class ModelAddClauseRevDAO extends EMDDAO {
	
	/***************************************************************************
	 * @author Satyam Computer Services Ltd
	 * @version 1.0
	 * @param objClauseVO
	 *            the object for searching Clause details
	 * @return ClauseVO the list contains the models
	 * @throws EMDException
	 **************************************************************************/
	public static ArrayList fetchClauses(ClauseVO objClauseVO)
	throws EMDException {
		LogUtil.logMessage("Enters into ModelAddClauseRevDAO:fetchClauses");
		Connection objConnection = null;
		CallableStatement objCallableStatement = null;
		
		String strOracleCode = null;
		String strErrorMessage = null;
		String strMessage = null;
		int intLSDBErrorID = 0;
		
		ResultSet objClauseResultSet = null;
		ResultSet objEDLNoResultSet = null;
		ResultSet objRefEDLNoResultSet = null;
		ResultSet objSubSecResultSet = null;
		ResultSet objTbDataResultSet = null;
		ResultSet objStdEqpResultSet = null;
		
		int cntEDL = 0;
		int cntRefEDl = 0;
		int cntPartOf = 0;
		int[] arlPartSubSecSeqNo = new int[4];
		String[] arlEDLNos = { "", "", "" };
		String[] arlRefEDLNos = { "", "", "" };
		String[] arlpartSubSecCode = { "", "", "", "" };
		
		ArrayList arlClauseList = new ArrayList();
		ArrayList arlTableRows = new ArrayList();
		ArrayList arlTableColumns = null;
		String strLogUser = "";
		try {
			strLogUser = objClauseVO.getUserID();
			objConnection = DBHelper.prepareConnection();
			objCallableStatement = objConnection
			.prepareCall(EMDQueries.SP_SELECT_MDL_CLAUSE);
			LogUtil.logMessage("ModelSeqNo ModelAddClauseRevDAO:fetchClauses:"
					+ objClauseVO.getModelSeqNo());
			objCallableStatement.setInt(1, objClauseVO.getModelSeqNo());
			LogUtil
			.logMessage("SubSectionSeqNo ModelAddClauseRevDAO:fetchClauses:"
					+ objClauseVO.getSubSectionSeqNo());
			objCallableStatement.setInt(2, objClauseVO.getSubSectionSeqNo());
			LogUtil.logMessage("ClauseSeqNo ModelAddClauseRevDAO:fetchClauses:"
					+ objClauseVO.getClauseSeqNo());
			LogUtil.logMessage("VersionNo ModelAddClauseRevDAO:fetchClauses:"
					+ objClauseVO.getVersionNo());
			if (objClauseVO.getClauseSeqNo() == 0) {
				objCallableStatement.setNull(3, Types.NULL);
				
			} else {
				objCallableStatement.setInt(3, objClauseVO.getClauseSeqNo());
			}
			if (objClauseVO.getVersionNo() == 0) {
				objCallableStatement.setNull(4, Types.NULL);
			} else {
				objCallableStatement.setInt(4, objClauseVO.getVersionNo());
			}
			if (objClauseVO.getDefaultFlag() == null) {
				objCallableStatement.setNull(5, Types.NULL);
			} else {
				objCallableStatement.setString(5, objClauseVO.getDefaultFlag());
			}
			objCallableStatement.registerOutParameter(6, OracleTypes.CURSOR);
			objCallableStatement.setString(7, objClauseVO.getUserID());
			objCallableStatement.registerOutParameter(8, Types.INTEGER);
			objCallableStatement.registerOutParameter(9, Types.VARCHAR);
			objCallableStatement.registerOutParameter(10, Types.VARCHAR);
			objCallableStatement.execute();
			
			objClauseResultSet = (ResultSet) objCallableStatement.getObject(6);
			
			intLSDBErrorID = objCallableStatement.getInt(8);
			LogUtil.logMessage("LSDBErrorID:" + intLSDBErrorID);
			strOracleCode = objCallableStatement.getString(9);
			LogUtil.logMessage("OracleErrorCode:" + strOracleCode);
			strErrorMessage = (String) objCallableStatement.getString(10);
			LogUtil.logMessage("ErrorMessage:" + strErrorMessage);
			
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Enters into Error Loop:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				strMessage = String.valueOf(intLSDBErrorID);
				LogUtil.logMessage("Error message in DAO:" + strMessage);
				objErrorInfo.setMessageID(strMessage);
				LogUtil.logMessage("Error message in ErrorInfo:"
						+ objErrorInfo.getMessageID());
				
				throw new DataAccessException(objErrorInfo);
				
			} else if (strOracleCode != null && !"0".equals(strOracleCode)) {
				LogUtil.logMessage("enters into oracle error code block:"
						+ strOracleCode);
				ErrorInfo objErrorInfo = new ErrorInfo();
				StringBuffer objStBuffer = new StringBuffer();
				objStBuffer.append(strOracleCode + " ");
				objStBuffer.append(strErrorMessage);
				objErrorInfo.setMessage(objStBuffer.toString());
				objConnection.rollback();
				throw new ApplicationException(objErrorInfo);
				
			}
			
			while (objClauseResultSet.next()) {
				objClauseVO = new ClauseVO();
				
				objClauseVO.setClauseSeqNo(objClauseResultSet
						.getInt(DatabaseConstants.LS300_CLA_SEQ_NO));
				LogUtil.logMessage("ClauseSeqNo:"
						+ objClauseVO.getClauseSeqNo());
				objClauseVO.setVersionNo(objClauseResultSet
						.getInt(DatabaseConstants.LS301_VERSION_NO));
				LogUtil.logMessage("VersionNo:" + objClauseVO.getVersionNo());
				objClauseVO.setClauseDesc(objClauseResultSet
						.getString(DatabaseConstants.LS301_CLA_DESC));
				LogUtil.logMessage("ClauseDesc:" + objClauseVO.getClauseDesc());
				objClauseVO.setClauseDescForTextArea(objClauseResultSet
						.getString(DatabaseConstants.LS301_CLA_DESC));
				objClauseVO.setCustomerName(objClauseResultSet
						.getString(DatabaseConstants.LS050_CUST_NAME));
				LogUtil.logMessage("CustomerName:"
						+ objClauseVO.getCustomerName());
				objClauseVO.setModifiedBy(objClauseResultSet
						.getString(DatabaseConstants.LS301_UPDT_USER_ID));
				objClauseVO.setModifiedDate(objClauseResultSet
						.getString(DatabaseConstants.LS301_UPDT_DATE));
				objClauseVO.setDefaultFlag(objClauseResultSet
						.getString(DatabaseConstants.LS301_DEFAULT_FLAG));
				
				objEDLNoResultSet = (ResultSet) objClauseResultSet.getObject(8);
				
				while (objEDLNoResultSet.next()) {
					LogUtil.logMessage("Enters into EDLNo Resultset Loop:");
					arlEDLNos[cntEDL] = objEDLNoResultSet
					.getString(DatabaseConstants.LS302_EDL_NO);
					
					cntEDL++;
					
				}
				
				objClauseVO.setEdlNo(arlEDLNos);
				LogUtil.logMessage("Length of EdlNumber:" + arlEDLNos.length);
				
				DBHelper.closeSQLObjects(objEDLNoResultSet, null, null);
				
				objRefEDLNoResultSet = (ResultSet) objClauseResultSet
				.getObject(9);
				while (objRefEDLNoResultSet.next()) {
					LogUtil.logMessage("Enters into RefEDLNo Resultset Loop:");
					arlRefEDLNos[cntRefEDl] = objRefEDLNoResultSet
					.getString(DatabaseConstants.LS303_REF_EDL_NO);
					
					cntRefEDl++;
					
				}
				
				LogUtil.logMessage("Length of RefEdlNumber:"
						+ arlRefEDLNos.length);
				objClauseVO.setRefEDLNo(arlRefEDLNos);
				
				DBHelper.closeSQLObjects(objRefEDLNoResultSet, null, null);
				
				objSubSecResultSet = (ResultSet) objClauseResultSet
				.getObject(10);
				
				while (objSubSecResultSet.next()) {
					LogUtil
					.logMessage("PartCode Of values"
							+ objSubSecResultSet
							.getString(DatabaseConstants.LS304_SUBSEC_NO));
					arlpartSubSecCode[cntPartOf] = objSubSecResultSet
					.getString(DatabaseConstants.LS304_SUBSEC_NO);
					arlPartSubSecSeqNo[cntPartOf] = objSubSecResultSet
					.getInt(DatabaseConstants.LS202_SUBSEC_SEQ_NO);
					
					cntPartOf++;
					
				}
				
				objClauseVO.setPartOfCode(arlpartSubSecCode);
				LogUtil.logMessage("PartOfCode:" + objClauseVO.getPartOfCode());
				LogUtil.logMessage("Length of PartOfCode:"
						+ arlpartSubSecCode.length);
				objClauseVO.setPartOfSeqNo(arlPartSubSecSeqNo);
				LogUtil.logMessage("PartOfSeqNo:"
						+ objClauseVO.getPartOfSeqNo());
				
				DBHelper.closeSQLObjects(objSubSecResultSet, null, null);
				
				objTbDataResultSet = (ResultSet) objClauseResultSet
				.getObject(11);
				
				while (objTbDataResultSet.next()) {
					LogUtil.logMessage("Enters into TableData Resultset Loop:");
					arlTableColumns = new ArrayList();
					arlTableColumns.add(objTbDataResultSet
							.getString(DatabaseConstants.LS306_TBL_DATA_COL_1));
					arlTableColumns.add(objTbDataResultSet
							.getString(DatabaseConstants.LS306_TBL_DATA_COL_2));
					arlTableColumns.add(objTbDataResultSet
							.getString(DatabaseConstants.LS306_TBL_DATA_COL_3));
					arlTableColumns.add(objTbDataResultSet
							.getString(DatabaseConstants.LS306_TBL_DATA_COL_4));
					arlTableColumns.add(objTbDataResultSet
							.getString(DatabaseConstants.LS306_TBL_DATA_COL_5));
					arlTableRows.add(arlTableColumns);
					
					LogUtil
					.logMessage("headerflag:"
							+ objTbDataResultSet
							.getString(DatabaseConstants.LS306_HEADER_FLAG));
				}
				objClauseVO.setTableArrayData1(arlTableRows);
				
				DBHelper.closeSQLObjects(objTbDataResultSet, null, null);
				
				objClauseVO.setDwONumber(objClauseResultSet
						.getInt(DatabaseConstants.LS301_DWO_NUMBER));
				LogUtil.logMessage("DWO Number:" + objClauseVO.getDwONumber());
				objClauseVO.setPartNumber(objClauseResultSet
						.getInt(DatabaseConstants.LS301_PART_NUMBER));
				LogUtil
				.logMessage("Part Number:"
						+ objClauseVO.getPartNumber());
				objClauseVO.setPriceBookNumber(objClauseResultSet
						.getInt(DatabaseConstants.LS301_PRICE_BOOK_NUMBER));
				LogUtil.logMessage("Price book number:"
						+ objClauseVO.getPriceBookNumber());
				
				objStdEqpResultSet = (ResultSet) objClauseResultSet
				.getObject(15);
				while (objStdEqpResultSet.next()) {
					LogUtil.logMessage("Enters into std equip ResultSet");
					objClauseVO.setStandardEquipmentDesc(objStdEqpResultSet
							.getString(DatabaseConstants.LS060_STD_EQP_DESC));
					objClauseVO.setStandardEquipmentSeqNo(objStdEqpResultSet
							.getInt(DatabaseConstants.LS060_STD_EQP_SEQ_NO));
					LogUtil.logMessage("StandardEquipmentDesc:"
							+ objClauseVO.getStandardEquipmentDesc());
					
				}
				
				DBHelper.closeSQLObjects(objStdEqpResultSet, null, null);
				
				objClauseVO.setComments(objClauseResultSet
						.getString(DatabaseConstants.LS301_ENGG_DATA_COMMENTS));
				LogUtil.logMessage("Comments:" + objClauseVO.getComments());
				objClauseVO.setReason(objClauseResultSet
						.getString(DatabaseConstants.LS301_CLA_REASON));
				LogUtil.logMessage("Reason:" + objClauseVO.getReason());
				
				arlClauseList.add(objClauseVO);
				
			}
			
		} catch (DataAccessException objDataExp) {
			ErrorInfo objErrorInfo = objDataExp.getErrorInfo();
			LogUtil
			.logMessage("ENters into DataAccessException block in ModelAddClauseRevDAO:fetchClauses"
					+ objDataExp.getMessage());
			LogUtil
			.logMessage("ENters into DataAccessException block in ModelAddClauseRevDAO:fetchClauses"
					+ objErrorInfo.getMessageID());
			throw new BusinessException(objDataExp, objErrorInfo);
		} catch (ApplicationException objAppExp) {
			ErrorInfo objErrorInfo = objAppExp.getErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objErrorInfo.getMessage());
			LogUtil
			.logMessage("ENters into ApplicationException block in ModelAddClauseRevDAO:fetchClauses"
					+ objErrorInfo.getMessage());
			throw new ApplicationException(objAppExp, objErrorInfo);
			
		} catch (Exception objExp) {
			LogUtil
			.logMessage("Enters into Exception block in ModelAddClauseRevDAO:fetchClauses:");
			ErrorInfo objErrorInfo = new ErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objExp.getMessage());
			throw new ApplicationException(objExp, objErrorInfo);
		}
		
		finally {
			try {
				
				DBHelper.closeSQLObjects(objClauseResultSet,
						objCallableStatement, objConnection);
				
			}
			
			catch (Exception objExp) {
				LogUtil
				.logMessage("Enters into Exception block in ModelAddClauseRevDAO:fetchClauses:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessage(ApplicationConstants.LOG_USER
						+ strLogUser + "-" + objExp.getMessage());
				throw new ApplicationException(objExp, objErrorInfo);
			}
		}
		
		return arlClauseList;
		
	}
	
	/***************************************************************************
	 * @author Satyam Computer Services Ltd
	 * @version 1.0
	 * @param objClauseVO
	 *            the object for Inserting clause
	 * @return boolean that returns True or False
	 * @throws EMDException
	 **************************************************************************/
	
	public static synchronized int insertClause(ClauseVO objClauseVO)
	throws EMDException {
		LogUtil.logMessage("Inside the ModelClauseDAO:insertClause");
		Connection objConnection = null;
		CallableStatement objCallableStatement = null;
		int intStatus = 0;
		String strOracleCode = null;
		String strErrorMessage = null;
		String strMessage = null;
		int intLSDBErrorID = 0;
		int intClauseSeqNo;
		int intClauseVersionNo;
		ArrayDescriptor arrdesc = null;
		ArrayList arlStandardEquipmentList = new ArrayList();
		ARRAY arr = null;
		ClauseTableDataVO objTableDataVO = null;
		ArrayList arlTableList;
		ARRAY tableDataArr1, tableDataArr2, tableDataArr3, tableDataArr4, tableDataArr5 = null;
		String strLogUser = "";
		try {
			strLogUser = objClauseVO.getUserID();
			objConnection = DBHelper.prepareConnection();
			objCallableStatement = objConnection
			.prepareCall(EMDQueries.insert_Clause);
			
			objCallableStatement.setInt(1, objClauseVO.getModelSeqNo());
			objCallableStatement.setInt(2, objClauseVO.getSubSectionSeqNo());
			
			if (objClauseVO.getCustomerSeqNo() <= 0) {
				objCallableStatement.setNull(3, Types.NULL);
			} else {
				objCallableStatement.setInt(3, objClauseVO.getCustomerSeqNo());
			}
			LogUtil.logMessage("ClauseSEqNo:******"
					+ objClauseVO.getClauseSeqNo());
			
			objCallableStatement.setInt(4, objClauseVO.getClauseSeqNo());
			
			if (objClauseVO.getParentClauseSeqNo() <= 0) {
				objCallableStatement.setNull(5, Types.NULL);
			} else {
				objCallableStatement.setInt(5, objClauseVO
						.getParentClauseSeqNo());
			}
			objCallableStatement.setString(6, objClauseVO.getClauseDesc());
			Connection dconn = ((DelegatingConnection) objConnection).getInnermostDelegate(); //Added for CR-123 & Tomcat
			
			arrdesc = new ArrayDescriptor(DatabaseConstants.STR_ARRAY,
					dconn);
			arr = new ARRAY(arrdesc, dconn,
					processComponentVO(objClauseVO));//Modified For Tomcat
			objCallableStatement.setArray(7, arr);
			
			ARRAY arrEdlno = new ARRAY(arrdesc, dconn, objClauseVO
					.getEdlNo());
			if (arrEdlno.length() == 0) {
				objCallableStatement.setNull(8, Types.NULL);
			} else {
				objCallableStatement.setArray(8, arrEdlno);
			}
			ARRAY arrRefEDLNO = new ARRAY(arrdesc, dconn, objClauseVO
					.getRefEDLNo());
			
			if (objClauseVO.getRefEDLNo() == null) {
				arrRefEDLNO = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(9, arrRefEDLNO);
			} else {
				objCallableStatement.setArray(9, arrRefEDLNO);
			}
			
			ARRAY arrPartOfSeqNO = new ARRAY(arrdesc, dconn,
					objClauseVO.getPartOfSeqNo());
			if (arrPartOfSeqNO.length() == 0) {
				
				objCallableStatement.setNull(10, Types.NULL);
			} else {
				
				objCallableStatement.setArray(10, arrPartOfSeqNO);
			}
			
			ARRAY arrPartOfSeqCode = new ARRAY(arrdesc, dconn,
					objClauseVO.getPartOfCode());
			
			if (arrPartOfSeqCode.length() == 0) {
				objCallableStatement.setNull(11, Types.NULL);
			} else {
				objCallableStatement.setArray(11, arrPartOfSeqCode);
			}
			
			if (objClauseVO.getDwONumber() == 0) {
				objCallableStatement.setNull(12, Types.NULL);
			} else {
				objCallableStatement.setInt(12, objClauseVO.getDwONumber());
			}
			
			if (objClauseVO.getPartNumber() == 0) {
				objCallableStatement.setNull(13, Types.NULL);
			} else {
				objCallableStatement.setInt(13, objClauseVO.getPartNumber());
			}
			
			if (objClauseVO.getPriceBookNumber() == 0) {
				objCallableStatement.setNull(14, Types.NULL);
			} else {
				objCallableStatement.setInt(14, objClauseVO
						.getPriceBookNumber());
			}
			
			if (objClauseVO.getObjStandardEquipVO() == null) {
				objCallableStatement.setNull(15, Types.NULL);
			} else {
				arlStandardEquipmentList = objClauseVO.getObjStandardEquipVO();
				StandardEquipVO objStandardEquipVO = (StandardEquipVO) arlStandardEquipmentList
				.get(0);
				objCallableStatement.setInt(15, objStandardEquipVO
						.getStandardEquipmentSeqNo());
			}
			
			if (objClauseVO.getComments() == null) {
				objCallableStatement.setNull(16, Types.NULL);
			} else {
				objCallableStatement.setString(16, objClauseVO.getComments());
			}
			
			objCallableStatement.setString(17, objClauseVO.getReason());
			
			arlTableList = objClauseVO.getTableDataVO();
			
			objTableDataVO = (ClauseTableDataVO) arlTableList.get(0);
			
			tableDataArr1 = new ARRAY(arrdesc, objConnection, objTableDataVO
					.getTableArrayData1());
			
			if (objTableDataVO.getTableArrayData1() == null) {
				tableDataArr1 = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(18, tableDataArr1);
			} else {
				objCallableStatement.setArray(18, tableDataArr1);
			}
			
			objTableDataVO = (ClauseTableDataVO) arlTableList.get(1);
			tableDataArr2 = new ARRAY(arrdesc, dconn, objTableDataVO
					.getTableArrayData2());
			
			if (objTableDataVO.getTableArrayData2() == null) {
				tableDataArr2 = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(19, tableDataArr2);
			} else {
				objCallableStatement.setArray(19, tableDataArr2);
			}
			
			objTableDataVO = (ClauseTableDataVO) arlTableList.get(2);
			tableDataArr3 = new ARRAY(arrdesc, dconn, objTableDataVO
					.getTableArrayData3());
			
			if (objTableDataVO.getTableArrayData3() == null) {
				tableDataArr3 = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(20, tableDataArr3);
			} else {
				objCallableStatement.setArray(20, tableDataArr3);
			}
			
			objTableDataVO = (ClauseTableDataVO) arlTableList.get(3);
			tableDataArr4 = new ARRAY(arrdesc, dconn, objTableDataVO
					.getTableArrayData4());
			
			if (objTableDataVO.getTableArrayData4() == null) {
				tableDataArr4 = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(21, tableDataArr4);
			} else {
				objCallableStatement.setArray(21, tableDataArr4);
			}
			
			objTableDataVO = (ClauseTableDataVO) arlTableList.get(4);
			tableDataArr5 = new ARRAY(arrdesc, dconn, objTableDataVO
					.getTableArrayData5());
			if (objTableDataVO.getTableArrayData5() == null) {
				tableDataArr5 = new ARRAY(arrdesc, dconn, null);
				objCallableStatement.setArray(22, tableDataArr5);
			} else {
				objCallableStatement.setArray(22, tableDataArr5);
			}
			
			objCallableStatement.setString(23, objClauseVO.getUserID());
			
			/* Added for Attach Clause CR - Begin */
			if (objClauseVO.getLeadCompGrpSeqNo() == 0) {
				objCallableStatement.setNull(24, Types.NULL);
				LogUtil.logMessage("Lead Comp Grp Seq No"
						+ objClauseVO.getLeadCompGrpSeqNo());
			} else {
				objCallableStatement.setInt(24, objClauseVO
						.getLeadCompGrpSeqNo());
				LogUtil.logMessage("Lead Comp Grp Seq No"
						+ objClauseVO.getLeadCompGrpSeqNo());
			}
			/* Added for Attach Clause CR - End */
			
			objCallableStatement.registerOutParameter(25, Types.INTEGER);
			objCallableStatement.registerOutParameter(26, Types.VARCHAR);
			objCallableStatement.registerOutParameter(27, Types.VARCHAR);
			objCallableStatement.registerOutParameter(28, Types.INTEGER);
			objCallableStatement.registerOutParameter(29, Types.INTEGER);
			
			intStatus = objCallableStatement.executeUpdate();
			
			LogUtil.logMessage("After execute update");
			
			if (intStatus > 0) {
				intStatus = 0;
				
			}
			
			LogUtil.logMessage("BEFORE LSDB ERROR ID111111");
			
			intLSDBErrorID = (int) objCallableStatement.getInt(25);
			LogUtil.logMessage("LSDB ERROR ID111111");
			
			strOracleCode = (String) objCallableStatement.getString(26);
			LogUtil.logMessage("Oracle Error Code111");
			
			strErrorMessage = (String) objCallableStatement.getString(27);
			LogUtil.logMessage("Oracle Error ID11111");
			
			intClauseSeqNo = (int) objCallableStatement.getInt(28);
			intClauseVersionNo = (int) objCallableStatement.getInt(29);
			
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Enters into Error Loop:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				strMessage = String.valueOf(intLSDBErrorID);
				LogUtil.logMessage("Error message in DAO:" + strMessage);
				
				objErrorInfo.setMessageID(strMessage);
				LogUtil.logMessage("Error message in ErrorInfo:"
						+ objErrorInfo.getMessageID());
				
				throw new DataAccessException(objErrorInfo);
			} else if (strOracleCode != null && !"0".equals(strOracleCode)) {
				LogUtil.logMessage("enters into oracle error code block:"
						+ strOracleCode);
				ErrorInfo objErrorInfo = new ErrorInfo();
				StringBuffer objStBuffer = new StringBuffer();
				objStBuffer.append(strOracleCode + " ");
				objStBuffer.append(strErrorMessage);
				objErrorInfo.setMessage(objStBuffer.toString());
				objConnection.rollback();
				throw new ApplicationException(objErrorInfo);
			}
		}
		
		catch (DataAccessException objDataExp) {
			ErrorInfo objErrorInfo = objDataExp.getErrorInfo();
			LogUtil
			.logMessage("ENters into DataAccessException block in ModelAddClauseRevDAO:insertClause"
					+ objDataExp.getMessage());
			LogUtil
			.logMessage("ENters into DataAccessException block in ModelAddClauseRevDAO:insertClause"
					+ objErrorInfo.getMessageID());
			throw new BusinessException(objDataExp, objErrorInfo);
		} catch (ApplicationException objAppExp) {
			ErrorInfo objErrorInfo = objAppExp.getErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objErrorInfo.getMessage());
			LogUtil
			.logMessage("ENters into ApplicationException block in ModelAddClauseRevDAO:insertClause"
					+ objAppExp.getMessage());
			throw new ApplicationException(objAppExp, objErrorInfo);
			
		} catch (Exception objExp) {
			LogUtil
			.logMessage("Enters into Exception block in ModelAddClauseRevDAO:insertClause:");
			ErrorInfo objErrorInfo = new ErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objExp.getMessage());
			objExp.printStackTrace();
			throw new ApplicationException(objExp, objErrorInfo);
		}
		
		finally {
			try {
				
				DBHelper.closeSQLObjects(null, objCallableStatement,
						objConnection);
			}
			
			catch (Exception objExp) {
				LogUtil
				.logMessage("Enters into Exception block1 in ModelAddClauseRevDAO:insertClause:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessage(ApplicationConstants.LOG_USER
						+ strLogUser + "-" + objExp.getMessage());
				throw new ApplicationException(objExp, objErrorInfo);
			}
			
		}
		
		return intStatus;
		
	}
	
	/***************************************************************************
	 * @author Satyam Computer Services Ltd
	 * @version 1.0
	 * @param objClauseVO
	 *            the object for processing Components
	 * @return integer Array that returns component Sequence No's
	 * @throws EMDException
	 **************************************************************************/
	private static int[] processComponentVO(ClauseVO objClauseVO)
	throws EMDException {
		LogUtil
		.logMessage("Inside the processComponentVO method of AddClauseDAO");
		
		int intComponentSeqNo[];
		
		ArrayList componentSeqArray = objClauseVO.getComponentVO();
		intComponentSeqNo = new int[componentSeqArray.size()];
		if (componentSeqArray != null && componentSeqArray.size() > 0) {
			for (int i = 0; i < componentSeqArray.size(); i++) {
				intComponentSeqNo[i] = Integer.parseInt(componentSeqArray
						.get(i).toString());
			}
		}
		
		return intComponentSeqNo;
	}
	
}
