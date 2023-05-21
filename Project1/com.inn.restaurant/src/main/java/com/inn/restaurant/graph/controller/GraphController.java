package com.inn.restaurant.graph.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class GraphController {

    @GetMapping("/displayBarGraph")
    public String barGraph(Model model){
        Map<String,Integer> surveyMap=new LinkedHashMap<>();
        surveyMap.put("Python",50);
        surveyMap.put("Java",30);
        surveyMap.put(".Net",15);
        surveyMap.put("Spring",5);
        model.addAttribute("surveyMap",surveyMap);
        return "barGraph";
    }

    @GetMapping("/displayPieChart")
    public String pieChart(Model model) {
        model.addAttribute("pass", 50);
        model.addAttribute("fail", 50);
        return "pieChart";
    }
}
