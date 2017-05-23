package com.funcaretest.domain;


import java.io.Serializable;
import java.util.ArrayList;

import com.funcaretest.util.CommandUtils;

public class TableData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int m_nCurRowCount = 0; // 一页行数
	public int m_nAllRowCount = 0; // 总共行数
	public int m_nCurPage = 0; // 当前页
	public int m_nAllPageCount = 0; // 总页数
	public int m_nPageSize = 0; // 
	public int m_nColCount = 0; // 参数名信息 一共多少个参数

	public ArrayList<String> m_arrFieldName = new ArrayList<String>(); // 字段名
	public ArrayList<ArrayList<String>> m_arrRecodes = new ArrayList<ArrayList<String>>(); // 字段名对应的字段值

	public TableData() {

	}

	public TableData(String tableData) {
		if(tableData==null || tableData.length()<1){
			return;
		}
		String[] arrTable = tableData.split(CommandUtils.m_strSep_Table);//按参数表分隔符解析得到 参数表信息 （分两部分部分   一部分：分页信息+参数名信息  二部分：多条参数值信息）
		if(arrTable==null || arrTable.length <2){
			return;
		}
		
		String[] arrHead = arrTable[0].split(CommandUtils.m_strSep_Row);//按行参数分隔符  将 分页信息+参数信息 分开 得到  分页信息、参数名信息
		if(arrHead==null || arrHead.length < 2){
			return;
		}
		String[] arrPageInfo = arrHead[0].split(CommandUtils.m_strSep_Feild);//得到分页信息
		String[] arrField = arrHead[1].split(CommandUtils.m_strSep_Feild);//得到参数名信息
		if(arrPageInfo == null || arrPageInfo.length < 4){//判断分页信息长度是否正确 （包括一页行数，总行数，当前页，总页数 共4个长度）
			return;
		}
		this.m_nCurRowCount = Integer.valueOf(arrPageInfo[0].split(CommandUtils.m_strSep_Feild)[0]);//一页行数
		this.m_nAllRowCount = Integer.valueOf(arrPageInfo[1].split(CommandUtils.m_strSep_Feild)[0]);//总共行数
		this.m_nCurPage = Integer.valueOf(arrPageInfo[2].split(CommandUtils.m_strSep_Feild)[0]);//当前页数
		this.m_nAllPageCount = Integer.valueOf(arrPageInfo[3].split(CommandUtils.m_strSep_Feild)[0]);//总页数
		this.m_nColCount = arrField.length;//参数名信息数目 一共有多少个参数
		for(int i=0;i<this.m_nColCount;i++){//得到所有参数
			this.m_arrFieldName.add(arrField[i].split(CommandUtils.m_strSep_Type)[0]);//取得每一个参数名
		}
		String[] arrRow = arrTable[1].split(CommandUtils.m_strSep_Row);//按行参数分隔符 如果有多条数据 就会被分成一条一条的数据
		for(int j=0;j<arrRow.length;j++){
			String[] recodes = arrRow[j].split(CommandUtils.m_strSep_Feild);
			ArrayList<String> recodesList = new ArrayList<String>();
			for(int i=0;i<recodes.length;i++){
				recodesList.add(recodes[i]);//取得每个参数值
			}
			if(recodes.length<this.m_arrFieldName.size()){//如果所有字段值的长度 小于 所有字段名信息长度  那就当剩下的字段名信息都给""字符串
				for(int i=recodes.length;i<this.m_arrFieldName.size();i++){
					recodesList.add("");
				}
			}
			this.m_arrRecodes.add(recodesList);//将字段值集合放入集合
		}
	}
}
