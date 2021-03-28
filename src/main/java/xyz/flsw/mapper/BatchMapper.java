package xyz.flsw.mapper;

import xyz.flsw.model.NounEntry;
import java.util.List;

//批量操作接口
public interface BatchMapper {

    //批量插入
    public int batchInsert(List<NounEntry> nounEntries);

    //批量删除
    public int batchDelete(String word);


}
