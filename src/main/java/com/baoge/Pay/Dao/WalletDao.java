package com.baoge.Pay.Dao;

import com.baoge.Pay.PO.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletDao extends JpaRepository<Wallet, Integer> {
    Wallet queryByUid(int uid);
}
