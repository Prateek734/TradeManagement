package com.trade.store.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.trade.store.entity.Trade;
import com.trade.store.model.ResposeTradeMessage;
import com.trade.store.model.TradeModel;
import com.trade.store.repository.TradeRepository;

/**
 * @author Prateek.
 */
@Service
public class TradeService {

	@Autowired
	TradeRepository tradeRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

	/**
	 * @param tradeId
	 * @param version
	 * @return
	 * @throws ParseException
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Trade> fetchTradeDetails(String tradeId, Integer version) throws ParseException {

		LOGGER.info("Starts fetchTradeDetails service method!!!");
		List<Trade> allTrade = tradeRepository.findAll();
		List<Trade> listTrades1 = new ArrayList();
		if (tradeId != null && version != null) {
			for (Trade trade : allTrade) {
				if (trade.getTradeId().equals(tradeId) && trade.getVersion() == version) {
					List<Trade> findByTradeIdAndVersion = tradeRepository.findByTradeIdAndVersion(tradeId, version);
					listTrades1.addAll(findByTradeIdAndVersion);
				}
			}
		} else {
			List<Trade> findAll = tradeRepository.findAll();
			listTrades1.addAll(findAll);
		}
		return listTrades1;
	}

	/**
	 * @param trade
	 * @return
	 * @throws ParseException
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ResposeTradeMessage addTrade(TradeModel trade) throws ParseException {

		LOGGER.info("Starts addTrade service method!!!");

		ResposeTradeMessage resposeMessage = new ResposeTradeMessage();

		String tradeId = trade.getTradeId();
		int version = trade.getVersion();
		String maturityDate = trade.getMaturityDate();

		LOGGER.info("TRADE Request : {}", trade);

		List<Trade> allTrade = tradeRepository.findAll();

		if (!allTrade.isEmpty()) {
			LOGGER.info("listTrades size : {}", allTrade.size());
			List<Trade> tradeById1 = tradeRepository.findByTradeIdAndVersion(tradeId, version);
			if (!tradeById1.isEmpty()) {
				Trade trade2 = tradeById1.get(0);
				if (!StringUtils.isEmpty(maturityDate)) {

					Date matDate = new SimpleDateFormat("dd-MM-yyyy").parse(maturityDate);
					Date todayDate = new Date();

					if (matDate.compareTo(todayDate) < 0) {
						LOGGER.info("Maturity Date of the trade is less than the todays date.");
						String resp = "Maturity Date";
						resposeMessage.setMessage(resp);
					} else {
						Trade tradeById = tradeRepository.findById(trade2.getId());
						tradeById.setTradeId(tradeId);
						tradeById.setVersion(version);
						tradeById.setCounterPartyId(tradeById.getCounterPartyId());
						tradeById.setBookId(tradeById.getBookId());
						tradeById.setMaturityDate(maturityDate);
						tradeById.setCreatedDate(trade2.getCreatedDate());
						tradeById.setIsExpired(trade2.getIsExpired());
						tradeRepository.save(tradeById);
						resposeMessage.setMessage("Success");
					}
				}

			} else {
				List<Trade> tradeById = tradeRepository.findByTradeId(trade.getTradeId());
				if (!tradeById.isEmpty()) {
					List<Integer> collect = tradeById.stream().map(x -> x.getVersion()).collect(Collectors.toList());
					collect.sort(Comparator.naturalOrder());
					if (version < collect.get(0)) {
						LOGGER.info("Trade version is less than the existing trade.");
						String resp = "Version";
						resposeMessage.setMessage(resp);
					} else {
						createTrade(trade);
						resposeMessage.setMessage("Success");
					}
				} else {
					createTrade(trade);
					resposeMessage.setMessage("Success");
				}

			}
		}
		return resposeMessage;

	}

	/**
	 * @return
	 */
	private String getCurrentDate() {
		String pattern = "dd-MM-yyyy";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		System.out.println("Current Date value: " + dateInString);
		return dateInString;
	}

	/**
	 * @param trade
	 * @return
	 */
	private String createTrade(TradeModel trade) {
		String tradeId = trade.getTradeId();
		int version = trade.getVersion();
		String counterPartyId = trade.getCounterPartyId();
		String bookId = trade.getBookId();
		String maturityDate = trade.getMaturityDate();
		String createdDate = getCurrentDate();
		String isExpired = trade.getIsExpired();

		LOGGER.info("Creating a new trade.");
		Trade trade1 = new Trade();
		trade1.setTradeId(tradeId);
		trade1.setVersion(version);
		trade1.setCounterPartyId(counterPartyId);
		trade1.setBookId(bookId);
		trade1.setMaturityDate(maturityDate);
		trade1.setCreatedDate(createdDate);
		trade1.setIsExpired(isExpired);

		tradeRepository.save(trade1);

		LOGGER.info("TRADE Created : {}", trade1.toString());
		return "Success";
	}
}
