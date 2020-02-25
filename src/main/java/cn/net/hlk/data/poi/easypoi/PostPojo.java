package cn.net.hlk.data.poi.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Collection;

/**
 * @author ceshi
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2020/1/2116:41
 */
@Data
public class PostPojo {

	@Excel(name = "公告", orderNum = "0")
	private String title;
	@Excel(name = "岗位", orderNum = "1")
	private String postname;
	@Excel(name = "招考单位", orderNum = "2")
	private String recruitmentunit;
	@Excel(name = "招聘人数", orderNum = "3")
	private String numberofrecruits;
	@Excel(name = "学历要求", orderNum = "4")
	private String academicrequirements;
	@Excel(name = "专业要求", orderNum = "5")
	private String professionalrequirements;
	@Excel(name = "性别要求", orderNum = "6")
	private String genderrequirements;
	@Excel(name = "最高年龄", orderNum = "7")
	private String maximumage;
	@Excel(name = "政治面貌", orderNum = "8")
	private String politicaloutlook;
	@Excel(name = "联系方式", orderNum = "9")
	private String contactinformation;
	@Excel(name = "用工性质", orderNum = "10")
	private String employmentnature;
	@Excel(name = "其他要求", orderNum = "11")
	private String otherrequirements;
	@Excel(name = "备注", orderNum = "12")
	private String remarks;
	@Excel(name = "考试方式", orderNum = "13")
	private String examinationmethod;
	@Excel(name = "考点名称", orderNum = "14")
	private String examinationname;
	@Excel(name = "考试地点", orderNum = "15")
	private String examinationplace;
	@Excel(name = "考试时间", orderNum = "16")
	private String examinationtime;
	@Excel(name = "考试房间", orderNum = "17")
	private String examinationroom;
	@Excel(name = "考试须知", orderNum = "18")
	private String examinationnotes;
	@Excel(name = "报名人数", orderNum = "19")
	private String numberofenrolment;
	@Excel(name = "岗位类型", orderNum = "20")
	private String posttype;
	@Excel(name = "岗位编号", orderNum = "21")
	private String postnumber;
	@Excel(name = "招聘条件", orderNum = "22")
	private String recruitmentconditions;
	@Excel(name = "薪酬范围", orderNum = "23")
	private String salaryrange;


}
