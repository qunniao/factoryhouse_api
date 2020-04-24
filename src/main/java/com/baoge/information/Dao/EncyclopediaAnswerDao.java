package com.baoge.information.Dao;

import com.baoge.information.PO.EncyclopediaAnswerPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EncyclopediaAnswerDao extends JpaRepository<EncyclopediaAnswerPO, Integer>, JpaSpecificationExecutor<EncyclopediaAnswerPO> {
    List<EncyclopediaAnswerPO> queryByElid(int elid);

    int countByElidAndAndIsEnd(int elid,boolean isEnd);

    EncyclopediaAnswerPO queryByEaid(int eaid);
}
