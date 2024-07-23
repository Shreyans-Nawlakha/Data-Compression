package com.example.compression.Controller;

import com.example.compression.Service.MemoryService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class MemController {

    @Autowired
    MemoryService memoryService;

    @PostMapping("/set")
    public String setMemCompress(@RequestBody String inputBody) {
        String response = "";
        try {
            JSONObject obj = new JSONObject(inputBody);
            String key = obj.getString("key");
            String value = obj.getString("value");
            response = memoryService.setMemCompress(key,value);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/get")
    public String getMemCompress(@RequestBody String inputKey){
        String response = "";
        try{
            JSONObject obj = new JSONObject(inputKey);
            String key = obj.getString("key");
            response = memoryService.getMemCompress(key);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
