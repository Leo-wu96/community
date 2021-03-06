package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDAO;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)


class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}


	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		AlphaDAO alphaDAO = applicationContext.getBean(AlphaDAO.class);
		System.out.println(alphaDAO.select());

		alphaDAO = applicationContext.getBean("alphaHibernate",AlphaDAO.class);
		System.out.println(alphaDAO.select());

	}

	@Test
	public void testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}


	@Test
	public void testBeanConfig(){
		SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(bean.format(new Date()));

	}

	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDAO alphaDAO;


	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;
	@Test
	public void testDI(){
		System.out.println(alphaDAO.select());
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}
}
