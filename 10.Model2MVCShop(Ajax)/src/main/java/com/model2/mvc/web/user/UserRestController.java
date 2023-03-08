package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");
		
		//Business Logic
		return userService.getUser(userId);
	}

	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(		@RequestBody User user,
										HttpSession session ) throws Exception{
	
		System.out.println("/user/json/login : POST");
		//Business Logic
		System.out.println("::"+user);
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return dbUser;
	}
	
	@RequestMapping(value = "json/update", method = RequestMethod.POST)
	public User update(@RequestBody User user) throws Exception{
		
		System.out.println("/user/json/update : POST\n");
		System.out.println(userService.getUser(user.getUserId()));
		
		userService.updateUser(user);
		System.out.println("update 완료\n");
		User returnUser = userService.getUser(user.getUserId());

		return returnUser;
	}
	
	@RequestMapping(value = "json/add", method = RequestMethod.POST)
	public User add(@RequestBody User user) throws Exception{
		
		System.out.println("/user/json/add : POST\n");
		
		userService.addUser(user);
		System.out.println("add완료\n");
		User returnUser = userService.getUser(user.getUserId());

		return returnUser;
	}
	
	@RequestMapping(value = "json/getUserList", method = RequestMethod.POST)
	public List<User> getUserList(@RequestBody Search search) throws Exception{
		
		
		
		System.out.println("/user/json/getUserList : POST\n");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		List<User> listUser = (List<User>)map.get("list");
		return listUser;
	}
	
	@RequestMapping(value = "json/getUserListAll", method = RequestMethod.POST)
	public Map<String , Object> getUserListTotalCount(@RequestBody Search search) throws Exception{
		
		System.out.println("/user/json/getUserListAll : POST\n");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		map.put("resultPage", resultPage);
		return map;
	}
	
	@RequestMapping(value = "json/getUserListAutoComplete/{searchCondition}", method = RequestMethod.POST)
	public List getUserListAutoComplete(@PathVariable String searchCondition) throws Exception{
		
		System.out.println("/user/json/getUserListAutoComplete : POST\n");
		Search search = new Search();
		search.setSearchCondition(searchCondition);
		// Business logic 수행
		List<String> list = userService.getAllUserList(search);
		
		return list;
	}
	
	@RequestMapping(value = "json/userListScroll", method = RequestMethod.POST)
	public Map userListScroll(@RequestBody Search search) throws Exception{
		search.setPageSize(pageSize);
		
		System.out.println("/user/json/prodListScroll : POST\n");
		Map<String , Object> map = userService.getUserList(search);
		map.put("pageSize", pageSize);
		// Business logic 수행
		
		return map;
	}
	
	
}