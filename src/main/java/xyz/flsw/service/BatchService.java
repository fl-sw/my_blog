package xyz.flsw.service;

import xyz.flsw.mapper.BatchMapper;
import xyz.flsw.mapper.NounEntryMapper;
import xyz.flsw.model.NounEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.flsw.util.Formats;

import java.io.FileReader;
import java.util.List;

@Service
public class BatchService {
    @Autowired
    public BatchMapper batchMapper;

    //批量插入
    public int batchInsert(List<NounEntry> nounEntries)
    {
        int ret = batchMapper.batchInsert(nounEntries);
        return ret;
    }

    //批量删除，删除关键字中含有某些字母组合的词条
    public int batchDelete(String word)
    {
        int ret = batchMapper.batchDelete(word);
        return ret;
    }

    //将数据从打开的csv文件中读取出来，写入数据库
    public int csvImport(FileReader fileReader)
    {
        int ret = 0;
        Formats formats = new Formats();
        formats.readCSV(fileReader);
        ret = batchInsert(formats.getList());
        return ret;
    }

    //将数据从数据库取出，并存至result.csv
    public void csvExport(String filePath)
    {
        Formats formats = new Formats();
        formats.writeCSV(filePath);
    }
}
