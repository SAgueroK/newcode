package com.domain;

public class Page {
    //当前页
    private int current=1;
    private int limit=10;
    //总行数
    private int rows;
    //查询路径
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1)
            this.current = current;

    }

    public int getLimit() {
        return limit;

    }
    /*
    * 获取起始页码
    * */
    public int getFrom(){
        int from=current-2;
        return from<1?1:from;
    }
    //获取终止页
    public int getTo(){
        int to=current+2;
        int total=getTotal();
        return to>total?total:to;
    }
    public void setLimit(int limit) {
        if(limit>=1&&limit<=100){
            this.limit= limit;
        }
    }
    //返回当前页的起始行
    public int getoffset(){
        return (current-1)*limit;
    }
    //返回总页数
    public int getTotal(){
        if(rows%limit==0){
            return rows/limit;
        }
        else{
            return rows/limit+1;
        }
    }
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0)
            this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
