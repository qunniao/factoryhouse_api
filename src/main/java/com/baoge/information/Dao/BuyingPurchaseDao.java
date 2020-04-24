package com.baoge.information.Dao;

import com.baoge.information.PO.BuyingPurchasePO;
import com.baoge.information.PO.FplHousePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface BuyingPurchaseDao extends JpaRepository<BuyingPurchasePO, Integer>, JpaSpecificationExecutor<BuyingPurchasePO> {

    List<BuyingPurchasePO> queryByTypeAndStatus(int type, int status);

    List<BuyingPurchasePO>  findAllByUid(int uid);

    BuyingPurchasePO queryByBpid(int bpid);

}
