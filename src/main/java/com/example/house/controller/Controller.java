package com.example.house.controller;


import com.example.house.pojo.*;
import com.example.house.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import net.sf.json.JSONArray;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
@RequestMapping("")
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private SetService setService;

    @Autowired
    private SetContentService contentService;

    @Autowired
    private SetConfigService configService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SetOrderService setOrderService;

    @ModelAttribute
    public void addAttributes(Model model) {
        List<Set> set = setService.getSetList();
        model.addAttribute("setInfo",set);
    }

    @RequestMapping(value = "/{page}",method = RequestMethod.GET)
    public String showPage(@PathVariable String page){
        return page;
    }

    @RequestMapping("/session")
    public String testSession(HttpSession session){
        session.setAttribute("sessionKey", "sessionData--->user");
        return "user";
    }

    @RequestMapping(value = "/logout")
    public String sessionOut(HttpSession session, HttpServletRequest request){
        Enumeration em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        return "redirect:/index";
    }

    @PostMapping("/dologin")
    public String dologin(User user, HttpSession session, HttpServletRequest request, Model model) {
        User u = userService.findUserByCodeAndPWD(user.getCode(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getCode(), user.getPassword());
        try {
            subject.login(token);
            token.setRememberMe(true);
            request.getSession().setAttribute("id",u.getId());
            request.getSession().setAttribute("name",u.getName());
            request.getSession().setAttribute("code",u.getCode());
            request.getSession().setAttribute("role",u.getRole());
            request.getSession().setAttribute("email",u.getEmail());
            request.getSession().setAttribute("phone",u.getPhone());
            request.getSession().setAttribute("password",u.getPassword());
            if (u.getRole().equals("admin")){
                return  "redirect:/index";
            }
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("loginErr", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("loginErr", "密码错误");
            return "login";
        }
    }

    @RequestMapping("/register")
    public String toRegister() {
        return "register";
    }

    @PostMapping("/doregister")
    public String doregister(User user) {
        if (checkCode(user.getCode())){
            user.setPhone(user.getCode());
            userService.addUser(user);
            return "redirect:/login";
        }
        return "redirect:/register";
    }

    public boolean checkCode(String code) {
        List<User> users=userService.getUserList();
        boolean flag = true;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCode().equals(code)){
                flag = false;
            }
        }
        return flag;
    }

    @RequestMapping(value="/queryCode")
    @ResponseBody
    public Map<String, Integer> queryCode(@RequestParam("code") String code) {
        List<User> users = userService.getUserList();
        Map<String, Integer> map = new HashMap<>();
        boolean flag = true;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCode().equals(code)) {
                flag = false;
            }
        }
        if (flag){
            map.put("result", 200);
        }else {
            map.put("result", 400);
        }
        return map;
    }

    @RequestMapping("/index")
    public String testSession(HttpSession session, Model model) {
        List<Designer> designers=designerService.getTopNDesigner(5);
        model.addAttribute("designerInfo",designers);
        return "index";
    }

    @RequestMapping("/account")
    public String toAccount() {
        return "my-account";
    }

    @PostMapping("/editAccount")
    public String editAccount(User user, HttpServletRequest request) {
        userService.modifyUser(user);
        request.getSession().setAttribute("name",user.getName());
        request.getSession().setAttribute("email",user.getEmail());
        request.getSession().setAttribute("phone",user.getPhone());
        return "redirect:/account";
    }

    @PostMapping("/editPassword")
    public String editPassword(User user, HttpServletRequest request) {
        userService.modifyPassword(user);
        request.getSession().setAttribute("password",user.getPassword());
        return "redirect:/account";
    }

    @RequestMapping("/fullwidth1")
    public String tofullwidth(HttpServletRequest request,
                              @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                              RedirectAttributes attr){
        String refUrl=request.getHeader("Referer").toString();
        int typeP=refUrl.indexOf("type");
        String typeT="";
        String type="";
        if(typeP!=-1){
            typeT=refUrl.substring(typeP);
            if (typeT.indexOf('&')!=-1){
                type=typeT.substring(0,typeT.indexOf('&'));
            }
            else {
                type=typeT;
            }
        }
        int styleP=refUrl.indexOf("area");
        String styleT="";
        String style="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                style=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                style=styleT;
            }
        }
        int areaP=refUrl.indexOf("area");
        String areaT="";
        String area="";
        if(areaP!=-1){
            areaT=refUrl.substring(areaP);
            if (areaT.indexOf('&')!=-1){
                area=areaT.substring(0,areaT.indexOf('&'));
            }
            else {
                area=areaT;
            }
        }
        if (style!=""){
            style=style.substring(6);
            attr.addAttribute("style",style);
            pageNow=1;
        }
        if (area!=""){
            area=area.substring(6);
            attr.addAttribute("area",area);
            pageNow=1;
        }
        if (type!=""){
            type=type.substring(6);
            attr.addAttribute("type",type);
            pageNow=1;
        }
        if(pageNow==0){
            pageNow=1;
        }
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        attr.addAttribute("pageNow",pageNow);
        attr.addAttribute("houseStyle",houseStyle);
        attr.addAttribute("houseType",houseType);
        attr.addAttribute("houseForm",houseForm);
        attr.addAttribute("houseArea",houseArea);
        return "redirect:/fullwidth";
    }

    @RequestMapping("/fullwidth")
    public String tofullwidth(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                                 @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "") String style,
                                 @RequestParam(required = false, defaultValue = "") String type,
                                 @RequestParam(required = false, defaultValue = "") String form,
                                 @RequestParam(required = false, defaultValue = "") String area,
                                 HttpServletRequest request){
        String refUrl=request.getHeader("Referer").toString();
        int typeP=refUrl.indexOf("type");
        String typeT="";
        String typeRef="";
        if(typeP!=-1){
            typeT=refUrl.substring(typeP);
            if (typeT.indexOf('&')!=-1){
                typeRef=typeT.substring(0,typeT.indexOf('&'));
            }
            else {
                typeRef=typeT;
            }
        }
        int styleP=refUrl.indexOf("style");
        String styleT="";
        String styleRef="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                styleRef=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                styleRef=styleT;
            }
        }
        int formP=refUrl.indexOf("form");
        String formT="";
        String formRef="";
        if(formP!=-1){
            formT=refUrl.substring(formP);
            if (formT.indexOf('&')!=-1){
                formRef=formT.substring(0,formT.indexOf('&'));
            }
            else {
                formRef=formT;
            }
        }
        int areaP=refUrl.indexOf("area");
        String areaT="";
        String areaRef="";
        if(areaP!=-1){
            areaT=refUrl.substring(areaP);
            if (areaT.indexOf('&')!=-1){
                areaRef=areaT.substring(0,areaT.indexOf('&'));
            }
            else {
                areaRef=areaT;
            }
        }
        if (areaRef!=""){
            areaRef=areaRef.substring(6);
        }
        if (formRef!=""){
            formRef=formRef.substring(6);
        }
        if (styleRef!=""){
            styleRef=styleRef.substring(6);
        }
        if (!area.equals(areaRef)||!type.equals(typeRef)||!style.equals(styleRef)||!form.equals(formRef)){
            pageNow=1;
        }
        PageHelper.startPage(pageNow,5);
        List<House> houses=houseService.getHouseByPageAndSTFA(pageNow,pageSize,style,type,form,area);
        PageInfo<House> housePageInfo=new PageInfo<>(houses);
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        model.addAttribute("pageInfo",housePageInfo);
        model.addAttribute("houseInfo",houses);
        model.addAttribute("houseStyle",houseStyle);
        model.addAttribute("houseType",houseType);
        model.addAttribute("houseForm",houseForm);
        model.addAttribute("houseArea",houseArea);
        return "shop-grid-fullwidth";
    }

    @RequestMapping("/designer-list1")
    public String designerlist(HttpServletRequest request,
                               @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                               RedirectAttributes attr){
        String refUrl=request.getHeader("Referer").toString();
        int levelP=refUrl.indexOf("level");
        String levelT="";
        String level="";
        if(levelP!=-1){
            levelT=refUrl.substring(levelP);
            if (levelT.indexOf('&')!=-1){
                level=levelT.substring(0,levelT.indexOf('&'));
            }
            else {
                level=levelT;
            }
        }
        int styleP=refUrl.indexOf("style");
        String styleT="";
        String style="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                style=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                style=styleT;
            }
        }
        if (style!=""){
            style=style.substring(6);
            attr.addAttribute("style",style);
            pageNow=1;
        }
        if (level!=""){
            level=level.substring(6);
            attr.addAttribute("level",level);
            pageNow=1;
        }
        if(pageNow==0){
            pageNow=1;
        }
        List<Index> levelIndex = designerService.getDesignerLevel();
        List<Index> styleIndex = designerService.getDesignerStyle();
        attr.addAttribute("pageNow",pageNow);
        attr.addAttribute("levelIndex",levelIndex);
        attr.addAttribute("styleIndex",styleIndex);
        return "redirect:/designer-list";
    }

    @RequestMapping("/designer-list")
    public String todesignerlist(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                                 @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "") String style,
                                 @RequestParam(required = false, defaultValue = "") String level,
                                 HttpServletRequest request){
        String refUrl=request.getHeader("Referer").toString();
        int levelP=refUrl.indexOf("level");
        String levelT="";
        String levelRef="";
        if(levelP!=-1){
            levelT=refUrl.substring(levelP);
            if (levelT.indexOf('&')!=-1){
                levelRef=levelT.substring(0,levelT.indexOf('&'));
            }
            else {
                levelRef=levelT;
            }
        }
        int styleP=refUrl.indexOf("style");
        String styleT="";
        String styleRef="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                styleRef=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                styleRef=styleT;
            }
        }
        if (styleRef!=""){
            styleRef=styleRef.substring(6);
        }
        if (levelRef!=""){
            levelRef=levelRef.substring(6);
        }
        if (!style.equals(styleRef)||!level.equals(levelRef)){
            pageNow=1;
        }
        PageHelper.startPage(pageNow,5);
        List<Designer> designers=designerService.getDesignerByPageAndSL(pageNow,pageSize,style,level);
        PageInfo<Designer> designerPageInfo=new PageInfo<>(designers);
        List<Index> levelIndex = designerService.getDesignerLevel();
        List<Index> styleIndex = designerService.getDesignerStyle();
        model.addAttribute("pageInfo",designerPageInfo);
        model.addAttribute("designerInfo",designers);
        model.addAttribute("levelIndex",levelIndex);
        model.addAttribute("styleIndex",styleIndex);
        return "designer-list";
    }

