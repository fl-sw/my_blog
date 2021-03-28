package xyz.flsw.controller;

import xyz.flsw.model.NounEntry;
import xyz.flsw.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BatchController {

    @Autowired
    BatchService batchService;

    @RequestMapping("/batchInsert")
    @ResponseBody
    public int batchInsert(List<NounEntry> nounEntries)
    {
        int ret = batchService.batchInsert(nounEntries);
        return ret;
    }

    //////////////////////////////////////
    @RequestMapping("/del")
    @ResponseBody
    public int batchDelete(String word)
    {
        int ret = batchService.batchDelete(word);
        return ret;
    }
}
