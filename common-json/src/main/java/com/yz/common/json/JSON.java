package com.yz.common.json;


import com.yz.common.config.Application;

/**
 * json装饰类
 *
 * @auther yangzhao
 * create by 17/10/9
 */
public final class JSON {

    public static final IJsonInterface iJsonInterface;

    static {
        int json = 1;
        if (Application.sysConfig!=null){
            json = Application.sysConfig.getJson();
        }
        switch (json){
            case 1:
                iJsonInterface = new FastJson();
                break;
            case 2:
                iJsonInterface = new Jackson();
                break;
            default:
                iJsonInterface=new FastJson();
        }
    }
}