//    @RequestMapping("/designer-list")
//    public String todesignerlist(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
//                                 @RequestParam(required = false,defaultValue = "8") Integer pageSize, HttpServletRequest request){
//        PageHelper.startPage(pageNow,5);
//        List<Designer> designers=designerService.getDesignerListByPage(pageNow,pageSize);
//        PageInfo<Designer> designerPageInfo=new PageInfo<>(designers);
//        model.addAttribute("pageInfo",designerPageInfo);
//        model.addAttribute("designerInfo",designers);
//        return "designer-list";
//    }

//    @RequestMapping("/designer")
//    public String todesigner() {
//        return "designer.html";
//    }

    @RequestMapping("/designer/{id}")
    public String todesigner(@PathVariable int id, Model model) {
        Designer designer=designerService.findDesignerById(id);
        List<House> houses=houseService.findHouseByDesignerId(id);
        model.addAttribute("designerInfo",designer);
        model.addAttribute("houseInfo",houses);
        return "designer";
    }

    @RequestMapping("/worker-list")
    public String toworkderlist(Model model,@RequestParam(required = false, defaultValue = "") String type,
                                HttpServletRequest request) {
        List<Worker> workers=workerService.getWorkerByType(type);
        List<Index> workerTypes = workerService.getWorkerType();
        model.addAttribute("workerInfo",workers);
        model.addAttribute("workerTypes",workerTypes);
        return "worker-list";
    }

    @RequestMapping("/worker/{id}")
    public String toworker(@PathVariable int id, Model model) {
        Worker worker = workerService.findWorkerById(id);
        model.addAttribute("workerInfo",worker);
        return "worker";
    }

    @RequestMapping("/searchDesigner")
    public String searchdesigner(Model model,
                                 @RequestParam(required = false, defaultValue = "") String category,
                                 @Param("searchText") String searchText,
                                 HttpServletRequest request){
        if (category.equals("designer")){
            List<Designer> designerList = designerService.findDesignerByName(searchText);
            model.addAttribute("designerInfo",designerList);
            return "search-designer";
        }
        else{
            List<Worker> workerList = workerService.findWorkerByName(searchText);
            model.addAttribute("workerInfo",workerList);
            return "search-worker";
        }
    }

    @RequestMapping("/discount/{id}")
    public String todiscount(@PathVariable int id, Model model) {
        Set set = setService.findSetById(id);
        List<SetContent> contents = contentService.getSetContentListBySet(id);
        List<SetConfig> configs = configService.getSetConfigListBySet(id);
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("setInfo",set);
        model.addAttribute("contentInfo",contents);
        model.addAttribute("configInfo",configs);
        model.addAttribute("provinces",provinces);
        return "discount";
    }

    @PostMapping("/discountOrderAdd")
    public String discountOderAdd(SetOrder setOrder, HttpSession session, HttpServletRequest request, Model model){
        if(setOrder!=null){
            setOrderService.addOrder(setOrder);
            return "redirect:/discount/"+setOrder.getSetId();
        }
        return "discount/"+setOrder.getSetId();
    }

    @RequestMapping("/contact")
    public String tocontact() {
        return "contact.html";
    }

    @RequestMapping("/login-register")
    public String tologin() {
        return "login.html";
    }

    @RequestMapping("/booking/{id}")
    public String tobooking(@PathVariable int id, Model model) {
        Designer designer=designerService.findDesignerById(id);
        List<Appointment> appointments=appointmentService.getAppointmentList();
        List dateList=appointments.stream().map(e -> e.getDate()).collect(Collectors.toList());
        model.addAttribute("designerInfo",designer);
        model.addAttribute("appointmentInfo",appointments);
        model.addAttribute("dateListInfo",dateList);
        return "booking";
    }

    @PostMapping("/appointmentAdd")
    public String appointmentAdd(Appointment appointment, HttpSession session, HttpServletRequest request, Model model){
        if(appointment!=null){
            appointmentService.addAppointment(appointment);
            List<Appointment> appointments=appointmentService.getAppointmentList();
            List dateList=appointments.stream().map(e -> e.getDate()).collect(Collectors.toList());
            model.addAttribute("appointmentInfo",appointments);
            model.addAttribute("dateListInfo",dateList);
            return "redirect:/booking/"+appointment.getDesignerId();
        }
        return "booking/"+appointment.getDesignerId();
    }

    @RequestMapping("/bookOnline")
    public String tobookOnline(Model model, HttpServletResponse resp){
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        List<Index> workerTypes = workerService.getWorkerType();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("designers",designers);
        model.addAttribute("workers",workers);
        model.addAttribute("workerTypes",workerTypes);
        model.addAttribute("provinces",provinces);
        return "bookOnline.html";
    }

    /**
     * 获取工人列表
     * @param resp
     */
    @RequestMapping(value="/queryAllWorkers")
    public void query(HttpServletResponse resp) {
        try {
            /*list集合中存放的是好多student对象*/
            List<Worker> workers = workerService.getWorkerList();
            /*将list集合装换成json对象*/
            JSONArray data = JSONArray.fromObject(workers);
            //接下来发送数据
            /*设置编码，防止出现乱码问题*/
            resp.setCharacterEncoding("utf-8");
            /*得到输出流*/
            PrintWriter respWritter = resp.getWriter();
            /*将JSON格式的对象toString()后发送*/
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 施工人员工种列表
     * @param resp
     */
    @RequestMapping(value="/queryAllTypes")
    public void queryType(HttpServletResponse resp) {
        try {
            List<Index> workerTypes = workerService.getWorkerType();
            JSONArray data = JSONArray.fromObject(workerTypes);
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取省下地级市列表
     * @param resp
     * @param id
     */
    @RequestMapping(value="/queryCity/{id}")
    public void queryCity(HttpServletResponse resp,@PathVariable int id) {
        try {
            List<City> cityList = cityService.getCityListByPid(id);
            JSONArray data = JSONArray.fromObject(cityList);
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
