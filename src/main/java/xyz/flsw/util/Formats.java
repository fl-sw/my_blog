package xyz.flsw.util;

import xyz.flsw.model.NounEntry;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


public class Formats{
    private String filePath;
    private FileReader fileReader;
    private List<NounEntry> list;   //存对象的链表，数据库与程序之间的桥梁

    public Formats()
    {
        this.filePath = null;
        this.fileReader = null;
        this.list = new ArrayList<>();
    }
    public Formats(String filePath)
    {
        this.filePath = filePath;
        try {
            this.fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        list = new ArrayList<>();
    }
    public Formats(FileReader fileReader)
    {
        this.fileReader = fileReader;
        this.list = new ArrayList<>();
    }

    /**
     * 将数据库中的数据导出到CSV文件.
     * 其中csv文件的路径由形参指定，也可以由类的set方法指定。
     * 在调用此函数之前，应当先取得list<NounEntry>
     * @param filePath
     */

    public void writeCSV(String filePath){
        setFilePath(filePath);
        try {
            CsvWriter csvWriter = new CsvWriter(this.filePath,',', Charset.forName("GBK"));      //用来写CSV
            String[] headers = {"noun","detail","definition","definition_CH"};         // 表头
            csvWriter.writeRecord(headers);         // 写表头

            //把对象的各字段取出来，放到字符串数组里，最后写进csv文件里
            for(int i = 0;i < list.size();i++){
                String[] content = {list.get(i).getNoun(),
                                    list.get(i).getDetail(),
                                    list.get(i).getDefinition(),
                                    list.get(i).getDefinition_CH(),
                                };
                csvWriter.writeRecord(content);     //写进csv文件
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将csv文件中的数据读出，并构造元素为Entry的list,以便向数据库传递消息
     * 当函数调用结束后，this.list中即以Entry对象的形式保存了csv文件中的数据
     */

    //读CSV文件并得到list
    public void readCSV(FileReader fileReader) {
        setFileReader(fileReader);         //设置路径
        try {
             ArrayList<String[]> csvFileList = new ArrayList<>();              //保存从CSV文件中读出的String数据
             //NounEntry tempNounEntry = new NounEntry();
             CsvReader reader = new CsvReader(this.fileReader);           //用来读CSV
             reader.readHeaders();         //读表头,丢弃之  (之后再读 是数据)
             while (reader.readRecord()) {
                 csvFileList.add(reader.getValues());        //先放到字符串list里
             }
             reader.close();

             //用读到的String数据构造一个对象，并且把这个对象放进一个list
            for (int row = 0; row < csvFileList.size(); row++) {
                if(null == csvFileList.get(row)[0])         //主键不许为空
                    return;
                NounEntry tempNounEntry = new NounEntry(  csvFileList.get(row)[0],
                                                          csvFileList.get(row)[1],
                                                          csvFileList.get(row)[2],
                                                          csvFileList.get(row)[3]);
                this.list.add(tempNounEntry);        //放进list,循环结束后这个list里放的就全是实体类对象了
            }
        } catch (IOException e) {
             e.printStackTrace();
             }
    }


    public String  getFilePath() {
        return filePath;
    }
    public void    setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<NounEntry>  getList() {
        return list;
    }
    public void             setList(List<NounEntry> list) {
        this.list = list;
    }

    public FileReader getFileReader() {
        return fileReader;
    }

    public void setFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }
}



/*


public class Formats {
    private String filePathCSV;
    private List<NounEntry> list;
    private String split;
    private String firstLine;
    public enum Status {
        BEGIN,INWORD,TRANSFER
    }

    public Formats()
    {
        filePathCSV = null;
        list = null;
        split = ",";
    }
    public Formats(String filePathCSV,List<NounEntry> list)
    {
        this.filePathCSV = filePathCSV;
        this.list = list;
        this.split = ",";
    }



    public void listToCsv(String filePathCSV) {
    }


    public void csvToList(String filePathCSV) throws IOException {

        BufferedReader  br          = new BufferedReader(new FileReader(filePathCSV));
        String          firstLine   = br.readLine();
        Status          status      = Status.BEGIN;
        String          line        = null;
        NounEntry       tmpEntry    = new NounEntry();
        String[]        arr         = new String[4];
        int             strNum      = 0;
        Stack           stack       = new Stack();
        char            c           = '\0';

        if(firstLine != null) {
            setFirstLine(firstLine);
            firstLine = null;
        }

        while ((line = br.readLine()) != null) {
            int i = 0;
            while(!isLineFeed(line,i))
            {

                c = (char) line.indexOf(i);
                if(c == ',')
                {
                    if(status == Status.BEGIN)
                    {
                        arr[strNum] = null;
                        strNum++;
                        status = Status.BEGIN;
                    }
                    else if(status == Status.TRANSFER)
                    {
                        arr[strNum]+=c;
                    }
                    i++;
                    continue;
                }
                else if(c == '"')
                {
                    if(status == Status.BEGIN)
                    {
                        stack.push('"');
                        status = Status.TRANSFER;
                        i++;
                        continue;
                    }
                    if(status == Status.TRANSFER && ((char)stack.peek() == '"'))
                    {
                        stack.pop();
                        if(((char) line.indexOf(1+i)==',') || isLineFeed(line,1+i))
                        {
                            strNum++;
                            status = Status.BEGIN;
                            i++;
                            continue;
                        }
                        arr[strNum]+=c;
                    }
                    else
                    {
                        arr[strNum]+=c;
                    }
                }
                else//(c != '"' && c != ',' )
                {
                        arr[strNum]+=c;
                }
            }//while
            if(status == Status.BEGIN)
            {
                strNum = 0;
            }
        }//while
        br.close();
    }

    private boolean isLineFeed(String line,int index)
    {
        char c = (char) line.indexOf(index);
        if(c == '\n')
            return true;
        if(c == '\r')
        {
            if(index+1<line.length() && line.indexOf(index+1) == '\n')
            //if(line.indexOf(index+1) == '\n')
                return true;
        }
        return false;
    }

    public String getFilePathCSV() {
        return filePathCSV;
    }
    public void setFilePathCSV(String filePathCSV) {
        this.filePathCSV = filePathCSV;
    }
    public List<NounEntry> getList() {
        return list;
    }
    public void setList(List<NounEntry> list) {
        this.list = list;
    }
    public String getSplit() {
        return split;
    }
    public void setSplit(String split) {
        this.split = split;
    }
    public String getFirstLine() {
        return firstLine;
    }
    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }
}




*/