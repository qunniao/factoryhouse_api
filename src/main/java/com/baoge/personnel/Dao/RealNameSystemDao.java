package com.baoge.personnel.Dao;

import com.baoge.personnel.PO.RealNameSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RealNameSystemDao extends JpaSpecificationExecutor<RealNameSystem>,JpaRepository<RealNameSystem, Integer> {
    RealNameSystem queryTop1ByUidOrderByRnidDesc(int uid);

    @Modifying
    @Transactional
    @Query(value = "update RealNameSystem set isEnd=3 where uid=:uid")
    int updateByUidForIsEnd(int uid);

    List<RealNameSystem> findAllByUid(int uid);

}
