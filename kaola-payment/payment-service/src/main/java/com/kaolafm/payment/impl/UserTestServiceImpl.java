package com.kaolafm.payment.impl;

import com.kaolafm.payment.service.IUserTestService;
import org.springframework.stereotype.Service;

/**
 * @author gongzf
 * @date 2016/3/12
 */
@Service
public class UserTestServiceImpl implements IUserTestService {


    @Override
    public String test() {
        return "test";
    }
}
