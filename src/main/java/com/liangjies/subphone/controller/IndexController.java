package com.liangjies.subphone.controller;

import com.liangjies.subphone.config.WebSocketServer;
import com.liangjies.subphone.entity.Text;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class IndexController {
    @Resource
    private JdbcTemplate jdbcTemplate;


    @GetMapping("index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @GetMapping("page")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }


    @RequestMapping("/get")
    public ResponseEntity<String> get() throws IOException {
        String toUserId = "10";
        String sql = "SELECT text FROM subphone WHERE id = ?";
        // 通过jdbcTemplate查询数据库
        String text = (String) jdbcTemplate.queryForObject(
                sql, new Object[]{1}, String.class);
        WebSocketServer.sendInfo(text);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @RequestMapping(value="/submit",method= RequestMethod.POST)
    public String index(String text) throws IOException {
        if(text.equals("")){
            return "error";
        }else{
            String sql = "UPDATE subphone set text = ? WHERE id = ?";
            // 通过jdbcTemplate更新数据
            jdbcTemplate.update(sql, new Object[]{text,1});
            //return Integer.toString(count);
            WebSocketServer.sendInfo(text);
        }

        return "success";
    }
}
