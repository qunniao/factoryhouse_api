package com.baoge.information.Dao;

import com.baoge.information.PO.FplHousePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Author 王志鹏
 * @Date 2019/4/13 17:22
 **/
public interface FplHouseDao extends JpaRepository<FplHousePO, Integer>, JpaSpecificationExecutor<FplHousePO> {

    FplHousePO queryByProductId(String productId);

    int countByUid(int uid);

    List<FplHousePO> findAllByIsEnd(int isEnd);

    List<FplHousePO> findAllByUid(int uid);

    List<FplHousePO> findAllByUidAndTypes(int uid,int types);

    int countByRegionLike(String region);

    List<FplHousePO> findAllByRegionLike(String region);
}
