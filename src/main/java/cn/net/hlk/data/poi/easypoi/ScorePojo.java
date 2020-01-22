package cn.net.hlk.data.poi.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author ceshi
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2020/1/2116:41
 */
@Data
public class ScorePojo {

	@Excel(name = "公告", orderNum = "0")
	private String title;
	@Excel(name = "岗位", orderNum = "1")
	private String postname;
	@Excel(name = "姓名", orderNum = "2")
	private String name;
	@Excel(name = "身份证号", orderNum = "3")
	private String id_card;
	@Excel(name = "邮箱", orderNum = "4")
	private String email;
	@Excel(name = "手机号", orderNum = "5")
	private String phone;
	@Excel(name = "性别", orderNum = "6")
	private String sex;
	@Excel(name = "籍贯", orderNum = "7")
	private String address;
	@Excel(name = "民族", orderNum = "8")
	private String nation;
	@Excel(name = "户籍地", orderNum = "9")
	private String adress;
	@Excel(name = "报名时间", orderNum = "10")
	private String posttime;
	@Excel(name = "状态", orderNum = "11")
	private String pstate;
	@Excel(name = "笔试成绩", orderNum = "12")
	private String writtenresults;
	@Excel(name = "面试成绩", orderNum = "13")
	private String interviewresults;
	@Excel(name = "uid", orderNum = "14")
	private String uid;
	@Excel(name = "post_id", orderNum = "15")
	private String post_id;


}
