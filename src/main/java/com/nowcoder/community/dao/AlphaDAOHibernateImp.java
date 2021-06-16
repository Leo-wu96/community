package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")
public class AlphaDAOHibernateImp implements AlphaDAO{
    @Override
    public String select() {
        return "Hibernate";
    }
}
