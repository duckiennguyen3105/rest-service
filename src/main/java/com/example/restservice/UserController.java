package com.example.restservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {




    @GetMapping("/me")
    @ResponseBody
    public Map<String,String> getUserToken() {
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient = AuthConfig.getClient();
        String token = AuthConfig.getUserAccessToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
