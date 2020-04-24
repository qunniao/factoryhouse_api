package com.baoge.information.Dao;

import com.baoge.information.PO.NewsPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsDao extends JpaRepository<NewsPO, Integer>, JpaSpecificationExecutor<NewsPO> {
    NewsPO queryByNid(int nid);
}
