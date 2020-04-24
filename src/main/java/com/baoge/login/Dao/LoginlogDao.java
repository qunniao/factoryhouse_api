package com.baoge.login.Dao;

import com.baoge.login.PO.Loginlog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author 王志鹏
 * @Date 2019/4/14 14:22
 **/
public interface LoginlogDao extends JpaRepository<Loginlog, Integer> {

}
