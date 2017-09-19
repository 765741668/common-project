package com.yz.common.core.config;

/**
 * 系统配置
 *
 * @auther yangzhao
 * create by 17/9/19
 */
public class SysConfig {

    //数据中心ID
    private int datacenterId=0;

    //机器标识ID
    private int machineId=0;

    //solr服务url
    private String solrServerUrl;

    //缓存方式
    private int cacheWay;

    public int getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(int datacenterId) {
        this.datacenterId = datacenterId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getSolrServerUrl() {
        return solrServerUrl;
    }

    public void setSolrServerUrl(String solrServerUrl) {
        this.solrServerUrl = solrServerUrl;
    }

    public int getCacheWay() {
        return cacheWay;
    }

    public void setCacheWay(int cacheWay) {
        this.cacheWay = cacheWay;
    }
}
