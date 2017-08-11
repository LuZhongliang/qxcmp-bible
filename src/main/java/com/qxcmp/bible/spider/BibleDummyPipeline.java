package com.qxcmp.bible.spider;

import com.qxcmp.framework.user.User;
import com.qxcmp.framework.user.UserService;
import com.qxcmp.platform.module.spider.SpiderEntityPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 圣经蜘蛛管道
 *
 * @author aaric
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class BibleDummyPipeline extends SpiderEntityPipeline<User, UserService> {
    public BibleDummyPipeline(UserService entityService) {
        super(entityService);
    }

    @Override
    protected boolean isValidTarget(User target) {
        return false;
    }

    @Override
    protected Optional<User> getOriginTarget(User target) {
        return null;
    }

    @Override
    protected boolean isTargetChanged(User target, User origin) {
        return false;
    }

    @Override
    protected void newTarget(User target) {

    }

    @Override
    protected void updateTarget(User target, User origin) {

    }

    @Override
    protected void dropTarget(User target) {

    }
}
