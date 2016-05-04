package com.kaolafm.payment.mapper;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

/**
 * BUG:MappingJacksonJsonView返回 {model类名:{内容}}
 */
public class CustomMappingJacksonJsonView extends MappingJackson2JsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<?, ?> result = (Map<?, ?>) super.filterModel(model);
		if (result.size() == 1) {
			return result.values().iterator().next();
		} else {
			return result;
		}
	}
	
}
