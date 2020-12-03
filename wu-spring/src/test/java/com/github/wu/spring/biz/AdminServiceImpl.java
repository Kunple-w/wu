package com.github.wu.spring.biz;

import com.github.wu.spring.WuInject;
import com.github.wu.spring.WuService;

/**
 * @author wangyongxu
 */
@WuService
public class AdminServiceImpl implements AdminService {

    @WuInject
    private EmailService emailService;

    @Override
    public String admin(String cmd) {
        return emailService.echo("admin", cmd);
    }

}

