package com.model2.mvc.web.product;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RestController
@RequestMapping("/prod/*")
public class ProductRestController {

	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@RequestMapping(value = "json/getProdListAutoComplete/{searchCondition}", method = RequestMethod.POST)
	public List getUserListAutoComplete(@PathVariable String searchCondition) throws Exception{
		
		System.out.println("/user/json/getProdListAutoComplete : POST\n");
		Search search = new Search();
		search.setSearchCondition(searchCondition);
		// Business logic 수행
		List<String> list = productService.getAllProdList(search);
		
		return list;
	}
	
	@RequestMapping(value = "json/prodListScroll", method = RequestMethod.POST)
	public List prodListScroll(@RequestBody Search search, HttpServletRequest request) throws Exception{
		
		System.out.println("/user/json/getProdListAutoComplete : POST\n");
		search.setPageSize(pageSize);
		// Business logic 수행
		List<Product> list= (List)(productService.getProductList(search)).get("list");
		request.setAttribute("list", list);
		return list;
	}
	
	
	
}
