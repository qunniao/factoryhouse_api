package com.baoge.Pay.Dao;

import com.baoge.Pay.PO.WalletOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WalletOrderDao extends JpaRepository<WalletOrder, Integer>, JpaSpecificationExecutor<WalletOrder> {

    List<WalletOrder> findAllByUid(int uid);

    List<WalletOrder> findAllByUidAndStatusInOrderByWoidDesc(int uid,int[] status);


    WalletOrder queryByOrderNumber(String orderNumber);
}
