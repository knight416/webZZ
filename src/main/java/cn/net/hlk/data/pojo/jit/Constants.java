package cn.net.hlk.data.pojo.jit;

import cn.net.hlk.util.ConfigUtil;

/**
 * 【描 述】：常量类
 * 【环 境】：J2SE 1.8
 * @author   leol	iliuleicom@gmail.com
 * @version  version 1.0
 * @since    2017年3月28日
 */
public final class Constants {
    public final static Integer PAGESIZE = 16;//每页显示数量
    public final static Integer STATEON = 0;
    public final static Integer STATEOFF = 1;
    public final static String DTXXLBFLIGHT = "102001";// 民航
    public final static String DTXXLBTRAIN = "102011";// 铁路
    
    
    
    /** 【描 述】：ES端口号 */
    public final static String ES_Port = ConfigUtil.getString("esPort");
    /** 【描 述】：ES集群名称 */
    public final static String ES_CLUSTER_NAME = ConfigUtil.getString("clusterName");
    /** 【描 述】：ES_IP地址 */
    public final static String ES_IP = ConfigUtil.getString("elasticsearch");
    /** 【描 述】：利剑数据索引 */
    public final static String ES_FKLJ_INDEX_NAME = ConfigUtil.getString("fkljIndexName");
    /** 【描 述】：研判分析索引 */
    public final static String ES_YPFX_INDEX_NAME = ConfigUtil.getString("ypfxIndexName");
    /** 【描 述】：研判报告文档 */
    public final static String ES_LJSJ_TYPE_NAME = ConfigUtil.getString("ljsjTypeName");
    /** 【描 述】：利剑数据文档 */
    public final static String ES_YPBG_TYPE_NAME = ConfigUtil.getString("ypbgTypeName");
    /** 【描 述】：pki网关返回身份证字段 */
    public final static String PKI_ID_NUMBER_FieldName = ConfigUtil.getString("pkiIdNumberFieldName");
    /** 【描 述】： pki网关返回身份证命名空间*/
    public final static String PKI_ID_NUMBER_NAMESPACE = ConfigUtil.getString("pkiIdNumberFieldNameSpace");
    
    
    /** 【描 述】：日志存储路径 */
    public final static String LOG_PATH = ConfigUtil.getString("logPath");
    /** 【描 述】：日志保存是否开启 true false */
    public final static String LOG_OPEN = ConfigUtil.getString("logOpen");
    
    
    
    
}
