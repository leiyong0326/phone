package com.base.generate.project;

import com.base.generate.BaseGenerate;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysAccount;
/**
 * 生成js代码
 * @author LeiYong
 *
 */
public class GenerateHtml extends BaseGenerate{
	private static String columns[] = ReflectionUtil.getFields(SysAccount.class).split(",");
	private static boolean checkbox = true;
	private static String pkColumn = "pk";//主键字段,主键字段必须匹配,否则格式不一致
	private static String templateFilterColumns = "serialVersionUID,ip,os,browser";//不生成代码的字段
	private static String formTemplateFilterColumns = templateFilterColumns;//不生成代码的字段
	private static String addOptionFilterColumns = templateFilterColumns;//不生成代码的字段
	private static String dataFormatFilterColumns = templateFilterColumns;//不生成代码的字段
	public static void main(String[] args) {
		System.out.println("Totol is:"+(columns.length-templateFilterColumns.split(",").length));
		generateTemplateHead();//生成html模版Head
		generateTemplate();//生成html模版
		generateFormTemplate();//生成form表单
		generateAddOption();
		generateDataFormater();
	}
	/**
	 * 生成html模版Head
	 */
	private static void generateTemplateHead(){
		StringBuffer sb = new StringBuffer("*********template head**********"+RT_1);
		for (int i = 0; i < columns.length; i++) {
			String c = columns[i];
			if (!StringUtil.inStrings(c, templateFilterColumns)) {
				sb.append(BLANK_2+"<div class='cy-th'>"+c+"</div>"+RT_1);
			}else if (pkColumn.equals(c)&&checkbox) {
				sb.insert(0,BLANK_4+"<div class='cy-th'>选中</div>"+RT_1);
			}
		}
		System.out.println(sb.toString());
	}
	/**
	 * 生成html模版
	 */
	private static void generateTemplate(){
		StringBuffer sb = new StringBuffer("*********template**********"+RT_1);
		sb.append(BLANK_1+"<div class='cy-tr'>"+RT_1);
		if (checkbox) {
			sb.append(BLANK_2+"<div class='cy-td'>"+RT_1);
			sb.append(BLANK_3+"<label class='cy-checkbox' data-id='${"+pkColumn+"}'>"+RT_1);
			sb.append(BLANK_4+"<input type='checkbox' name='' value=''>"+RT_1);
			sb.append(BLANK_3+"</label>"+RT_1);
			sb.append(BLANK_2+"</div>"+RT_1);
		}
		for (int i = 0; i < columns.length; i++) {
			String c = columns[i];
			if (!StringUtil.inStrings(c, templateFilterColumns)) {
				sb.append(BLANK_2+"<div class='cy-td'>${"+c+"}</div>"+RT_1);
			}
		}
		sb.append(BLANK_2+"<div class='cy-td'>"+RT_1);
		sb.append(BLANK_3+"<button class='cy-btn cy-btn-green cy-btn-sm editB' data-id='${"+pkColumn+"}'>编辑</button>"+RT_1);
		sb.append(BLANK_2+"</div>"+RT_1);
		sb.append(BLANK_1+"</div>"+RT_2);
		System.out.println(sb.toString());
	}
	/**
	 * 生成html表单模版
	 */
	private static void generateFormTemplate(){
		StringBuffer sb = new StringBuffer("*********form template**********"+RT_1);
		for (int i = 0; i < columns.length; i++) {
			String c = columns[i];
			if (!StringUtil.inStrings(c, formTemplateFilterColumns)) {
				sb.append(BLANK_2+"<div class='cy-td'>"+c+"</div>"+RT_1);
			}
		}
		System.out.println(sb.toString());
	}
	/**
	 * 生成AddOption
	 */
	private static void generateAddOption(){
		StringBuffer sb = new StringBuffer("*********add option**********"+RT_1);
		sb.append(BLANK_2+"this.addOptions = {"+RT_1);
		for (int i = 0; i < columns.length; i++) {
			String c = columns[i];
			if (!StringUtil.inStrings(c, addOptionFilterColumns)) {
				sb.append(BLANK_3+c+":'',"+RT_1);
			}
		}
		sb = sb.deleteCharAt(sb.length()-3);
		sb.append(BLANK_2+"};"+RT_2);
		System.out.println(sb);
	}
	/**
	 * 生成DataFormate
	 */
	private static void generateDataFormater(){
		StringBuffer sb = new StringBuffer("*********data formater**********"+RT_1);
		sb.append(BLANK_2+"return {"+RT_1);
		for (int i = 0; i < columns.length; i++) {
			String c = columns[i];
			if (!StringUtil.inStrings(c, dataFormatFilterColumns)) {
				sb.append(BLANK_3+c+":item."+c+","+RT_1);
			}
		}
		sb = sb.deleteCharAt(sb.length()-3);
		sb.append(BLANK_2+"};"+RT_2);
		System.out.println(sb);
	}
	
}
