package xyz.flsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.flsw.model.NounEntry;
import xyz.flsw.service.BatchService;
import xyz.flsw.service.NounEntryService;
import xyz.flsw.util.Formats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FormatsController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private NounEntryService nounEntryService;      //用来调用业务方法
    @Autowired
    private BatchService batchService;
    private NounEntry nounEntry = new NounEntry();                    //用来接收一个CURD结果（保存一个词条）
    private List<NounEntry> nounEntries = new ArrayList<>();            //保存结果集，最后用这个list向前端传递数据

    @RequestMapping("/csvExport")
    //@ResponseBody
    public String csvExport(HttpServletRequest request,HttpServletResponse response)
    {
        Formats formats = new Formats();
        List<NounEntry> nounEntries = nounEntryService.findAllEntries();
        formats.setList(nounEntries);
        formats.writeCSV("result.csv");//result.csv内是数据库中全部数据的csv格式文件

        String fileName = "result.csv";//文件名
        //设置Header
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;     //读result.csv文件
        OutputStream os = null;     //向缓冲区写

        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(fileName)));

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, buffer.length);
                os.flush();
                i = bis.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "start";
    }

    @RequestMapping("/csvImport")
    //@ResponseBody
    public String csvImport(@RequestParam(required=false) MultipartFile csvFile,Model model)
    {
        //客户端传来的文件存至此路径
        String newfilePath = request.getSession().getServletContext().getRealPath("/") +csvFile.getOriginalFilename();
        File newFile = null;//新文件
        try {
            newFile = new File(newfilePath);
            if(!newFile.exists())
                newFile.createNewFile();
            // 转存文件
            csvFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //用来做service层csvImport方法的实参
        FileReader fileReader = null;
        try {
            fileReader= new FileReader(newFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fileReader == null)
            model.addAttribute("return","upload error");//文件上传错误，没有数据插入
        //如果文件是csv文件，并且文件未超过1G
        if((csvFile.getContentType().equals("application/vnd.ms-excel")) && ((csvFile.getSize()>0)&&(csvFile.getSize()<(1<<30))))
        {
            int ret = batchService.csvImport(fileReader);
            model.addAttribute("return",ret);
        }
        else
        {
            String responseReturn = ("ContentType:"+csvFile.getContentType()+" FileSize:"+csvFile.getSize());
            model.addAttribute("return",responseReturn);
        }
        return "import";
    }
}

