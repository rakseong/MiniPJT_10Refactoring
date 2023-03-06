<%@ page contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>

<script type="text/javascript">
function fncGetList(currentPage){
	$("#currentPage").val(currentPage);
	$("form").attr("method","POST").attr("action","/prod/listProduct").submit();
// 	document.getElementById("currentPage").value = currentPage;
// 	document.detailForm.submit();
}

function fncGetOrderList(currentPage,orderStandard){
	$("#orderStandard").val(orderStandard);
// 	document.getElementById("orderStandard").value = orderStandard;
	fncGetList(currentPage);
}

$(function(){
	
	$("input[name=searchKeyword]").on('keydown',function(){
		var condition = $("select[name='searchCondition']").val()
		$.ajax( 
				{
					url : "/prod/json/getProdListAutoComplete/"+condition ,
					method : "POST" ,
					dataType : "json" ,
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					success : function(JSONData , status) {
						var list = JSONData;
						$(".ct_input_g[name=searchKeyword]").autocomplete({
							source:list
						});
					}
			}); 
	});	
	
	$(".ct_btn01:contains('검색')").on('click',function(){
		fncGetList(1);
	})
	
	$(".order td").each(function(index,elem){
		$(elem).on('click',function(){
			fncGetOrderList('1',index);
		})
	})
	
	$(".ct_list_pop:nth-child(4n+6)").css("background-color" , "whitesmoke");
	
	
	$(".ct_list_pop:contains('배송하기')").each(function(index,elem){
		var num = $($(".ct_list_pop:contains('배송하기') input")[index]).val();
		$(elem).on('click',function(){
			self.location = "/prch/updateTranCodeByProd?prodNo="+num+"&tranCode=2";
		})
	})
	
	$("td.getProd").each(function(index,elem){
		var num = $($("td.getProd input")[index]).val();
		$(elem).on('click',function(){
			self.location = "/prod/getProduct?productNo="+num+"&menu=${param.menu}";
		})
	})
	
});

</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form>

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					${(param.menu eq 'manage') ? '상품 관리' : '상품 목록 조회'}
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>

<input type="hidden" id="orderStandard" name="orderStandard" value="${search.orderStandard}"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="0" ${!empty search.searchCondition && search.searchCondition eq '0' ? "selected" : "" }>상품번호</option>
				<option value="1" ${!empty search.searchCondition && search.searchCondition eq '1' ? "selected" : "" }>상품이름</option>
			</select>
			<input type="text" name="searchKeyword" value="${search.searchKeyword}"  class="ct_input_g" style="width:200px; height:19px" />
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23"><img src="/images/ct_btnbg01.gif" width="17" height="23"></td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						검색
					</td>
					<td width="14" height="23"><img src="/images/ct_btnbg03.gif" width="14" height="23"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="20%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;" >
	<tr class="order">
		<c:if test="${user.role eq 'admin'}">
		<td>
			등록 번호순
		</td>
		</c:if>
		<td>
			낮은 가격순
		</td>
		<td>
			높은 가격순
		</td>
		<td>
			이름순
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >전체 ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<c:if test="${user.role ne 'admin' || empty user}">
		<td class="ct_list_b">제조일자</td>	
		</td>
		</c:if>
		<c:if test="${user.role eq 'admin'}">
		<td class="ct_list_b">등록일</td>	
		</td>
		</c:if>
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	<c:set var="i" value="0"/>
	<c:forEach var="product" items="${list}">
		<c:set var="i" value="${ i+1 }" />
		<tr class="ct_list_pop">
			<td align="center">${i}</td>
			<td></td>
			<c:if test="${product.proTranCode ne '0' && param.menu eq 'manage'}">
				<td align="left">
			</c:if>
			<c:if test="${product.proTranCode eq '0' || (product.proTranCode ne '0' && param.menu eq 'search')}">
				<td class="getProd" align="left">
			</c:if>
				${product.prodName}
				<input type="hidden" value="${product.prodNo}">
			</td>
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<c:if test="${user.role ne 'admin' || empty user}">
			<td align="left">${product.manuDate}
			</td>
			</c:if>
			<c:if test="${user.role eq 'admin'}">
			<td align="left">${product.regDate}
			</td>
			</c:if>
			<td></td>
			<td align="left">
			<c:set var="tranCode" value="${fn:trim(product.proTranCode)}"/>
			<c:if test="${!empty user && user.role eq 'admin'}">
				<c:if test="${tranCode eq '0'}">
					판매중
				</c:if>
				<c:if test="${tranCode eq '1'}">
					구매완료
					<c:if test="${param.menu eq 'manage'}">
						배송하기
						<input type="hidden" value="${product.prodNo}">
					</c:if>
				</c:if>
				<c:if test="${tranCode eq '2'}">
					배송중
				</c:if>
				<c:if test="${tranCode eq '3'}">
					배송완료
				</c:if>
			</c:if>
			<c:if test="${empty user || user.role ne 'admin'}">
				<c:if test="${tranCode eq '0'}">
					판매중
				</c:if>
				<c:if test="${tranCode eq '1' || tranCode eq '2' || tranCode eq '3'}">
					재고 없음
				</c:if>
			</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>
	</c:forEach>
</table>

<input type="hidden" id="menu" name="menu" value="${param.menu}"/>
<input type="hidden" id="currentPage" name="currentPage" value=""/>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td class="pageNavi" align="center">
			<jsp:include page="../common/pageNavigator.jsp"/>
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>