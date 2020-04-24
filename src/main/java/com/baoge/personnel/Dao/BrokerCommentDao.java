package com.baoge.personnel.Dao;

import com.baoge.personnel.PO.BrokerCommentPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BrokerCommentDao extends JpaRepository<BrokerCommentPO, Integer>, JpaSpecificationExecutor<BrokerCommentPO> {
}
