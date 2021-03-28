package xyz.flsw.controller;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import xyz.flsw.model.NounEntry;
import xyz.flsw.service.NounEntryService;
import xyz.flsw.util.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller层
 * 其中方法均返回页面名称，且向前端传递结果集时用List<NounEntry>类型
 */
@Controller
public class NounEntryController {
    //private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private NounEntryService nounEntryService;
    private NounEntry nounEntry = new NounEntry();           //用来接收一个CURD结果（保存一个词条）
    private List<NounEntry> nounEntries = new ArrayList<>();            //保存结果集
    //private List<NounEntry> tmpNounEntries = new ArrayList<>();         //保存结果集，最后用这个list向前端传数据

    /**
     * 查询所有数据
     * @param model List<NounEntry>
     * @return name(.html)
     */
    @RequestMapping("/getAllEntires")
    //@ResponseBody
    public String getAllEntires(@RequestParam(required=false)Integer index,Model model)
    {
        //if(index == null)
        {
            //每次使用前将缓存清空
            nounEntries.clear();
            nounEntries = nounEntryService.findAllEntries();
            //int toIndex = 10 <= nounEntries.size() ? 10 : nounEntries.size();
            //tmpNounEntries = nounEntries.subList(0,toIndex);
        }
        //else if(index >= 1)
        {
            //int fromIndex = index*10;
            //int toTndex = nounEntries.size() > (1+index)*50 ? (1+index)*50 : nounEntries.size();
            //tmpNounEntries = nounEntries.subList(fromIndex,toTndex);
        }

        if(nounEntries != null)
        {
            model.addAttribute("nounEntries",nounEntries);
        }

        return "start";
    }

    /**
     * 根据前端传来的noun进行精准查询。
     *      1.当前端传来空串，则在前端展示数据库中所有noun
     *      2.当前端传来的noun匹配不上数据库中的noun，则在前端展示与用户输入相近的数据（模糊查询）
     *      3.当模糊查询也查不到，在前端展示数据库中所有noun
     * @param model List<NounEntry>
     * @return name(.html)
     */
    @RequestMapping("/findEntryByNoun")
    //@ResponseBody
    public String findEntryByNoun(@RequestParam(required=false)String noun, Model model)
    {
        nounEntries.clear();
        if(noun != null)
        {
            nounEntry = nounEntryService.findEntryByNoun(noun);
            //nounEntries = nounEntryService.findEntriesByNounFuzzy(noun);
            if(nounEntry != null)
                nounEntries.add(nounEntry);
            else
                nounEntries = nounEntryService.findEntriesByNounFuzzy(noun);
        }

        //根据前端传来的noun，即使模糊查询也无法查询到数据，则向用户展示数据库中所有数据
        if(0 == nounEntries.size())
            return "start";
            //nounEntries = nounEntryService.findAllEntries();
        model.addAttribute("nounEntries",nounEntries);
        return "start";
    }

    /**
     * 模糊查询
     * @param word 前端传来的单词，通过匹配noun的方式进行模糊查询
     * @return name(.html)
     */
    @RequestMapping("/findEntriesByNounFuzzy")
    //@ResponseBody
    public List<NounEntry> findEntriesByNounFuzzy(String word)
    {
        nounEntries.clear();
        nounEntries = nounEntryService.findEntriesByNounFuzzy(word);
        return nounEntries;
    }

    /**
     * 根据noun删除一条数据
    * @param noun
     * @param model
     * @return
     */
    @RequestMapping("/deleteNounEntry")
    //@ResponseBody
    public String deleteNounEntry(String noun,Model model)
    {
        int ret = nounEntryService.deleteNounEntry(noun);
        if(ret <= 0)
            model.addAttribute("return","delete nothing");
        model.addAttribute("return",ret+"entries delete success");
        return "start";
    }

    /**
     * 插入数据
     *      noun 字段禁止为空，其他字段允许为空
     * @param nounEntry
     * @param model
     * @return
     */
    @RequestMapping("/insertNounEntry")
    //@ResponseBody
    public String insertNounEntry(NounEntry nounEntry,Model model)
    {
        int ret = nounEntryService.insertNounEntry(nounEntry);
        if(ret <= 0) {
            if(nounEntry.getNoun() == null)
                model.addAttribute("return","noun can not be null");
            else
                model.addAttribute("return","insert failed");
        }
        else
            model.addAttribute("return","insert"+ret+"entries");
        return "insert";
    }


    /**
     * 跳到更新页面，页面中输入框内带旧数据
     * @param noun
     * @param model
     * @return
     */
    @RequestMapping("/update")
    //@ResponseBody
    public String update(String noun, Model model)
    {
        //查旧数据
        NounEntry nounEntry = nounEntryService.findEntryByNoun(noun);
        model.addAttribute("nounEntry",nounEntry);
        return "update";
    }

    /**
     * 更新数据到数据库里
     * @param nounEntry
     * @param model
     * @return
     */
    @RequestMapping("/updateNounEntry")
    //@ResponseBody
    public String updateNounEntry(NounEntry nounEntry, Model model)
    {
        int ret = nounEntryService.updateNounEntry(nounEntry);
        return "start";
    }
}
