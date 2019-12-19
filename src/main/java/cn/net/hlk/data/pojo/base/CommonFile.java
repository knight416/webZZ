package cn.net.hlk.data.pojo.base;

import java.sql.Timestamp;

/**
 * 【描 述】：公共文件表
 * 【表 名】：t_comm_file
 * 【环 境】：J2SE 1.8
 * @author   柴志鹏	CHAIZP@GMAIL.COM
 * @version  version 1.0
 * @since    2017年11月2日
 */
public class CommonFile {
	/** 【描 述】：主键Id */
	private int id;
	/** 【描 述】：操作表名 */
	private String tableName;
	/** 【描 述】：操作表Id */
	private int tableId;
	/** 【描 述】：图片地址Url */
	private String imageUrl;
	/** 【描 述】：操作时间 */
	private Timestamp optTime;
	/** 【描 述】：操作人 */
	private String optPerson;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Timestamp getOptTime() {
		return optTime;
	}
	public void setOptTime(Timestamp optTime) {
		this.optTime = optTime;
	}
	public String getOptPerson() {
		return optPerson;
	}
	public void setOptPerson(String optPerson) {
		this.optPerson = optPerson;
	}
	@Override
	public String toString() {
		return "CommonFile [id=" + id + ", tableName=" + tableName + ", tableId=" + tableId + ", imageUrl=" + imageUrl
				+ ", optTime=" + optTime + ", optPerson=" + optPerson + "]";
	}
	
}
