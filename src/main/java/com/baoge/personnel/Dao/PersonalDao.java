package com.baoge.personnel.Dao;

import com.baoge.personnel.PO.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 王志鹏
 * @title: PersonalDao
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/20 15:54
 */
public interface PersonalDao extends JpaRepository<UserPO,Integer> , JpaSpecificationExecutor<UserPO> {

    UserPO queryByUid(int uid);

    UserPO queryByUserPhone(String userPhone);

    int countByUid(int uid);

    int countByUserPhone(String phone);

    UserPO queryByUserPhoneAndUserPassword(String userPhone,String userPassword);
}
