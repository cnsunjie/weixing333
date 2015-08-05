package wx.bean;

import java.sql.Timestamp;

public class Shop extends BaseBean {
	public int Id;
	public String Name;
	public String Contact = "";
	public String Tel = "";
	public int Area;
	public int Category;
    public Timestamp CreateTime;
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getContact() {
        return Contact;
    }
    public void setContact(String contact) {
        Contact = contact;
    }
    public String getTel() {
        return Tel;
    }
    public void setTel(String tel) {
        Tel = tel;
    }
    public int getArea() {
        return Area;
    }
    public void setArea(int area) {
        Area = area;
    }
    public int getCategory() {
        return Category;
    }
    public void setCategory(int category) {
        Category = category;
    }
    public Timestamp getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }
	
}
