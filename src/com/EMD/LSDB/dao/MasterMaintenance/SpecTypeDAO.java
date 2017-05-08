/*
 * Created on Apr 10, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.EMD.LSDB.dao.MasterMaintenance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import org.apache.tomcat.dbcp.dbcp2.DelegatingConnection;//Added for Tomcat & CR-123

import oracle.jdbc.OracleTypes;
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
import com.EMD.LSDB.vo.common.SpecTypeVo;

/*******************************************************************************
 * @author Satyam Computer Services Ltd
 * @version 1.0 This class has the business methods for the Spec Type
 ******************************************************************************/

public class SpecTypeDAO extends EMDDAO {
	
	private SpecTypeDAO() {
	}
	
	//CR-84 added 
	/***************************************************************************
	 * @author Satyam Computer Services Ltd
	 * @version 1.0
	 * @param objSpecTypeVo
	 *            the object containg null
	 * @return ArrayList contains the list of spec types
	 * @throws EMDException
	 **************************************************************************/
	
	public static ArrayList fetchSpecTypes(SpecTypeVo objSpecTypeVo)
	throws EMDException {
		LogUtil.logMessage("Inside the  fetchSpecTypeDetails:fetchSpecTypes");
		Connection objConnnection = null;
		ArrayList arSpecList = new ArrayList();
		
		
		String strLogUser = "";
		String strMessage = null;
		ResultSet rsSpecType = null;
		ResultSet rsScreen=null;
		CallableStatement objCallableStatement = null;
		int intLSDBErrorID = 0;
		String strOracleCode = null;
		String strErrorMessage = null;
		try {
			strLogUser = objSpecTypeVo.getUserID();
			objConnnection = DBHelper.prepareConnection();
			LogUtil.logMessage("objConnnection in fetchSpecTypes :"
					+ objConnnection);
						
			objCallableStatement = objConnnection
			.prepareCall(EMDQueries.SP_SELECT_SPEC_TYPES);
			if(objSpecTypeVo.getSpectypeSeqno()!= 0)
			{
				objCallableStatement.setInt(1,objSpecTypeVo.getSpectypeSeqno());
				
			}else{
				objCallableStatement.setNull(1, Types.NULL);
				
			}
			
			if(objSpecTypeVo.getStrScreenID()!=null)
			{
				objCallableStatement.setString(2,objSpecTypeVo.getStrScreenID());
				
			}else{
				objCallableStatement.setNull(2, Types.NULL);
				
			}
			
			objCallableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			objCallableStatement.setString(4, objSpecTypeVo.getUserID());
			objCallableStatement.registerOutParameter(5, Types.INTEGER);
			objCallableStatement.registerOutParameter(6, Types.VARCHAR);
			objCallableStatement.registerOutParameter(7, Types.VARCHAR);
			
			objCallableStatement.execute();
			
			LogUtil.logMessage("user in fetchSpecTypes :");
			
			rsSpecType = (ResultSet) objCallableStatement.getObject(3);
			
			LogUtil
			.logMessage("Inside the fetchSpecTypes method of DAO :resultSet"
					+ rsSpecType);
			
			intLSDBErrorID = objCallableStatement.getInt(5);
			LogUtil.logMessage("LSDBErrorID:" + intLSDBErrorID);
			strOracleCode = objCallableStatement.getString(6);
			LogUtil.logMessage("OracleErrorCode:" + strOracleCode);
			strErrorMessage = (String) objCallableStatement.getString(7);
			LogUtil.logMessage("ErrorMessage:" + strErrorMessage);
			
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Enters into Error Loop:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				strMessage = String.valueOf(intLSDBErrorID);
				LogUtil.logMessage("Error message in DAO:" + strMessage);
				objErrorInfo.setMessageID(strMessage);
				LogUtil.logMessage("Error message in ErrorInfo:"
						+ objErrorInfo.getMessageID());
				LogUtil.logMessage("inside loop intLSDBErrorID:");
				throw new DataAccessException(objErrorInfo);
				
				
			} else if (strOracleCode != null && !"0".equals(strOracleCode)) {
				LogUtil.logMessage("enters into oracle error code block:"
						+ strOracleCode);
				ErrorInfo objErrorInfo = new ErrorInfo();
				StringBuffer objStBuffer = new StringBuffer();
				objStBuffer.append(strOracleCode + " ");
				objStBuffer.append(strErrorMessage);
				objErrorInfo.setMessage(objStBuffer.toString());
				objConnnection.rollback();
				throw new ApplicationException(objErrorInfo);
				
			}
					
			
			while (rsSpecType.next()) {
				LogUtil.logMessage("inside loop rsSpecType:");
			SpecTypeVo specTypeVo= new SpecTypeVo();
			specTypeVo.setSpectypeSeqno(rsSpecType.getInt(DatabaseConstants.SPEC_TYPE_SEQ_NO));
			specTypeVo.setSpectypename(rsSpecType.getString(DatabaseConstants.SPEC_TYPE));
			if((rsSpecType.getString(DatabaseConstants.LS030_SPEC_TYPE_DESC)!=null))
					{
			specTypeVo.setComments(rsSpecType.getString(DatabaseConstants.LS030_SPEC_TYPE_DESC));
					}else{
						specTypeVo.setComments("");
					}
			
			rsScreen = (ResultSet) rsSpecType.getObject(DatabaseConstants.SCREENMAP);
			
			while(rsScreen.next())
			{	
				LogUtil.logMessage("inside loop rsScreen:");

				if((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)==8))
						{
				specTypeVo.setCustomerMaintID((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)));
				LogUtil.logMessage("inside loop CustomerMaintID=="+specTypeVo.getCustomerMaintID());
				if(rsScreen.getString(DatabaseConstants.FLAG).equals("Y"))
				{
					specTypeVo.setCustMaintDisPlayFlag(true);
					
						}else {
							
							specTypeVo.setCustMaintDisPlayFlag(false);
						}
					}
				if((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)==9))
				{
				specTypeVo.setDistributorMaintID((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)));
				LogUtil.logMessage("inside loop DistributorMaintID=="+specTypeVo.getDistributorMaintID());
				if(rsScreen.getString(DatabaseConstants.FLAG).equals("Y"))
				{				
				specTypeVo.setDistMaintDisPlayFlag(true);
				}else{
				specTypeVo.setDistMaintDisPlayFlag(false);
				}
				}
				if((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)==18)){
				specTypeVo.setGeneralArrangementMaintID((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)));
				LogUtil.logMessage("inside loop GeneralArrangementMaintID=="+specTypeVo.getGeneralArrangementMaintID());
				
				if(rsScreen.getString(DatabaseConstants.FLAG).equals("Y"))
						{
					specTypeVo.setGenArrMaintDisPlayFlag(true);
					
						}else{
							specTypeVo.setGenArrMaintDisPlayFlag(false);
							
						}
				}
				if((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)==19)){
				specTypeVo.setPerformanceCurveMaintID((rsScreen.getInt(DatabaseConstants.LS020_SCREEN_ID)));
				LogUtil.logMessage("inside loop GeneralArrangementMaintID=="+specTypeVo.getPerformanceCurveMaintID());
				if(rsScreen.getString(DatabaseConstants.FLAG).equals("Y"))
						{
				specTypeVo.setPerfCurveMaintDisPlayFlag(true);
				}else{
					specTypeVo.setPerfCurveMaintDisPlayFlag(false);
				}
				
				LogUtil.logMessage("ended loop rsScreen2222:"+specTypeVo.getSpectypeSeqno()+""+specTypeVo.getSpectypename()+""+specTypeVo.getComments());
				
			}
			}
			
			
			
			DBHelper.closeSQLObjects(rsScreen, null, null);
			arSpecList.add(specTypeVo);

			}
			DBHelper.closeSQLObjects(rsSpecType, null, null);
			LogUtil.logMessage("ended loop of fetchSpecTypeDetails :");
			
		}
		
		catch (Exception objExp) {
			
			LogUtil
			.logMessage("Enters into Exception fetchSpecTypeDAO:fetchSpecTypeDetails");
			ErrorInfo objErrorInfo = new ErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objExp.getMessage());
			throw new ApplicationException(objExp, objErrorInfo);
		} finally {
			try {
				DBHelper.closeConnection(objConnnection);
			} catch (Exception objExp) {
				LogUtil
				.logMessage("Enters into Exception SpecTypeDAO:fetchSpecTypeDetails");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessage(ApplicationConstants.LOG_USER
						+ strLogUser + "-" + objExp.getMessage());
				throw new ApplicationException(objExp, objErrorInfo);
			}
			
		}
		
		LogUtil.logMessage("objSpecType in fetchSpecTypes :" + arSpecList);
		return arSpecList;
		
	}
	
	/***************************************************************************
	 * @author Satyam Computer Services Ltd
	 * @version 1.0
	 * @param objSpecTypeVo
	 *            the object containg null
	 * @return ArrayList contains the list of spec types
	 * @throws EMDException
	 **************************************************************************/
	
	public static int insertSpecTypeDetails(SpecTypeVo objSpecTypeVo)
	throws EMDException {
		LogUtil.logMessage("Inside the  SpecTypeDAO:fetchSpecTypes");
		Connection objConnnection = null;
		String strLogUser = "";
		CallableStatement objCallableStatement = null;
		int intStatus = 0;
		String strOracleCode = null;
		String strErrorMessage = null;
		int intLSDBErrorID = 0;
	
		try {
			strLogUser = objSpecTypeVo.getUserID();
			objConnnection = DBHelper.prepareConnection();
			objCallableStatement = objConnnection
			.prepareCall(EMDQueries.SP_INSERT_SPEC_TYPE);
		objCallableStatement.setString(1, objSpecTypeVo.getSpectypename());
			objCallableStatement.setString(2, objSpecTypeVo.getComments());
			objCallableStatement.setString(3, objSpecTypeVo.getUserID());
			objCallableStatement.registerOutParameter(4, Types.INTEGER);
			objCallableStatement.registerOutParameter(5, Types.VARCHAR);
			objCallableStatement.registerOutParameter(6, Types.VARCHAR);
			objConnnection.setAutoCommit(false);
			intStatus = objCallableStatement.executeUpdate();
			
			if (intStatus > 0) {
				intStatus = 0;
				
			}
			
			LogUtil
			.logMessage("Inside the insertSpecTypeDetails method of SpecTypeDAO :intStatus .."
					+ intStatus);
			intLSDBErrorID = (int) objCallableStatement.getInt(4);
			strOracleCode = (String) objCallableStatement.getString(5);
			strErrorMessage = (String) objCallableStatement.getString(6);
			
			
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Enters into Error Loop:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessageID("" + intLSDBErrorID);
				LogUtil.logMessage("Error message in ErrorInfo:"
						+ objErrorInfo.getMessageID());
				throw new DataAccessException(objErrorInfo);
				
			} else if (strOracleCode != null && !"0".equals(strOracleCode)) {
				//Un handled exception				
				LogUtil.logMessage("strOracleCode:" + strOracleCode);
				ErrorInfo objErrorInfo = new ErrorInfo();
				StringBuffer strbuffer = new StringBuffer();
				strbuffer.append(strOracleCode + " ");
				strbuffer.append(strErrorMessage);
				LogUtil.logMessage("sb.toString():" + strbuffer.toString());
				objErrorInfo.setMessage(strbuffer.toString());
				LogUtil.logError(objErrorInfo);
				objConnnection.rollback();
				throw new ApplicationException(objErrorInfo);
			}
			
		}
		
		catch (DataAccessException objDataExp) {
			ErrorInfo objErrorInfo = objDataExp.getErrorInfo();
			objErrorInfo.setMessage(objDataExp.getMessage());
			LogUtil
			.logMessage("Enters into DataAccess Exception block in SpecTypeDAO.insertSpecTypeDetails"
					+ objDataExp.getErrorInfo().getMessageID());
			throw new BusinessException(objDataExp, objErrorInfo);
		} catch (ApplicationException objAppExp) {
			ErrorInfo objErrorInfo = objAppExp.getErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objErrorInfo.getMessage());
			LogUtil
			.logMessage("Enters into AppException block in  SpecTypeDAO.insertSpecTypeDetails"
					+ objErrorInfo.getMessage());
			throw new ApplicationException(objAppExp, objErrorInfo);
		} catch (Exception objExp) {
			LogUtil
			.logMessage("Enters into Exception block in SpecTypeDAO.insertSpecTypeDetails"
					+ objExp.getMessage());
			ErrorInfo objErrorInfo = new ErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objExp.getMessage());
			throw new ApplicationException(objExp, objErrorInfo);
		}
		
		finally {
			try {
				
				DBHelper.closeSQLObjects(null, objCallableStatement,
						objConnnection);
			}
			
			catch (Exception objExp) {
				LogUtil
				.logMessage("Enters into Exception block in DAO:Close Connection"
						+ objExp.getMessage());
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
	 * @param objSpecTypeVo
	 *            the object containg null
	 * @return ArrayList contains the list of spec types
	 * @throws EMDException
	 **************************************************************************/
	
	public static int updateSpecTypeDetails(SpecTypeVo objSpecTypeVo)
	throws EMDException {
		LogUtil.logMessage("Inside SpecTypeDAO:updateSpecTypeDetails");
		Connection objConnnection = null;
		CallableStatement objCallableStatement = null;
		int intStatus = 0;
		// Error out parameters
		String strOracleCode = null;
		String strErrorMessage = null;
		int intLSDBErrorID = 0;
		String strLogUser = "";
		ArrayDescriptor arrdesc = null;
		try {
			strLogUser = objSpecTypeVo.getUserID();
			objConnnection = DBHelper.prepareConnection();
			LogUtil.logMessage("objConnnection in DAO in Update Method:"
					+ objConnnection);
			objCallableStatement = objConnnection
			.prepareCall(EMDQueries.SP_UPDATE_SPEC_TYPE);
			Connection dconn = ((DelegatingConnection) objConnnection).getInnermostDelegate(); //Added for CR-123 & Tomcat
			
			objCallableStatement.setInt(1, objSpecTypeVo.getSpectypeSeqno());
			objCallableStatement.setString(2, objSpecTypeVo.getSpectypename());
			objCallableStatement.setString(3, objSpecTypeVo.getComments());
			arrdesc = new ArrayDescriptor(DatabaseConstants.IN_ARRAY,
					dconn);
			
			LogUtil.logMessage("ArrayList ScreenId Size :"+objSpecTypeVo.getScreenIdList().length);
			ARRAY arrScreenno = new ARRAY(arrdesc, dconn, objSpecTypeVo.getScreenIdList());
			
			objCallableStatement.setArray(4, arrScreenno);
				
			objCallableStatement.setString(5, objSpecTypeVo.getUserID());
			objCallableStatement.registerOutParameter(6, Types.INTEGER);
			objCallableStatement.registerOutParameter(7, Types.VARCHAR);
			objCallableStatement.registerOutParameter(8, Types.VARCHAR);
			LogUtil.logMessage("objConnnection in DAO in updateSpecTypeDetails22***2222");
						
			
			intStatus = objCallableStatement.executeUpdate();
			LogUtil.logMessage("objConnnection in DAO in updateSpecTypeDetails3333333333");
			
			if (intStatus > 0) {
				intStatus = 0;
			}
			
			LogUtil
			.logMessage("Inside the Update method of SpecTypeDAO :intStatus .."
					+ intStatus);
			intLSDBErrorID = (int) objCallableStatement.getInt(6);
			strOracleCode = (String) objCallableStatement.getString(7);
			strErrorMessage = (String) objCallableStatement.getString(8);
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Error ID:" + intLSDBErrorID);
				LogUtil.logMessage("strOracleCode:" + strOracleCode);
				
			}
			if (intLSDBErrorID != 0) {
				LogUtil.logMessage("Enters into Error Loop in Update Method:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				
				objErrorInfo.setMessageID("" + intLSDBErrorID);
				LogUtil.logMessage("Error message in ErrorInfo:"
						+ objErrorInfo.getMessageID());
				throw new DataAccessException(objErrorInfo);
				
			} else if (strOracleCode != null && !"0".equals(strOracleCode)) {// Un
				// handled
				// exception
				
				LogUtil.logMessage("strOracleCode:" + strOracleCode);
				ErrorInfo objErrorInfo = new ErrorInfo();
				StringBuffer sb = new StringBuffer();
				sb.append(strOracleCode + " ");
				sb.append(strErrorMessage);
				LogUtil.logMessage("sb.toString():" + sb.toString());
				objErrorInfo.setMessage(sb.toString());
				LogUtil.logError(objErrorInfo);
				objConnnection.rollback();
				throw new ApplicationException(objErrorInfo);
			}
			
		}
		
		catch (DataAccessException objDataExp) {
			ErrorInfo objErrorInfo = objDataExp.getErrorInfo();
			objErrorInfo.setMessage(objDataExp.getMessage());
			LogUtil
			.logMessage("ENters into catch block in SpecTypeDAO:updateSpecTypeDetails:"
					+ objDataExp.getErrorInfo().getMessageID());
			throw new BusinessException(objDataExp, objErrorInfo);
		} catch (ApplicationException objAppExp) {
			ErrorInfo objErrorInfo = objAppExp.getErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objErrorInfo.getMessage());
			LogUtil
			.logMessage("Enters into AppException block in SpecTypeDAO:updateSpecTypeDetails:"
					+ objErrorInfo.getMessage());
			throw new ApplicationException(objAppExp, objErrorInfo);
		} catch (Exception objExp) {
					LogUtil
			.logMessage("Enters into Exception block in SpecTypeDAO:updateSpecTypeDetails:");
			ErrorInfo objErrorInfo = new ErrorInfo();
			objErrorInfo.setMessage(ApplicationConstants.LOG_USER + strLogUser
					+ "-" + objExp.getMessage());
			throw new ApplicationException(objExp, objErrorInfo);
		}
		
		finally {
			try {
				DBHelper.closeSQLObjects(null, objCallableStatement,
						objConnnection);
			} catch (SQLException sqlex) {
				LogUtil
				.logMessage("Enters into Exception block in SpecTypeDAO:updateSpecTypeDetails:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessage(ApplicationConstants.LOG_USER
						+ strLogUser + "-" + sqlex.getMessage());
				throw new ApplicationException(sqlex, objErrorInfo);
			} catch (Exception objExp) {
				LogUtil
				.logMessage("Enters into Exception block in SpecTYpeDAO:updateSpecTypeDetails:");
				ErrorInfo objErrorInfo = new ErrorInfo();
				objErrorInfo.setMessage(ApplicationConstants.LOG_USER
						+ strLogUser + "-" + objExp.getMessage());
				throw new ApplicationException(objExp, objErrorInfo);
			}
			
		}
		
		return intStatus;
		
	}
	
}