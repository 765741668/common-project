package com.app.enums;

/**
 * Created by yangzhao on 16/10/27.
 */
public enum UserTaskStatus {

    NOT_START("未开始",0),NOT_FINISH("未完成",1),RUNNING("进行中",2),FINISH("完成",3);

    public String name;

    public int status;

    private UserTaskStatus(String name,int status){
        this.name=name;
        this.status=status;
    }

}
