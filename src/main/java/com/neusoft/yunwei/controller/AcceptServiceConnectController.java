package com.neusoft.yunwei.controller;

import com.neusoft.yunwei.service.AcceptServiceConnectService;
import com.neusoft.yunwei.vo.TNorthCheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
 接收其他服务调用接口
 */
@RestController
@RequestMapping(value="/ConnectYunwei")
public class AcceptServiceConnectController {

    @Autowired
    AcceptServiceConnectService acceptServiceConnectService;
    @PostMapping(value = "/executeNorthChekTask")
    public String executeNorthChekTask(@RequestBody Map<String,Object> map){
        TNorthCheckVo vo= new TNorthCheckVo();
        Map tmp = (Map)map.get("record");
        String startTime = (String) tmp.get("startTime");
        String endTime = (String) tmp.get("endTime");
        vo.setStartTime(startTime);
        vo.setEndTime(endTime);
        String flag = acceptServiceConnectService.relaTask(vo);
        return flag;
    }
}
