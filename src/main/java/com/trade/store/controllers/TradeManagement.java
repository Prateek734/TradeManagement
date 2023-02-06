package com.trade.store.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trade.store.config.EnableSwaggerDocumentation;
import com.trade.store.entity.Trade;
import com.trade.store.model.ResposeTradeMessage;
import com.trade.store.model.TradeModel;
import com.trade.store.service.TradeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * QueueManagement Controller.
 *
 * @author Prateek.
 */
@Controller
@EnableSwaggerDocumentation
@Api(value = "Trade", description = "")
public class TradeManagement {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeManagement.class);
	public static final int ERROR_CODE_NOT_FOUND = 60;
	public static final int ERROR_CODE_INTERNAL_SERVER = 1;
	public static final int ERROR_CODE_BAD_REQUEST = 23;
	public static final int ERROR_CODE_UNAUTHORIZED_ACCESS = 41;
	public static final int ERROR_CODE_OK = 4;
	public static final int ERROR_CODE_OK_SERVICE_UNAVAILABLE = 5;

	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error.";

	@Autowired
	private HttpServletRequest request;

	@Autowired
	TradeService tradeService;

	@ApiOperation(value = "This operation is used to fetch trades.", nickname = "trade", notes = "This operation is used to fetch trades.", response = Trade.class, tags = {
			"", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Trade.class),
			@ApiResponse(code = 500, message = "List of supported error codes:\r\n"
					+ " 1: Internal error", response = Error.class),
			@ApiResponse(code = 415, message = "List of supported error codes:\r\n"
					+ " 68: Unsupported Media Type", response = Error.class),
			@ApiResponse(code = 400, message = "List of supported error codes:\r\n"
					+ " 24: Bad Request", response = Error.class) })
	@RequestMapping(value = "/trade", produces = { "application/json" }, method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	ResponseEntity<?> getTrade(
			@ApiParam(value = "") @Valid @RequestParam(value = "tradeId", required = false) String tradeId,
			@ApiParam(value = "") @Valid @RequestParam(value = "version", required = false) Integer version) {

		try {
			LOGGER.info("In try Block getQueues ");

			try {

				List<Trade> tradeDetails = tradeService.fetchTradeDetails(tradeId, version);
				return new ResponseEntity<>(tradeDetails, HttpStatus.OK);
			} catch (Exception e) {

				return new ResponseEntity<>("Details Not Found.", HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred : ", e.getMessage());
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "This operation is used to add trades.", nickname = "trade", notes = "This operation is used to add trades.", response = String.class, tags = {
			"", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = String.class),
			@ApiResponse(code = 500, message = "List of supported error codes:\r\n"
					+ " 1: Internal error", response = Error.class),
			@ApiResponse(code = 415, message = "List of supported error codes:\r\n"
					+ " 68: Unsupported Media Type", response = Error.class),
			@ApiResponse(code = 400, message = "List of supported error codes:\r\n"
					+ " 24: Bad Request", response = Error.class) })
	@RequestMapping(value = "/trade", produces = { "application/json" }, method = RequestMethod.POST)
	@CrossOrigin
	@ResponseBody
	ResponseEntity<?> getTrade(@ApiParam(value = "") @Valid @RequestBody(required = true) TradeModel trade) {

		try {
			LOGGER.info("In try Block getTrade ");

			if (trade != null) {
				try {

					ResposeTradeMessage tradeDetails = tradeService.addTrade(trade);

					if (tradeDetails.getMessage().equals("Version")) {
						ResposeTradeMessage tradeDetails1 = new ResposeTradeMessage();
						tradeDetails1.setMessage("This is a lower version of trade and it should be higher.");
						return new ResponseEntity<>(tradeDetails1, HttpStatus.BAD_REQUEST);
					} else if (tradeDetails.getMessage().equals("Maturity Date")) {
						Date todayDate = new Date();
						ResposeTradeMessage tradeDetails1 = new ResposeTradeMessage();
						tradeDetails1.setMessage("Trade Failed as Maturity date is less than " + todayDate.toString());
						return new ResponseEntity<>(tradeDetails1, HttpStatus.BAD_REQUEST);
					} else {
						ResposeTradeMessage tradeDetails1 = new ResposeTradeMessage();
						String resp = "Trade Id: " + trade.getTradeId() + ", Trade Version : " + trade.getVersion()
								+ " Created Succesfully!!!";
						tradeDetails1.setMessage(resp);
						return new ResponseEntity<>(tradeDetails1, HttpStatus.OK);
					}

				} catch (Exception e) {
					return new ResponseEntity<>("Bad Request...", HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred : ", e.getMessage());
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
