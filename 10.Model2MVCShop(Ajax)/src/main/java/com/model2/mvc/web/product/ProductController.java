package com.model2.mvc.web.product;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.comment.CommentService;
import com.model2.mvc.service.comment.impl.CommentServiceImpl;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/prod/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("commentServiceImpl")
	private CommentService commentService;
	
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@RequestMapping("addProduct")
	public String addProduct(@ModelAttribute("vo") Product prod,
			@RequestParam("files") List<MultipartFile> fileList) throws Exception{
		System.out.println(prod);

		String temDir = "C:\\workspace\\09.Model2MVCShop(jQuery)\\src\\main\\webapp\\images\\uploadFiles";
		String prodTemp = "";
		long listSize = fileList.size();
		int temp = 1;
		
		for(MultipartFile mf : fileList) {
			String originalFileName = mf.getOriginalFilename();
			long size = mf.getSize();
			
			System.out.println("originalFileName : "+ originalFileName);
			System.out.println("fileSize : " + size);
			
			String uniqueFileName = System.currentTimeMillis()+"_"+originalFileName; 
			prodTemp = prodTemp +uniqueFileName+ ((temp < listSize) ? "," : "");
			temp++;
			try {
				mf.transferTo(new File((temDir)+"\\"+uniqueFileName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		prod.setFileName(prodTemp);
		prod.setProTranCode("0");
		System.out.println(prod);
		if(productService.addProduct(prod) != null) {
			System.out.println("상품등록 성공");
		}else {
			System.out.println("상품등록 실패");
		}
		
		return "forward:/product/addProduct.jsp";
	}

	
	@RequestMapping("listProduct")
	public String listProduct(@ModelAttribute("search") Search search ,@RequestParam(value="currentPage", required = false) Integer currentPage
			,@RequestParam("menu") String menu, HttpServletRequest req) throws Exception{
		if(currentPage == null) {
			currentPage = 1;
		}
		search.setCurrentPage(currentPage);
		search.setPageSize(pageSize);
		
		Map<String,Object> map= productService.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(),((Integer)map.get("totalCount")).intValue()
				,pageUnit,pageSize);
		
		req.setAttribute("resultPage", resultPage);
		req.setAttribute("list", map.get("list"));
		
		return "forward:/product/listProduct.jsp?menu="+menu;
	}
	
	@RequestMapping("getProduct")
	public String getProduct(@ModelAttribute("search") Search search,@RequestParam("productNo") int prodNo
			,@RequestParam(name="currentPage", required = false) Integer currentPage,@RequestParam("menu") String menu, HttpServletRequest req) throws Exception{
		System.out.println("getProduct start...");
		Product vo = productService.getProduct(prodNo);
		System.out.println(currentPage);
		if(currentPage == null) {
			currentPage = 1;
		}
		System.out.println(currentPage);
		if(search == null) {
			search = new Search();
		}
		search.setCurrentPage(currentPage.intValue());
		search.setPageSize(pageSize);
		Map<String, Object> map = commentService.getCommentList(search, prodNo);
		Page resultPage = new Page(currentPage, ((Integer)map.get("totalCount")).intValue(),
				pageUnit,pageSize);
		System.out.println(resultPage);
		String[] fileList = vo.getFileName().split("[,]");
		req.setAttribute("resultPage", resultPage);
		req.setAttribute("list", map.get("list"));
		req.setAttribute("vo", vo);
		req.setAttribute("fileList", fileList);
		
		if(menu.equals("manage")) {
			return "forward:updateProduct";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@GetMapping("updateProduct")
	public String updateProductView(@RequestParam("productNo") int prodNo,@RequestParam("menu") String menu
			,HttpServletRequest req) {
		System.out.println(prodNo);
		System.out.println("updateProductView start...");
		Product product = (Product)req.getAttribute("vo");
		
		req.setAttribute("product", product);
		System.out.println(menu);
		req.setAttribute("menu", menu);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@PostMapping("updateProduct")
	public String updateProduct(@ModelAttribute("vo") Product product,@RequestParam("prodNo") int prodNo,
			@RequestParam("menu") String menu,@RequestParam("file") MultipartFile file ) throws Exception {
		product.setProdNo(prodNo);
		String fileName = file.getOriginalFilename();//파일명 가져오기
		long size = file.getSize();
		String temDir = "C:\\\\workspace\\\\09.Model2MVCShop(jQuery)\\\\src\\\\main\\\\webapp\\\\images\\\\uploadFiles";
		
		System.out.println(fileName);
		System.out.println(size);
		
		String uniqueFileName = System.currentTimeMillis()+"_"+fileName;
		
		File saveFile = new File((temDir)+"\\"+uniqueFileName);
		try {
			file.transferTo(saveFile);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		product.setFileName(uniqueFileName);
		
		productService.updateProduct(product);
		
		return "forward:/product/getProduct.jsp?productNo="+prodNo+"&menu="+menu;
	}
	

}
