package com.example.house.controller;


import com.example.house.pojo.*;
import com.example.house.pojo.Set;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private BookService bookService;

    @Autowired
    private RoomPicService roomPicService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

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
            model.addAttribute("loginErr", "???????????????");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("loginErr", "????????????");
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
    public String toAccount(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getSession().getAttribute("id").toString());
        User user = userService.findUserById(id);
        if (user.getRole().equals("customer")){
            List<UserOrder> userOrderList = new ArrayList<>();
            List<SetOrder> setOrder = setOrderService.selectSetOrderByCustomerId(id);
            for (int i = 0; i < setOrder.size(); i++) {
                UserOrder userOrder = new UserOrder();
                userOrder.setTime(setOrder.get(i).getTime());
                userOrder.setType("????????????");
                userOrder.setTypeValue(1);
                userOrder.setId(setOrder.get(i).getId());
                if (setOrder.get(i).getStatus()==1){
                    userOrder.setStatus("?????????");
                }else {
                    userOrder.setStatus("?????????");
                }
                userOrderList.add(userOrder);
            }
            List<Appointment> appointments = appointmentService.getAppointmentListByCustomer(id);
            for (int i = 0; i < appointments.size(); i++) {
                UserOrder userOrder = new UserOrder();
                userOrder.setTime(appointments.get(i).getDate());
                userOrder.setType("???????????????");
                userOrder.setTypeValue(2);
                userOrder.setId(appointments.get(i).getAppointmentId());
                if (appointments.get(i).getStatus()==1){
                    userOrder.setStatus("?????????");
                }else {
                    userOrder.setStatus("?????????");
                }
                userOrderList.add(userOrder);
            }
            List<Book> books = bookService.selectBookByCustomerId(id);
            for (int i = 0; i < books.size(); i++) {
                UserOrder userOrder = new UserOrder();
                userOrder.setTime(books.get(i).getTime());
                userOrder.setType("????????????");
                userOrder.setTypeValue(3);
                userOrder.setId(books.get(i).getId());
                if (books.get(i).getStatus()==1){
                    userOrder.setStatus("?????????");
                }else {
                    userOrder.setStatus("?????????");
                }
                userOrderList.add(userOrder);
            }
            model.addAttribute("userOrder",userOrderList);
        }
        model.addAttribute("userRole",user.getRole());
        return "my-account";
    }

    @RequestMapping(value="/getDetial")
    public void getDetial(HttpServletResponse resp,HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int type = Integer.parseInt(request.getParameter("type"));
        try {
            JSONArray data;
            Designer designer;
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            switch (type){
                case 1:
                    System.out.println(1);
                    SetOrder setOrder = setOrderService.selectSetOrderById(id);
                    Set set = setService.findSetById(setOrder.getSetId());
                    setOrder.setSetName(set.getName());
                    System.out.println(2);
                    data = JSONArray.fromObject(setOrder);
                    respWritter.append(data.toString());
                    break;
                case 2:
                    Appointment appointment = appointmentService.getAppointmentById(id);
                    designer = designerService.findDesignerById(appointment.getDesignerId());
                    appointment.setDesignerName(designer.getName());
                    data = JSONArray.fromObject(appointment);
                    respWritter.append(data.toString());
                    break;
                case 3:
                    Book book = bookService.selectBookById(id);
                    designer = designerService.findDesignerById(book.getDesignerId());
                    book.setDesignerName(designer.getName());
                    data = JSONArray.fromObject(book);
                    respWritter.append(data.toString());
                    break;
            }
//            JSONArray data = JSONArray.fromObject(cityList);
//            resp.setCharacterEncoding("utf-8");
//            PrintWriter respWritter = resp.getWriter();
//            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/delSetOrder")
    public String delSetOrder(Model model, int id) {
        try {
            setOrderService.delSetOrder(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/delAppointment")
    public String delAppointment(Model model, int id) {
        try {
            appointmentService.deleteAppointment(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/delBook")
    public String delBook(Model model, int id) {
        try {
            bookService.deleteBook(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/editAccount")
    public String editAccount(User user, HttpServletRequest request) {
        System.out.println(user);
        userService.modifyUserMain(user);
        request.getSession().setAttribute("name",user.getName());
        request.getSession().setAttribute("email",user.getEmail());
        request.getSession().setAttribute("phone",user.getPhone());
        return "redirect:/account";
    }

    @PostMapping("/editPassword")
    public String editPassword(User user, HttpServletRequest request) {
        userService.modifyPassword(user.getId(),user.getPassword());
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
        attr.addAttribute("pageNow",pageNow);
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
        attr.addAttribute("pageNow",pageNow);
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

    @RequestMapping("/designerDetail/{id}")
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
        model.addAttribute("set",set);
        model.addAttribute("contentInfo",contents);
        model.addAttribute("configInfo",configs);
        model.addAttribute("provinces",provinces);
        return "discount";
    }

    @RequestMapping("/addDiscount")
    public String addDiscount(Model model, HttpServletResponse resp){
        return "/addDiscount.html";
    }

    @RequestMapping("/editDiscount/{id}")
    public String editCase(@PathVariable int id, Model model) {
        Set set = setService.findSetById(id);
        List<SetContent> contents = contentService.getSetContentListBySetId(id);

        List<Integer> config = new ArrayList<>();
        config.add(0);
        for (int i = 0; i < contents.size(); i++) {
            List<SetConfig> content_config = configService.getSetConfigListByContentId(contents.get(i).getContentId());
            contents.get(i).setSetConfigs(content_config);
            config.add(content_config.size());
        }

        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("set",set);
        model.addAttribute("contentInfo",contents);
        model.addAttribute("contentNum",contents.size());
        model.addAttribute("configList",config);
        model.addAttribute("provinces",provinces);
        return "editDiscount";
    }

    @PostMapping("/editDiscount")
    public String editDiscount(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        //??????????????????
        Enumeration<String> parameterNames = request.getParameterNames();
//        while(parameterNames.hasMoreElements()){
//            String name=(String)parameterNames.nextElement();
//            String value=request.getParameter(name);
//            System.out.println(name + "=" + value);
//        }

        int contentNum = Integer.parseInt(request.getParameter("contentNum"));

        //??????contentArray,??????content???config??????
        String arrayTemp = request.getParameter("contentArrays");
        String[] contentArrayString = arrayTemp.split(",");
        int[] contentArray = new int[contentArrayString.length-1];
        for (int i = 1; i < contentArrayString.length; i++) {
            contentArray[i-1] = Integer.parseInt(contentArrayString[i]);
        }

        Set set = new Set();
        set.setName(request.getParameter("name"));
        set.setDiscount(request.getParameter("discount"));
        set.setDes(request.getParameter("des"));
        set.setTime(request.getParameter("time"));
        set.setSetId(Integer.parseInt(request.getParameter("setId")));
        set.setPic(setService.findSetById(set.getSetId()).getPic());
        int setId = set.getSetId();

        //content???????????????????????????content
        SetContent[] contents = new SetContent[contentNum];
        int setContentIndex = 0;
        for (int i = 0; i < contentNum; i++) {
            SetContent setContent = new SetContent();
            setContent.setSetId(setId);
            setContent.setName(request.getParameter("content"+(i+1)+"_name"));
            //????????????id???????????????content?????????????????????????????????
            if (request.getParameter("content"+(i+1)+"_id")!=null){
                int contentId = Integer.parseInt(request.getParameter("content"+(i+1)+"_id"));
                setContent.setContentId(contentId);
                setContent.setPic(contentService.findSetContentById(contentId).getPic());
                contentService.modifySetContent(setContent);
            }
            //??????id????????????????????????content?????????content?????????????????????content
            //contentId???????????????????????????contentPic??????????????????????????????????????????
            else{
                contentService.addSetContent(setContent);
            }
            //??????contents
            contents[setContentIndex++] = setContent;
        }


//        //??????????????????
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            SimpleDateFormat sdf = null;
            while(iter.hasNext()) {
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                if (!file.isEmpty()){
                    String name = file.getName();//????????????input???id??????????????????????????????????????????
                    String fileName = file.getOriginalFilename();//??????????????????
                    String suffixName=fileName.substring(fileName.lastIndexOf("."));//??????????????????
                    File image;
                    image = new File(setPath+timeStamp+suffixName);//?????????????????????,??????????????????
                    file.transferTo(image);//????????????
                    switch (name){
                        case "pic":
                            set.setPic("/setImage/"+timeStamp+suffixName);
                            break;
                        default:
                            int contentInex = Integer.parseInt(name.substring(7, name.indexOf('_')));//??????content
                            String href = "/setImage/"+timeStamp+suffixName;
                            contents[contentInex-1].setPic(href);
                            contentService.modifySetContent(contents[contentInex-1]);
                            break;
                    }

                }
            }
        }
        setService.modifySet(set);
        for (int i = 1; i <= contentArray.length; i++) {
            int configNum = contentArray[i-1];
            for (int j = 1; j <= configNum; j++) {
                SetConfig setConfig = new SetConfig();
                setConfig.setCategory(request.getParameter("content"+i+"_config"+j+"_category"));
                setConfig.setBrand(request.getParameter("content"+i+"_config"+j+"_brand"));
                setConfig.setSetId(setId);
                setConfig.setContentId(contents[i-1].getContentId());
                if (request.getParameter("content"+i+"_config"+j+"_id")!=null){
                    setConfig.setConfigId(Integer.parseInt(request.getParameter("content"+i+"_config"+j+"_id")));
                    configService.modifySetConfig(setConfig);
                }else{
                    configService.addSetConfig(setConfig);
                }
            }
        }
        return "redirect:/editDiscount/"+set.getSetId();
    }

    @Value("${setImage.file.path}")
    private String setPath;

    @PostMapping("/saveDiscount")
    public String saveDiscount(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        //??????????????????
        Enumeration<String> parameterNames = request.getParameterNames();

        int contentNum = Integer.parseInt(request.getParameter("contentNum"));

        //??????contentArray,??????content???config??????
        String arrayTemp = request.getParameter("contentArrays");
        String[] contentArrayString = arrayTemp.split(",");
        int[] contentArray = new int[contentArrayString.length-1];
        for (int i = 1; i < contentArrayString.length; i++) {
            contentArray[i-1] = Integer.parseInt(contentArrayString[i]);
        }
        SetContent[] contents = new SetContent[contentNum];
        int setContentIndex = 0;
        Set set = new Set();
        set.setName(request.getParameter("name"));
        set.setDiscount(request.getParameter("discount"));
        set.setDes(request.getParameter("des"));
        set.setTime(request.getParameter("time"));
        setService.addSet(set);
        int setId = set.getSetId();
        //??????????????????
        //???????????????????????????????????????
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //?????? request ?????????????????????,??????????????????
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;//??????????????????request
            Iterator<String> iter = multiRequest.getFileNames();//?????????????????????
            SimpleDateFormat sdf = null;
            while(iter.hasNext()) {
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                String name = file.getName();//????????????input???id??????????????????????????????????????????
                String fileName = file.getOriginalFilename();//??????????????????
                String suffixName=fileName.substring(fileName.lastIndexOf("."));//??????????????????
                File image;
                image = new File(setPath+timeStamp+suffixName);//?????????????????????,??????????????????
                file.transferTo(image);//????????????
                SetContent setContent = new SetContent();
                int contentInex = Integer.parseInt(name.substring(7, name.indexOf('_')));//??????content
                String href = "/setImage/"+timeStamp+suffixName;
                String contentName = request.getParameter("content"+contentInex+"_name");//?????????content???name
                setContent.setPic(href);
                setContent.setName(contentName);
                setContent.setSetId(setId);
                contents[setContentIndex++] = setContent;
                contentService.addSetContent(setContent);
            }
        }

        set.setPic(contents[0].getPic());
        setService.modifySet(set);
        for (int i = 0; i < contentArray.length; i++) {
            int configNum = contentArray[i];
            for (int j = 1; j <= configNum; j++) {
                SetConfig config = new SetConfig();
                config.setSetId(setId);
                config.setContentId(contents[i].getContentId());
                config.setCategory(request.getParameter("content"+(i+1)+"_config"+j+"_category"));
                config.setBrand(request.getParameter("content"+(i+1)+"_config"+j+"_brand"));
                configService.addSetConfig(config);
            }
        }
        return "redirect:/addDiscount";
    }

    @RequestMapping("/delDiscount/{id}")
    public String delDiscount(@PathVariable int id, HttpServletRequest request){
        setService.deleteSet(id);
        String refUrl=request.getHeader("Referer").toString();
        String port = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort();
        refUrl = refUrl.substring(refUrl.indexOf(port)+port.length());
        return "redirect:"+refUrl;
    }

    @PostMapping("/discountOrderAdd")
    public String discountOderAdd(SetOrder setOrder, HttpSession session, HttpServletRequest request, Model model){
        if(setOrder!=null){
            setOrderService.addOrder(setOrder);
            Set set = setService.findSetById(setOrder.getSetId());
            String province = cityService.getCityNameById(Integer.parseInt(setOrder.getProvince()));
            String city = cityService.getCityNameById(Integer.parseInt(setOrder.getCity()));
            String contentToAdmin =  " ?????????\n" + "????????????" + set.getName() + "??????????????????"  + "\n?????????????????????:" + setOrder.getTel() +
                    "???????????????:" + setOrder.getEmail() +  "\n?????????:" + province+city+setOrder.getPosition() + "\n?????????????????????";
            SimpleMailMessage smmToAdmin = new SimpleMailMessage();
            smmToAdmin.setFrom(from);
            smmToAdmin.setSubject("??????????????????");
            smmToAdmin.setText(contentToAdmin);
            smmToAdmin.setTo(from);
            try {
                javaMailSender.send(smmToAdmin);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (setOrder.getEmail()!=null){
                String contentToCustomer =  " ?????????\n" + "????????????????????????" + set.getName() + "?????????????????????????????????????????????????????????" ;
                SimpleMailMessage smmToCustomer = new SimpleMailMessage();
                smmToCustomer.setFrom(from);
                smmToCustomer.setSubject("????????????");
                smmToCustomer.setText(contentToCustomer);
                smmToCustomer.setTo(setOrder.getEmail());
                try {
                    javaMailSender.send(smmToCustomer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
    public String tobooking(@PathVariable int id, Model model) throws ParseException {
        Designer designer=designerService.findDesignerById(id);
        List<Appointment> appointments=appointmentService.getAppointmentList();
        List<String> dateList=appointments.stream().map(e -> e.getDate()).collect(Collectors.toList());
        List<String> bookTime = bookService.selectBookTimeByDesignerId(id);

        //?????????????????????
        Date d = new Date();
        long dTime = d.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nextDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTime + 86400000))));
        String lastDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTime + 86400000*7))));

        //?????????????????????????????????
        for (int i = 0; i < dateList.size(); i++) {
            String temp = dateList.get(i).substring(0,dateList.get(i).length()-2);
            if (temp.compareTo(nextDay)<0){
                dateList.remove(i);
                i--;
            }
        }
        //?????????????????????????????????
        for (int i = 0; i < bookTime.size(); i++) {
            String temp = bookTime.get(i).substring(0,bookTime.get(i).length()-2);
            if (temp.compareTo(nextDay)<0){
                bookTime.remove(i);
                i--;
            }
        }
        for (int i = 0; i < bookTime.size(); i++) {
            dateList.add(bookTime.get(i));
        }
        //??????????????????????????????????????????????????????
        List<Schedule> scheduleAll = scheduleService.getScheduleByDesignerId(id);
        List<Schedule> schedule = new ArrayList<>();
        for (int i = 0; i < scheduleAll.size(); i++) {
            String endDay = scheduleAll.get(i).getDay().substring(scheduleAll.get(i).getDay().indexOf('-')+1);
            endDay = endDay.replace(".","-");
            String startDay = scheduleAll.get(i).getDay().substring(0,scheduleAll.get(i).getDay().indexOf('-'));
            startDay = startDay.replace(".","-");
            if (endDay.compareTo(nextDay)>0 && startDay.compareTo(lastDay)< 0){
                schedule.add(scheduleAll.get(i));
            }
        }

        List<List<String>> dayTime = new ArrayList<>();//???????????????
        List<List<String>> day = new ArrayList<>();//??????
        List<List<String>> time = new ArrayList<>();//??????
        for (int i = 0; i < schedule.size(); i++) {
            String scheduleDayTime = schedule.get(i).getDate();
            String startDayTime = scheduleDayTime.substring(0,scheduleDayTime.indexOf("-"));
            String endDayTime = scheduleDayTime.substring(scheduleDayTime.indexOf("-")+1);
            List<String> dayTimeTemp = new ArrayList<>();
            dayTimeTemp.add(startDayTime);
            dayTimeTemp.add(endDayTime);
            dayTime.add(dayTimeTemp);
            String scheduleDay = schedule.get(i).getDay();
            String startDay = scheduleDay.substring(0,scheduleDay.indexOf("-"));
            String endDay = scheduleDay.substring(scheduleDay.indexOf("-")+1);
            List<String> dayTemp = new ArrayList<>();
            dayTemp.add(startDay.replace(".","-"));
            dayTemp.add(endDay.replace(".","-"));
            day.add(dayTemp);
            String scheduleTime = schedule.get(i).getTime();
            String startTime = scheduleTime.substring(0,scheduleTime.indexOf(","));
            String endTime = scheduleTime.substring(scheduleTime.indexOf(",")+1);
            List<String> timeTemp = new ArrayList<>();
            timeTemp.add(startTime);
            timeTemp.add(endTime);
            time.add(timeTemp);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(day.get(i).get(0));
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(day.get(i).get(1));
            Calendar dd = Calendar.getInstance();//??????????????????
            dd.setTime(d1);
            String startTime = time.get(i).get(0);
            String endTime = time.get(i).get(1);
            if (startTime.equals("AM")){
                result.add(sdf.format(d1)+"AM");
                result.add(sdf.format(d1)+"PM");
            }
            else if (startTime.equals("PM")){
                result.add(sdf.format(d1)+"PM");
            }

            while (dd.getTime().before(d2)) {//???????????????????????????
                dd.add(Calendar.DATE, 1);//?????????????????????1
                String str = sdf.format(dd.getTime());
                result.add(str+"AM");
                result.add(str+"PM");
            }
            if (endTime.equals("AM")){
                result.remove(result.size()-1);
            }
        }
        for (int i = 0; i < result.size(); i++) {
            dateList.add(result.get(i));
        }
        model.addAttribute("designerInfo",designer);
        model.addAttribute("appointmentInfo",appointments);
        model.addAttribute("dateListInfo",dateList);
        return "booking";
    }

    @PostMapping("/appointmentAdd")
    public String appointmentAdd(Appointment appointment, HttpSession session, HttpServletRequest request, Model model){
        if(appointment!=null){
            appointmentService.addAppointment(appointment);
            Designer designer = designerService.findDesignerById(appointment.getDesignerId());
            List<Appointment> appointments=appointmentService.getAppointmentList();
            List dateList=appointments.stream().map(e -> e.getDate()).collect(Collectors.toList());
            model.addAttribute("appointmentInfo",appointments);
            model.addAttribute("dateListInfo",dateList);
            String contentToDesigner = "????????? " + designer.getName() + " ?????????\n" + "?????????????????????????????????" + "\n?????????:" + appointment.getDate() +
                    "\n???????????????:" + appointment.getLocation() + "\n?????????????????????:" + appointment.getCustomerTel() + "\n?????????????????????";
            String contentToCustomer = "??????????????? " + appointment.getCustomerName() + " ?????????\n" + "??????????????????????????? " + designer.getName() + "\n?????????:" + appointment.getDate() +
                    "\n???????????????:" + appointment.getLocation() + "\n????????????????????????:" + designer.getTel() + "\n?????????????????????";
            //?????????????????????????????????
            SimpleMailMessage smmTodesigner = new SimpleMailMessage();
            //?????????????????????
            smmTodesigner.setFrom(from);
            //????????????
            smmTodesigner.setSubject("??????????????????");
            //????????????
            smmTodesigner.setText(contentToDesigner);
            //????????????
            smmTodesigner.setTo(designer.getEmail());
            try {
                javaMailSender.send(smmTodesigner);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (appointment.getCustomerEmail()!=null){
                SimpleMailMessage smmToCustomer = new SimpleMailMessage();
                smmToCustomer.setFrom(from);
                smmToCustomer.setSubject("??????????????????");
                smmToCustomer.setText(contentToCustomer);
                smmToCustomer.setTo(appointment.getCustomerEmail());
                try {
                    javaMailSender.send(smmToCustomer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

        //???????????????????????????
        ArrayList<String> month = new ArrayList<>();
        Date d = new Date();
        long dTime = d.getTime();
        long dTimesMonth[] = new long[31];
        for (int i = 0; i < dTimesMonth.length; i++) {
            if (i == 0) {
                dTimesMonth[i] = dTime + 86400000;
            } else {
                dTimesMonth[i] = dTimesMonth[i - 1] + 86400000;
            }
        }
        //????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        String today = sdf.format(new Date(Long.parseLong(String.valueOf(dTime))));
        //?????????????????????????????????????????????????????????month
        for (int i = 0; i < dTimesMonth.length; i++) {
            month.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[i]))))+"AM");
            month.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[i]))))+"PM");
        }

        model.addAttribute("designers",designers);
        model.addAttribute("workers",workers);
        model.addAttribute("workerTypes",workerTypes);
        model.addAttribute("provinces",provinces);
        model.addAttribute("dateList",month);
        return "bookOnline.html";
    }

    @ResponseBody
    @PostMapping("/addBook")
    public void addBook(Book book, HttpServletResponse resp) throws IOException {
        try {
            book.setStatus(0);
            bookService.addBook(book);
            String province = cityService.getCityNameById(book.getProvince());
            String city = cityService.getCityNameById(book.getCity());
            //????????????????????????????????????
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append("200");
            if (book.getDesignerId()!=0){
                Designer designer = designerService.findDesignerById(book.getDesignerId());
                String contentToDesigner = "????????? " + "?????????\n" + "?????????????????????????????????" + "\n?????????:" + book.getTime() + "\n?????????????????????:" + book.getName() +
                        "???????????????:" + book.getEmail() +  "\n?????????:" + province+city+book.getLocation() + "\n?????????????????????";
                SimpleMailMessage smmToDesigner = new SimpleMailMessage();
                smmToDesigner.setFrom(from);
                smmToDesigner.setSubject("??????????????????");
                smmToDesigner.setText(contentToDesigner);
                smmToDesigner.setTo(designer.getEmail());
                try {
                    javaMailSender.send(smmToDesigner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                String contentToDesigner = "????????? " + "?????????\n" + "???????????????????????????????????????????????????????????????" ;
                SimpleMailMessage smmToAdmin = new SimpleMailMessage();
                smmToAdmin.setFrom(from);
                smmToAdmin.setSubject("??????????????????");
                smmToAdmin.setText(contentToDesigner);
                smmToAdmin.setTo(from);
                try {
                    javaMailSender.send(smmToAdmin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (book.getEmail()!=null){
                String contentToCustomer =  " ?????????\n" + "??????????????????" + "\n?????????:" + book.getTime() + "\n?????????????????????";
                SimpleMailMessage smmToCustomer = new SimpleMailMessage();
                smmToCustomer.setFrom(from);
                smmToCustomer.setSubject("????????????");
                smmToCustomer.setText(contentToCustomer);
                smmToCustomer.setTo(book.getEmail());
                try {
                    javaMailSender.send(smmToCustomer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append("400");
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????????????????
     * @param resp
     * @param id
     */
    @RequestMapping(value="/queryTime")
    public void queryTime(HttpServletResponse resp, int id, String date) throws ParseException, IOException {
        Designer designer=designerService.findDesignerById(id);
        List<Appointment> appointments=appointmentService.getAppointmentList();
        List<String> dateList=appointments.stream().map(e -> e.getDate()).collect(Collectors.toList());
        List<String> bookTime = bookService.selectBookTimeByDesignerId(id);
        //?????????????????????
        Date d = new Date();
        long dTime = d.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long dTimesMonth[] = new long[31];//?????????????????????????????????Date
        String nextDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTime + 86400000))));
        for (int i = 0; i < dTimesMonth.length; i++) {
            if (i == 0) {
                dTimesMonth[i] = dTime + 86400000;
            } else {
                dTimesMonth[i] = dTimesMonth[i - 1] + 86400000;
            }
        }
        String lastDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[30]))));

        //?????????????????????????????????
        for (int i = 0; i < dateList.size(); i++) {
            String temp = dateList.get(i).substring(0,dateList.get(i).length()-2);
            if (temp.compareTo(nextDay)<0){
                dateList.remove(i);
                i--;
            }
        }
        //?????????????????????????????????
        for (int i = 0; i < bookTime.size(); i++) {
            String temp = bookTime.get(i).substring(0,bookTime.get(i).length()-2);
            if (temp.compareTo(nextDay)<0){
                bookTime.remove(i);
                i--;
            }
        }
        for (int i = 0; i < bookTime.size(); i++) {
            dateList.add(bookTime.get(i));
        }
        for (int i = 0; i < dateList.size(); i++) {
            dateList.set(i,dateList.get(i).replace("-","."));
        }
        //??????????????????????????????????????????????????????
        List<Schedule> scheduleAll = scheduleService.getScheduleByDesignerId(id);
        List<Schedule> schedule = new ArrayList<>();
        for (int i = 0; i < scheduleAll.size(); i++) {
            String endDay = scheduleAll.get(i).getDay().substring(scheduleAll.get(i).getDay().indexOf('-')+1);
            endDay = endDay.replace(".","-");
            String startDay = scheduleAll.get(i).getDay().substring(0,scheduleAll.get(i).getDay().indexOf('-'));
            startDay = startDay.replace(".","-");
            if (endDay.compareTo(nextDay)>0 && startDay.compareTo(lastDay)< 0){
                schedule.add(scheduleAll.get(i));
            }
        }

        List<List<String>> dayTime = new ArrayList<>();//???????????????
        List<List<String>> day = new ArrayList<>();//??????
        List<List<String>> time = new ArrayList<>();//??????
        for (int i = 0; i < schedule.size(); i++) {
            String scheduleDayTime = schedule.get(i).getDate();
            String startDayTime = scheduleDayTime.substring(0,scheduleDayTime.indexOf("-"));
            String endDayTime = scheduleDayTime.substring(scheduleDayTime.indexOf("-")+1);
            List<String> dayTimeTemp = new ArrayList<>();
            dayTimeTemp.add(startDayTime);
            dayTimeTemp.add(endDayTime);
            dayTime.add(dayTimeTemp);
            String scheduleDay = schedule.get(i).getDay();
            String startDay = scheduleDay.substring(0,scheduleDay.indexOf("-"));
            String endDay = scheduleDay.substring(scheduleDay.indexOf("-")+1);
            List<String> dayTemp = new ArrayList<>();
            dayTemp.add(startDay.replace(".","-"));
            dayTemp.add(endDay.replace(".","-"));
            day.add(dayTemp);
            String scheduleTime = schedule.get(i).getTime();
            String startTime = scheduleTime.substring(0,scheduleTime.indexOf(","));
            String endTime = scheduleTime.substring(scheduleTime.indexOf(",")+1);
            List<String> timeTemp = new ArrayList<>();
            timeTemp.add(startTime);
            timeTemp.add(endTime);
            time.add(timeTemp);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(day.get(i).get(0));
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(day.get(i).get(1));
            Calendar dd = Calendar.getInstance();//??????????????????
            dd.setTime(d1);
            String startTime = time.get(i).get(0);
            String endTime = time.get(i).get(1);
            if (startTime.equals("AM")){
                result.add(sdf.format(d1)+"AM");
                result.add(sdf.format(d1)+"PM");
            }
            else if (startTime.equals("PM")){
                result.add(sdf.format(d1)+"PM");
            }

            while (dd.getTime().before(d2)) {//???????????????????????????
                dd.add(Calendar.DATE, 1);//?????????????????????1
                String str = sdf.format(dd.getTime());
                result.add(str+"AM");
                result.add(str+"PM");
            }
            if (endTime.equals("AM")){
                result.remove(result.size()-1);
            }
        }
        for (int i = 0; i < result.size(); i++) {
            dateList.add(result.get(i).replace("-","."));
        }
        HashSet hash = new HashSet(dateList);
        dateList.clear();
        dateList.addAll(hash);
        Collections.sort(dateList);
        List<String> list = new ArrayList<>(Arrays.asList(date.split(", ")));
        //?????????????????????
        list.removeAll(dateList);
        JSONArray data = JSONArray.fromObject(list);
        resp.setCharacterEncoding("utf-8");
        PrintWriter respWritter = resp.getWriter();
        respWritter.append(data.toString());
    }

    /**
     * ??????????????????
     * @param resp
     */
    @RequestMapping(value="/queryAllWorkers")
    public void query(HttpServletResponse resp) {
        try {
            /*list???????????????????????????student??????*/
            List<Worker> workers = workerService.getWorkerList();
            /*???list???????????????json??????*/
            JSONArray data = JSONArray.fromObject(workers);
            //?????????????????????
            /*???????????????????????????????????????*/
            resp.setCharacterEncoding("utf-8");
            /*???????????????*/
            PrintWriter respWritter = resp.getWriter();
            /*???JSON???????????????toString()?????????*/
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????????????????
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
     * ???????????????????????????
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
    /**
     * ????????????????????????
     * @param resp
     * @param id
     */
    @RequestMapping(value="/queryCityNow/{id}")
    public void queryCityNow(HttpServletResponse resp,@PathVariable int id) {
        try {
            City cityNow = cityService.getCityListById(id);
            City province = cityService.getCityListById(cityNow.getPid());
            JSONArray data = JSONArray.fromObject(province);
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/contactEmail")
    public String contactEmail(HttpServletRequest request, Model model, HttpServletResponse resp){
//        Enumeration<String> parameterNames = request.getParameterNames();
        String name = request.getParameter("name");
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        String content = "?????????????????????" + name + "????????????\n" + "?????????????????????:" + tel + "\n???????????????:" + email +
                "\n???????????????:" + message + "\n???????????????!";
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(from);
        smm.setSubject("????????????");
        smm.setText(content);
        smm.setTo(from);
        try {
            javaMailSender.send(smm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "contact.html";
    }

}
