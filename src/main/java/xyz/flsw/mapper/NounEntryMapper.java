package xyz.flsw.mapper;
import xyz.flsw.model.NounEntry;

import java.util.List;

//CURD
public interface NounEntryMapper {

    //查找所有词条
    public List<NounEntry> findAllEntries();//✔

    //依据名词精确查询词条
    public NounEntry findEntryByNoun(String noun);//✔

    //依据字母模糊查询词条
    public List<NounEntry> findEntriesByNounFuzzy(String word);//✔

    //依据名词更新词条
    public int updateNounEntry(NounEntry nounEntry);//？

    //依据名词删除词条
    public int deleteNounEntry(String noun);//✔

    //插入一个词条
    public int insertNounEntry(NounEntry nounEntry);//✔

}

