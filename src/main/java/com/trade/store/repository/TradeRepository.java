package com.trade.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trade.store.entity.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

	Trade findById(Integer id);

	List<Trade> findByTradeIdAndVersion(String tradeId, Integer version);

	List<Trade> findByTradeId(String tradeId);

}