package com.talentfootpoint.talentfootpoint.controller;


import com.talentfootpoint.talentfootpoint.annotions.AppInterface;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("test")
public class Test {
    @AppInterface(name = "name",ver = "2.0.0000")
    public Map<String, String> testPush(String name, String value){
        Map<String ,String> map = new HashMap<>();
        map.put(name,value);
        return map;
    }
}
