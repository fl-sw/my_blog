package xyz.flsw.service;

import xyz.flsw.mapper.NounEntryMapper;
import xyz.flsw.model.NounEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NounEntryService {
    @Autowired
    private NounEntryMapper nounEntryMapper;

    public List<NounEntry> findAllEntries(){
        List<NounEntry> nounEntries = nounEntryMapper.findAllEntries();
        return nounEntries;
    }

    public NounEntry findEntryByNoun(String noun)
    {
        NounEntry nounEntry = nounEntryMapper.findEntryByNoun(noun);
        return nounEntry;
    }

    public List<NounEntry> findEntriesByNounFuzzy(String word)
    {
        if(word == null)
            return null;
        word = "%"+word+"%";
        List<NounEntry> nounEntries = nounEntryMapper.findEntriesByNounFuzzy(word);
        return nounEntries;
    }

    public int deleteNounEntry(String noun)
    {
        int ret = nounEntryMapper.deleteNounEntry(noun);
        return ret;
    }

    public int insertNounEntry(NounEntry nounEntry)
    {
        int ret = nounEntryMapper.insertNounEntry(nounEntry);
        return ret;
    }

    public int updateNounEntry(NounEntry nounEntry)
    {
        int ret = nounEntryMapper.updateNounEntry(nounEntry);
        return ret;
    }
}
