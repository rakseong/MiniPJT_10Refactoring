package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

@Controller
@RequestMapping("/prch/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@GetMapping("addPurchase")
	public ModelAndView addPurchaseView(@RequestParam("prod_no") int prodNo) throws Exception{
		System.out.println("addPurchaseView start...");
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchase);
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		System.out.println("addPurchaseView end...");
		return modelAndView;
	}
	
	@PostMapping("addPurchase")
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase,
									@RequestParam("prodNo") int prodNo,
									@RequestParam("buyerId") String buyerId) throws Exception{
		purchase.setTranCode("1");
		purchase.setBuyer(userService.getUser(buyerId));
		purchase.setPurchaseProd(productService.getProduct(prodNo));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		modelAndView.addObject("purchase",purchaseService.addPurchase(purchase));
		
		return modelAndView;
	}
	
	@RequestMapping("getPurchase")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		return modelAndView;
	}
	
	@RequestMapping("listPurchase")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search ,@RequestParam(value="currentPage", required = false) Integer currentPage,
			HttpSession session) throws Exception{
		if(currentPage == null) {
			currentPage =1;
		}
		search.setCurrentPage(currentPage);
		search.setPageSize(pageSize);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, ((User)session.getAttribute("user")).getUserId());
		
		Page resultPage = new Page(search.getCurrentPage(),((Integer)map.get("totalCount")).intValue()
				,pageUnit,pageSize);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resultPage",resultPage);
		modelAndView.addObject("list", map.get("list"));
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		return modelAndView;
	}
	
	@GetMapping("updatePurchase")
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("forward:/purchase/updatePurchase.jsp");
		
		return modelAndView;
	}
	
	@PostMapping("updatePurchase")
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase
			, @RequestParam("buyerId") String buyerId) throws Exception{
		
		purchase.setBuyer(userService.getUser(buyerId));
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("tranNo",purchase.getTranNo());
		modelAndView.setViewName("forward:/prch/getPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping("updateTranCode")
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase) throws Exception {
		
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/prch/listPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping("updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@ModelAttribute("purchase") Purchase purchase,
			@RequestParam("prodNo") int prodNo) throws Exception {
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchaseService.updateTranCodeByProd(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("menu","manage");
		modelAndView.setViewName("forward:/prch/listProduct");
		
		return modelAndView;
	}

}
