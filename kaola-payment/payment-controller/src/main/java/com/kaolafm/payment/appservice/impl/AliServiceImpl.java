package com.kaolafm.payment.appservice.impl;

import com.kaolafm.payment.appservice.AliService;
import com.kaolafm.payment.request.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author gongzf
 * @date 2016/3/15
 */
@Service
public class AliServiceImpl implements AliService {

    private Logger logger = LoggerFactory.getLogger(AliServiceImpl.class);

    @Override
    public String alipaySignAndParams(Double fee, CommonParams commonParam) {


        return null;
    }
}
