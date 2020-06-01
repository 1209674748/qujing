package qj.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Header;
import qj.admin.pojo.Admin;
import qj.admin.service.AdminService;
import qj.admin.util.JwtUtil;

@Controller
@RequestMapping("/adminlogin")
public class AdminLoginController {
	@Autowired
	HttpServletResponse response;
	@Autowired
	HttpServletRequest request;
	@Autowired
	AdminService adminService;
	
	@RequestMapping("/login")
	@ResponseBody
	public String login(int username,int password,HttpServletRequest request)
	{
		response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
		System.out.println("进来验证了");
		String usernameString = String.valueOf(username);
		String passwordString = String.valueOf(password);
		if(!adminService.isExited(usernameString))
		{
			return "404 该管理员用户不存在";
		}
		if(adminService.isMatched(usernameString, passwordString))
		{
			HttpSession session =request.getSession();
			session.setAttribute("isLogin", 1);
			
			String jwtToken = JwtUtil.creatJwt(JwtUtil.JWT_ID, usernameString, JwtUtil.JWT_EXPIRE);
			System.out.println("签发token");
			System.out.println(jwtToken);
			response.setHeader(JwtUtil.AUTH_HEADER, jwtToken);
			//response.setHeader("Access-Control-Expose-Headers", JwtUtil.AUTH_HEADER);
			System.out.println("成功签发token并返回");
			return "success";
		}
		else
			return "403 账号密码不匹配";
	}
}
