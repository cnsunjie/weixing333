package wx.bean;

import java.sql.Timestamp;

public class AdminUser extends BaseBean {
	public int Id;
	public String Account;
	public String Name;
	public String Mobile;
	public int Area;
	public String Roles;
	public String Pwd;
    public String getPwd() {
        return Pwd;
    }
    public void setPwd(String pwd) {
        Pwd = pwd;
    }
    public Timestamp CreateTime;
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getAccount() {
        return Account;
    }
    public void setAccount(String account) {
        Account = account;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }
    public int getArea() {
        return Area;
    }
    public void setArea(int area) {
        Area = area;
    }
    public String getRoles() {
        return Roles;
    }
    public void setRoles(String roles) {
        Roles = roles;
    }
    public Timestamp getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }
}
