package com.pilog.mdm.cloud.DAO;

import com.pilog.mdm.cloud.access.DataAccess;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DashBoardsConversationDAO {

	@Autowired
	private DataAccess access;

	@Transactional
	public List getConversationalAIData(HttpServletRequest request) {
		List listData = new ArrayList();
		String messageId = request.getParameter("messageId"); 
		try {
			String selectQuery = "SELECT " 
		            + "MESSAGE_ID, "// 0
					+ "RIGHT_MESSAGE, "// 1
					+ "LEFT_MESSAGE, "// 2
					+ "RIGHT_BUTTON, "// 3
					+ "LEFT_BUTTON, "// 4
					+ "RIGHT_BUTTON_METHOD, "// 5
					+ "LEFT_BUTTON_METHOD, "// 6
					+ "RIGHT_NEXT_METHOD, "// 7
					+ "LEFT_NEXT_METHOD, "// 8
					+ "REPLIED_ID, "// 9
					+ "CUSTOM_COL1, "// 10
					+ "CUSTOM_COL2, "// 11
					+ "CUSTOM_COL3, "// 12
					+ "CUSTOM_COL4, "// 13
					+ "CUSTOM_COL5, "// 14
					+ "CUSTOM_COL6, "// 15
					+ "CUSTOM_COL7, "// 16
					+ "CUSTOM_COL8, "// 17
					+ "CUSTOM_COL9, "// 18
					+ "CUSTOM_COL10 "// 19
					+ "FROM CREATE_CHART_CONVERSATIONS WHERE MESSAGE_ID =:MESSAGE_ID";
			Map mapData = new HashMap();
			mapData.put("MESSAGE_ID", messageId);
			listData = access.sqlqueryWithParams(selectQuery, mapData);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listData;

	}

	@Transactional
	public JSONObject getConversationalAIMessage(HttpServletRequest request) {
		JSONObject jsonData = new JSONObject();
		try {
			String mainDiv = "<div class='visionChartsAutoSuggestionsClass'>";
			String rightMainDIv = "";
			String leftMainDIv = "";
			List listData = getConversationalAIData(request);
			if (listData != null && !listData.isEmpty()) {
				mainDiv += "<div class='convai-message'>";
				rightMainDIv += "<div class='convai_right_main_message'>";
				leftMainDIv += "<div class='convai_left_main_message'>";
				for (int i = 0; i < listData.size(); i++) {
					Object[] objData = (Object[]) listData.get(i);
					if (objData != null) {
						int messageId =  ((BigDecimal)objData[0]).intValue();
						String rightMsg = (String) objData[1];
						String leftMsg = (String) objData[2];
						String rightBtn = (String) objData[3];
						String leftBtn = (String) objData[4];
						String rightBtnMtd = (String) objData[5];
						String leftBtnMtd = (String) objData[6];
						String rightNxtMtd = (String) objData[7];
						String leftNxtMtd = (String) objData[8];
						int repliedId=0;
						if(objData[9] !=null) {
						   repliedId = ((BigDecimal) objData[9]).intValue();
						}
						if (rightMsg != null && !"".equalsIgnoreCase(rightMsg)) {
							rightMainDIv += "<div class='visionConversationalAIClass convai-right-message nonLoadedBubble'>" + rightMsg
									+ "</div>";
						}
						if (leftMsg != null && !"".equalsIgnoreCase(leftMsg)) {
							leftMainDIv += "<div class='visionConversationalAIClass convai-left-message nonLoadedBubble'>" + leftMsg
									+ "</div>";
						}
						if (rightBtn != null && !"".equalsIgnoreCase(rightBtn)) {
							rightMainDIv += "<button class='visionConversationalAIClass convai-left-message-button nonLoadedBubble' onclick=\""+ rightBtnMtd + "\">" + rightBtn + "</button>";
						}
						if (leftBtn != null && !"".equalsIgnoreCase(leftBtn)) {
							leftMainDIv += "<button class='visionConversationalAIClass convai-left-message-button nonLoadedBubble' onclick=\""+ leftBtnMtd +   "\">" + leftBtn + "</button>";
						}
						jsonData.put("rightNxtMtd", rightNxtMtd); 
						jsonData.put("leftNxtMtd", leftNxtMtd);
						jsonData.put("replyId", repliedId);
						if(i == listData.size()-1)
						{
							rightMainDIv += "</div>";
							leftMainDIv += "</div>";
						}
					}
				}
				mainDiv += leftMainDIv+rightMainDIv;
				mainDiv += "</div>";
			}
			mainDiv += "</div>";
			jsonData.put("mainDiv", mainDiv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonData;
	}
	
	@Transactional
	public JSONObject getUserTableNames(HttpServletRequest request) {          
		JSONObject resultObj = new JSONObject(); 
		try {
			String tableDiv = "";
			String userName = (String) request.getParameter("userName");
			String replyId = (String) request.getParameter("replyId");
			if (userName != null && !"".equalsIgnoreCase(userName)) {
				String fetchQuery = "SELECT TABLE_NAME  FROM C_ETL_DAL_AUTHORIZATION WHERE CREATE_BY =:CREATE_BY"; 
				Map mapData = new HashMap();
				mapData.put("CREATE_BY", userName);
				List listData = access.sqlqueryWithParams(fetchQuery, mapData);
				if (listData != null && !listData.isEmpty()) {
					tableDiv = "<div id='userTableNamesDivId' class='userTableNamesDivClass text-right replyIntelisenseView noBubble'>"
							//+ "<p class='nonLoadedBubble'>Existing Files/Tables</p>"
							+ "<div class=\"search nonLoadedBubble\">"
							+ "<input type=\"text\" placeholder=\"search\" id='data-search'/>"
							+ "</div>"
							+ "<div id='userIntellisenseViewTableNamesDivId' class='userIntellisenseViewTableNamesDivClass nonLoadedBubble'>";
					for (int i = 0; i < listData.size(); i++) {
						String tableName = (String) listData.get(i);
						tableDiv += "<div id='" + tableName
								+ "_table' class='userTableNameClass' onclick=getConversationalAISelectedDataTableName('"
								+ tableName + "','"+replyId+"') data-intelliSenseViewTablefilter-item data-filter-name=\""+tableName+"\">" + tableName + "</div>";      
					}
					tableDiv += "</div>" + "</div>";
				}
				resultObj.put("tableDiv", tableDiv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}
	
	@Transactional
	public JSONObject getUserMergeTableNames(HttpServletRequest request) {          
		JSONObject resultObj = new JSONObject(); 
		try {
			JSONArray tablesArr = new JSONArray();
			String tableDiv = "";
			String userName = (String) request.getParameter("userName");
			String replyId = (String) request.getParameter("replyId");
			if (userName != null && !"".equalsIgnoreCase(userName)) {
				String fetchQuery = "SELECT TABLE_NAME  FROM C_ETL_DAL_AUTHORIZATION WHERE CREATE_BY =:CREATE_BY"; 
				Map mapData = new HashMap();
				mapData.put("CREATE_BY", userName);
				List listData = access.sqlqueryWithParams(fetchQuery, mapData);
				if (listData != null && !listData.isEmpty()) {
					tableDiv = "<div id='userMergeTableNamesDivId' class='userTableNamesDivClass text-right replyIntelisenseView noBubble'>"
							  + "<div id='userIntellisenseViewMergeTableNamesDivId' class='userIntellisenseViewTableNamesDivClass nonLoadedBubble'>";
					for (int i = 0; i < listData.size(); i++) {
						String tableName = (String) listData.get(i);
						tablesArr.add(tableName);
					}
					tableDiv += "</div>" 
							+"<div id='userIntellisenseViewMergeTableNamesErrorDivId' class='userIntellisenseViewMergeTableNamesErrorDivClass'></div>"
							+"<button id='userConservationalMergeTableNamesButtonId' value='Confirm' onclick='showConversationalMergeTableNames("+replyId+")'>Ok</button>"
					+ "</div>";
				}
				resultObj.put("tableDiv", tableDiv);
				resultObj.put("tablesArr", tablesArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}
	@Transactional
	public JSONObject getUserMergeTableNamesColumns(HttpServletRequest request) {          
		JSONObject resultObj = new JSONObject(); 
		try {
			JSONArray tablesArr = new JSONArray();
			String tableDiv = "";
			Map tablesObj = new LinkedHashMap();
			String tableNames = (String) request.getParameter("tableNames");
			String replyId = (String) request.getParameter("replyId");
			if (tableNames != null && !"".equalsIgnoreCase(tableNames) && !"".equalsIgnoreCase(tableNames)) {
				tablesArr = (JSONArray) JSONValue.parse(tableNames);
				if (tablesArr != null && !tablesArr.isEmpty()) {
					tableDiv =  "<div id='userMergeTableColumnsRemoveDeleteId' class='userMergeTableColumnsRemoveDeleteClass'>"
							+ "<img src='images/delete_icon_hover.png' class='visionConversationalAiIcon' onclick='deleteFlowChartSelectedOperators()'/>"
							+ "</div>"
							+ "<div id='userMergeTableColumnsDivId' class='userTableColumnsDivClass text-right replyIntelisenseView noBubble'>";
					for (int i = 0; i < tablesArr.size(); i++) {
						String tableName = (String)tablesArr.get(i);
						String fetchQuery = "SELECT COLUMN_NAME  FROM USER_TAB_COLUMNS WHERE TABLE_NAME=:TABLE_NAME";
						Map mapData = new HashMap();
						mapData.put("TABLE_NAME", tableName);
						List listData = access.sqlqueryWithParams(fetchQuery, mapData);
						JSONObject mainInputObj = new JSONObject();
						JSONObject mainOutputObj = new JSONObject();
						if (listData != null && !listData.isEmpty()) {
							for(int j=0;j<listData.size();j++) {
								String columnName = (String)listData.get(j);
								JSONObject objData = new JSONObject();
								objData.put("label",columnName);
								//objData.put("multiple", true);
								if(i==0)
								{
									mainOutputObj.put("output_"+j, objData);
								}else if( i== (tablesArr.size()-1))
								{
									mainInputObj.put("input_"+j, objData);
								}else {
									mainInputObj.put("input_"+j, objData);
									mainOutputObj.put("output_"+j, objData);
								}
								
							}
						}
						JSONObject putsObjData = new JSONObject();
						putsObjData.put("inputs",mainInputObj);
						putsObjData.put("outputs",mainOutputObj);
						tablesObj.put(tableName, putsObjData);
					}
					tableDiv += "</div>"
							+ "<div class='userMergeTablesJoinErrorClass'>"
							        + "<input type='hidden' id='linkDynamicId' value='0'/>"
							        +"<div id='visionConvAIDefaultMapLinkColumnsId' class='visionConvAIDefaultMapLinkColumnsClass'></div>"
									+ "<button onclick='getMergeJoinCondColumns("+replyId+")' class='userMergeTablesJoinErrorButtonClass'>Next</button>"
									+ "</div>";
					resultObj.put("tableDiv", tableDiv);
					resultObj.put("tablesObj", tablesObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}
}
