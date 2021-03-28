package xyz.flsw.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class CurrencyController {
    @RequestMapping("/data")
    public String getData(@RequestParam(value="name", required=false, defaultValue="rose") String noun, Model model) {
        model.addAttribute("noun", noun);
        return "data";
    }

    @RequestMapping("/insert")
    public String getInsert() {
        return "insert";
    }

    @RequestMapping("/export")
    public String getFirst() {
        return "export";
    }

    @RequestMapping({"/import"})
    public String search() {
        return "import";
    }
    @RequestMapping({"/start","/"})
    public String start() {
        return "start";
    }

}
