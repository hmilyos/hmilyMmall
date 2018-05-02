package com.hmily.controller.backend;

import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.Category;
import com.hmily.pojo.User;
import com.hmily.service.ICategoryService;
import com.hmily.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user){
            return ServerResponse.createByErrorMessage("请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName, parentId);
        }
        else {
            return ServerResponse.createByErrorMessage("无权限操作，请登录管理员账号");
        }
    }

    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user){
            return ServerResponse.createByErrorMessage("请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }
        else {
            return ServerResponse.createByErrorMessage("无权限操作，请登录管理员账号");
        }
    }

    @RequestMapping(value = "get_children_parallel_category", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId" ,defaultValue = "0")Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user){
            return ServerResponse.createByErrorMessage("请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        else {
            return ServerResponse.createByErrorMessage("无权限操作，请登录管理员账号");
        }
    }

    @RequestMapping(value = "get_category_and_deep_children_category")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user){
            return ServerResponse.createByErrorMessage("请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        else {
            return ServerResponse.createByErrorMessage("无权限操作，请登录管理员账号");
        }
    }

}
