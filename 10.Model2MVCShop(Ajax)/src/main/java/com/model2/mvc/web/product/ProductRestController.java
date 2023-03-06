package com.model2.mvc.web.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Search;
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
		// Business logic ผ๖วเ
		List<String> list = productService.getAllProdList(search);
		
		return list;
	}
	
}
