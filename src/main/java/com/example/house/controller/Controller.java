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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println(style);
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

    @RequestMapping("/designer/{id}")
    public String todesigner(@PathVariable int id, Model model) {
        Designer designer=designerService.findDesignerById(id);
        System.out.println(designer);
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
        model.addAttribute("setInfo",set);
        model.addAttribute("contentInfo",contents);
        model.addAttribute("contentNum",contents.size());
        model.addAttribute("configList",config);
        model.addAttribute("provinces",provinces);
        return "editDiscount";
    }

    @PostMapping("/editDiscount")
    public String editDiscount(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        //获取表单数据
        Enumeration<String> parameterNames = request.getParameterNames();
//        while(parameterNames.hasMoreElements()){
//            String name=(String)parameterNames.nextElement();
//            String value=request.getParameter(name);
//            System.out.println(name + "=" + value);
//        }

        int contentNum = Integer.parseInt(request.getParameter("contentNum"));

        //获取contentArray,每个content的config个数
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

        //content列表，用于记录每个content
        SetContent[] contents = new SetContent[contentNum];
        int setContentIndex = 0;
        for (int i = 0; i < contentNum; i++) {
            SetContent setContent = new SetContent();
            setContent.setSetId(setId);
            setContent.setName(request.getParameter("content"+(i+1)+"_name"));
            //如果存在id，则说明该content不是新增的，对其做修改
            if (request.getParameter("content"+(i+1)+"_id")!=null){
                int contentId = Integer.parseInt(request.getParameter("content"+(i+1)+"_id"));
                setContent.setContentId(contentId);
                setContent.setPic(contentService.findSetContentById(contentId).getPic());
                contentService.modifySetContent(setContent);
            }
            //如果id不存在，则说明该content为新增content，数据库中增加content
            //contentId为空，数据表自增，contentPic为空，在下方文件操作中做处理
            else{
                contentService.addSetContent(setContent);
            }
            //写入contents
            contents[setContentIndex++] = setContent;

//            SetContent setContent = new SetContent();
//            String contentName = request.getParameter("content"+i+"_name");//获取该content的name
//            setContent.setName(contentName);
//            if (request.getParameter("content"+i+"_id")!=null){
//                int contentId = Integer.parseInt(request.getParameter("content"+i+"_id"));
//                setContent.setContentId(contentId);
//                setContent.setPic(contentService.findSetContentById(contentId).getPic());
//                contentService.modifySetContent(setContent);
//            }else{
//                contentService.addSetContent(setContent);
//            }
//            contents[setContentIndex++] = setContent;
        }


//        //图片文件操作
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
                    String name = file.getName();//获取图片input的id，用于判断该图片属于哪个字段
                    String fileName = file.getOriginalFilename();//获取文件名称
                    String suffixName=fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                    File image;
                    image = new File(setPath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                    file.transferTo(image);//上传文件
                    switch (name){
                        case "pic":
                            set.setPic("/setImage/"+timeStamp+suffixName);
                            break;
                        default:
                            int contentInex = Integer.parseInt(name.substring(7, name.indexOf('_')));//所属content
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
//            int configNum = contentArray[i];
//            for (int j = 1; j <= configNum; j++) {
//                SetConfig config = new SetConfig();
//                config.setSetId(setId);
//                config.setCategory(request.getParameter("content"+(i+1)+"_config"+j+"_category"));
//                config.setBrand(request.getParameter("content"+(i+1)+"_config"+j+"_brand"));
//                config.setContentId(contents[i].getContentId());
//                if (request.getParameter("content"+(i+1)+"_config"+j+"_id")!=null){
//                    config.setConfigId(Integer.parseInt(request.getParameter("content"+(i+1)+"_config"+j+"_id")));
//                    configService.modifySetConfig(config);
//                }else{
//                    configService.addSetConfig(config);
//                }
//            }
        }
        return "redirect:/editDiscount/"+set.getSetId();
//        return "redirect:/editDiscount/14";
    }

    @Value("${setImage.file.path}")
    private String setPath;

    @PostMapping("/saveDiscount")
    public String saveDiscount(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        //获取表单数据
        Enumeration<String> parameterNames = request.getParameterNames();

        int contentNum = Integer.parseInt(request.getParameter("contentNum"));

        //获取contentArray,每个content的config个数
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
//        //图片文件操作
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            SimpleDateFormat sdf = null;
            while(iter.hasNext()) {
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                String name = file.getName();//获取图片input的id，用于判断该图片属于哪个字段
                String fileName = file.getOriginalFilename();//获取文件名称
                String suffixName=fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                File image;
                image = new File(setPath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                file.transferTo(image);//上传文件
                SetContent setContent = new SetContent();
                int contentInex = Integer.parseInt(name.substring(7, name.indexOf('_')));//所属content
                String href = "/setImage/"+timeStamp+suffixName;
                String contentName = request.getParameter("content"+contentInex+"_name");//获取该content的name
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

    @ResponseBody
    @PostMapping("/addBook")
    public void addBook(Book book, HttpServletResponse resp) throws IOException {
        try {
            bookService.addBook(book);
            //给后台传输插入成功的消息
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append("200");
        } catch (Exception e) {
            resp.setCharacterEncoding("utf-8");
            PrintWriter respWritter = resp.getWriter();
            respWritter.append("400");
            e.printStackTrace();
        }
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
    /**
     * 获取当前城市的省
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

    @RequestMapping("/fileTest")
    public String test(Model model, HttpServletResponse resp){

        return "fileTest.html";
    }
}
