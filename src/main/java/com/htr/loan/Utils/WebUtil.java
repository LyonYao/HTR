package com.htr.loan.Utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebUtil {

	private static String SORT_ASC="ASC";
	@SuppressWarnings("unused")
	private static String SORT_DESC="DESC";

	private static ObjectMapper mapper = new ObjectMapper();

	public static PageRequest buildPageRequest() {
		return new PageRequest(1, Constants.DEFAULT_PAGE_SIGE);
	}

	public static PageRequest buildPageRequest(int pageNumber, int pageSize) {
		pageNumber = pageNumber - 1;
		if(pageNumber < 0){
			pageNumber = 0;
		}

		if (pageSize <= 0) {
			pageSize = Constants.DEFAULT_PAGE_SIGE;
		}

		return new PageRequest(pageNumber, pageSize);
	}


	/**
	 * 构造分页参数
	 *
	 * @param
	 * @param pageSize
	 *            页面大小(默认为10条)
	 * @param sort
	 *            排序字段
	 * @return
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pageSize,String sort) {

		List<Sort.Order> orders = new ArrayList<>();

		pageNumber = pageNumber - 1;
		if(pageNumber < 0){
			pageNumber = 0;
		}

		if (pageSize <= 0) {
			pageSize = Constants.DEFAULT_PAGE_SIGE;
		}

		if (StringUtils.isNotEmpty(sort)) {
			PageSort[] pageSorts = null;
			try {
				pageSorts = mapper.readValue(sort, PageSort[].class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (PageSort pageSort : pageSorts) {
				if(SORT_ASC.equalsIgnoreCase(pageSort.getDirection())){
					orders.add(new Sort.Order(Sort.Direction.ASC, pageSort.getProperty()));
				}else{
					orders.add(new Sort.Order(Sort.Direction.DESC, pageSort.getProperty()));
				}
			}
			Sort sorts = new Sort(orders);
			return new PageRequest(pageNumber , pageSize, sorts);
		} else {
			return new PageRequest(pageNumber , pageSize);
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 *
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(
			String jsonFilter, String prefix) {
		Map<String, Object> searchParams = new HashMap<>();
		if (StringUtils.isNotEmpty(jsonFilter) && StringUtils.isNotEmpty(prefix)) {
			Map<String, String> map = null;
			try {
				map = mapper.readValue(jsonFilter, HashMap.class);
			} catch (IOException e){
				e.printStackTrace();
			}
			for (String key : map.keySet()) {
				if (key.startsWith(prefix)) {
					String unprefixed = key.substring(prefix.length());
					Object value = map.get(key);
					searchParams.put(unprefixed, value);
				}
			}
		}
		return searchParams;
	}

	public static Map<String, String> buildDeleteMethodResult(boolean isDeleted){
		Map<String, String> result = new HashMap<>();
		if(!isDeleted){
			result.put(Constants.RESPONSE_CODE, Constants.CODE_FAIL);
			result.put(Constants.RESPONSE_MSG, Constants.MSG_DELETE_FAIL);
		} else {
			result.put(Constants.RESPONSE_CODE, Constants.CODE_SUCCESS);
			result.put(Constants.RESPONSE_MSG, Constants.MSG_DELETE_SUCCESS);
		}
		return result;
	}
}
