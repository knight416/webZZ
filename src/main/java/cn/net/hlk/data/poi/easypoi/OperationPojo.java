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
public class OperationPojo {

	@Excel(name = "报名编号", orderNum = "0",width = 32)
	private String uid;
	@Excel(name = "岗位名称", orderNum = "1")
	private String postname;
	@Excel(name = "应聘人员", orderNum = "2")
	private String createuser;
	@Excel(name = "性别", orderNum = "3")
	private String sex;
	@Excel(name = "身份证号", orderNum = "4",width = 20)
	private String id_card;
	@Excel(name = "邮箱", orderNum = "5",width = 20)
	private String email;
	@Excel(name = "手机号码", orderNum = "6",width = 20)
	private String phone;
	@Excel(name = "毕业学校", orderNum = "7")
	private String school;
	@Excel(name = "专业", orderNum = "8")
	private String major;
	@Excel(name = "学历", orderNum = "9")
	private String heightEducation;
	@Excel(name = "学历类别", orderNum = "10")
	private String degreeType;
	@Excel(name = "政治面貌", orderNum = "11")
	private String political;
	@Excel(name = "准考证号", orderNum = "12",width = 32)
	private String ticketnumber;
	@Excel(name = "考室", orderNum = "13")
	private String examinationroom;
	@Excel(name = "座位号", orderNum = "14")
	private String seatnumber;
	@Excel(name = "审核状态", orderNum = "15")
	private String state;
	@Excel(name = "缴费状态", orderNum = "16")
	private String trade_state;
	@Excel(name = "驳回原因", orderNum = "16")
	private String bohuimsg;


}
