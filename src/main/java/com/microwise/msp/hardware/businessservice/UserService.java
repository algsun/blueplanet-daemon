package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author bastengao
 * @date 14-5-4 上午10:43
 */
@Component
@Scope("prototype")
public class UserService {

    @Autowired
    private UserDao userDao;

    public User findById(int userId) {
        return userDao.findById(userId);
    }

    /**
     * 查询站点管理员
     *
     * @param logicGroupId
     * @return
     */
    public User findSiteManager(int logicGroupId) {
        return userDao.findSiteManager(logicGroupId);
    }
}
