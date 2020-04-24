package com.baoge.data.Dao;


import com.baoge.data.PO.CityPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 王志鹏
 * @title: CityPO
 * @projectName gymnasium
 * @description: TODO
 * @date 2019/4/8 10:36
 */
public interface CityDao extends JpaRepository<CityPO, Integer>  , JpaSpecificationExecutor<CityPO> {

    List<CityPO> queryByLevel(int level);

    List<CityPO> queryByMergerNameLikeAndLevel(String mergerName, int level);

    List<CityPO> queryByPid(int pid);

    CityPO queryTop1ByName(String name);
}
