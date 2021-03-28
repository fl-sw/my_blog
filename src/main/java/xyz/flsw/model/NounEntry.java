package xyz.flsw.model;

//词条类
public class NounEntry {

    private String noun;                //专有名词
    private String detail;              //详细/全称
    private String definition;          //英文释义
    private String definition_CH;       //中文释义

    public String getNoun() {
        return noun;
    }
    public void   setNoun(String noun) {
        this.noun = noun;
    }

    public String getDetail() {
        return detail;
    }
    public void   setDetail(String detail) {
        this.detail = detail;
    }

    public String getDefinition() {
        return definition;
    }
    public void   setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinition_CH() { return definition_CH; }
    public void   setDefinition_CH(String definition_CH) {
        this.definition_CH = definition_CH;
    }

    @Override
    public String toString() {
        return "NounEntry{" +
                "noun='" + noun + '\'' +
                ", detail='" + detail + '\'' +
                ", definition='" + definition + '\'' +
                ", definition_CH='" + definition_CH + '\'' +
                '}';
    }
    public NounEntry() {
        this.noun = null;
        this.detail = null;
        this.definition = null;
        this.definition_CH = null;
    }
    public NounEntry(String noun, String detail, String definition, String definition_CH) {
        this.noun = noun;
        this.detail = detail;
        this.definition = definition;
        this.definition_CH = definition_CH;
    }
}

/*
create table nqs(
noun varchar(128) unique not null comment '名词不能重复，不可以为空',
detail TEXT comment '详细',
definition TEXT comment '释义（英文）',
definition_CH TEXT comment '释义（中文）'
)character set utf8  collation utf8_general;


insert into nqs values('IoC','Inversion of Control','Control inversion is a design principle in...','控制反转是面向对象编程中的一种设计原则，可以...');

insert into nqs values('DI','Dependency Injection','Built-in objects in their own objects are created by injection','自身对象中的内置对象是通过注入的方式进行创建');

insert into nqs values('MVC','Model View Controller','A software design paradigm that organizes code in a way that separates business logic, data, and interface display','一种软件设计典范，用一种业务逻辑、数据、界面显示分离的方法组织代码');

 */
