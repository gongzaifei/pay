package com;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gongzf
 * @date 2016/3/11
 */
public class Application {

    public static void main(String[] args) throws Exception{


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:application-service.xml","classpath*:rpc-provider.xml"});
        while (true) {
            try {
                Application.class.wait();
            }
            catch (Throwable e) {

            }
        }

    }

}
