package com.neusoft.yunwei.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 接收其他服务调用接口
 */
@RestController
@RequestMapping(value="/ConnectYunwei", method = {RequestMethod.GET,RequestMethod.POST})
public class AcceptServiceConnectController {
      @PostMapping(value = "/executeNorthChekTask")
//    @GetMapping(value = "/executeNorthChekTask")
    public String executeNorthChekTask(@RequestBody Map<String, Object> map){
        System.out.println(map.get("startTime"));
        System.out.println(map.get("endTime"));
        return "成功";
    }
}
