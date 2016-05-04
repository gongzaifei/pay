package com.kaolafm.payment.appservice;

import com.kaolafm.payment.request.CommonParams;
import org.springframework.stereotype.Service;

/**
 * @author gongzf
 * @date 2016/3/15
 */
public interface AliService {

    public String alipaySignAndParams(Double fee, CommonParams commonParam);

}
