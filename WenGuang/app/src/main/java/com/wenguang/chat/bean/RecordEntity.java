package com.wenguang.chat.bean;
/**@类名: RecordEntity
* @功能描述: 通话记录bean类
* @作者:chepan
* @时间: 2016/11/18
* @版权申明:陈攀
* @最后修改者:
* @最后修改内容:
*/
public class RecordEntity {
    /**
     * 名字
     */
    private String name;
    /**
     * 电话号码
     */
    private String number;
    /**
     * 类型
     */
    private int type;
    /**
     * 时间
     */
    private long lDate;
    private long duration;
    private int _new;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getlDate() {
        return lDate;
    }

    public void setlDate(long lDate) {
        this.lDate = lDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int get_new() {
        return _new;
    }

    public void set_new(int _new) {
        this._new = _new;
    }
}