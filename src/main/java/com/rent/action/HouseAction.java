package com.rent.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.rent.biz.HouseBiz;
import com.rent.biz.impl.HouseBizImpl;
import com.rent.entity.House;
import com.rent.entity.Page;
import com.rent.entity.Users;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class HouseAction extends ActionSupport {
    private HouseBiz houseBiz = new HouseBizImpl();
    private House house;
    private String hid;
    private Page page=new Page();
    private String pageIndex;
    private String result;
    private String inputPath;   //读取下载文件的目录
    private String fileName;   //下载问价的文件名
    private InputStream inputStream;  //读取下载文件的输入流
    private String contentType;  //下载文件的类型

    public HouseBiz getHouseBiz() {
        return houseBiz;
    }
    public void setHouseBiz(HouseBiz houseBiz) {
        this.houseBiz = houseBiz;
    }
    public String getInputPath() {
        return inputPath;
    }
    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件下载的返回流
     * @return
     * @throws FileNotFoundException
     */
    public InputStream getInputStream() throws FileNotFoundException {
        String path= ServletActionContext.getServletContext().getRealPath(inputPath+"\\"+fileName);
        return new BufferedInputStream(new FileInputStream(path));
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    private List<House> houseList;

    public List<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }


    /**
     * 到增加房屋页面
     *
     * @return
     */
    public String toAdd() {
        return "success";
    }

    /**
     * 添加房屋信息
     *
     * @return
     */
    public String add() {
        Users user = (Users) ActionContext.getContext().getSession().get("user");
        house.setUser(user);
        if (houseBiz.addHouse(house) > 0) {
            return "addSuccess";
        } else {
            return "fail";
        }
    }

//    /**
//     * 添加验证的方法
//     */
    public void validateAdd(){
        if(house==null||"".equals(house.getTitle())){
            addFieldError("title","标题不能为空");
        }
        if(house==null||house.getType().getId()==0){
            addFieldError("typeId","户型不能为空");
        }
        if(house==null||house.getFloorage()==0){
            addFieldError("floorage","房屋面积不能为空");
        }
        if(house==null||house.getPrice()==0){
            addFieldError("price","价格不能为空");
        }
        if(house==null||house.getPubdate()==null){
            addFieldError("pubdate","日期不能为空");
        }
        if(house==null||house.getStreet().getId()==0){
            addFieldError("streetId","街道不能为空");
        }
        if(house==null||"".equals(house.getContact())){
            addFieldError("contact","联系方式不能为空");
        }

    }


    /**
     * 获得用户下的房屋信息
     * @return
     */
    public String getHouseByUser(){
        Map<String, Object> session = ActionContext.getContext().getSession();
        Users user =(Users) session.get("user");
        //登录成功用此返回的对象去查找此用户下的房屋集合存放在此用户对象里面
        this.houseList=houseBiz.getHouseByUser(user);
        return "success";
    }

    /**
     * 删除房屋
     * @return
     */
    public String remove(){
        house =new House();
        house.setId(Integer.parseInt(hid));
       houseBiz.removeHouse(house);
        return "removeSuccess";
    }

    /**
     * 房屋列表页面
     * @return
     */
    public String toList(){
        return "toListSuccess";
    }

    /**
     * 分页获得信息
     * @return
     */
    public String pageHouse(){
        page.setPageSize(2);
        page.setCurrentPage(Integer.parseInt(pageIndex));
        houseBiz.getPageInfo(house,page);
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"user"});//除去HouseSet属性
        result= JSONObject.fromObject(page,config).toString();
        return SUCCESS;
    }

    /**
     * 显示房屋信息
     * @return
     */
    public String show(){
        house= houseBiz.getHouseById(Integer.parseInt(hid));
        return "successShow";
    }



}
