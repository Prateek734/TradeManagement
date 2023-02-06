package com.trade.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Integer id;

	@Column(name = "tradeid")
	String tradeId;

	@Column(name = "version")
	int version;

	@Column(name = "counterpartyid")
	String counterPartyId;

	@Column(name = "bookid")
	String bookId;

	@Column(name = "maturitydate")
	String maturityDate;

	@Column(name = "createddate")
	String createdDate;

	@Column(name = "isexpired")
	String isExpired;

	public Trade() {
	}

	/**
	 * @param tradeId
	 * @param version
	 * @param counterPartyId
	 * @param bookId
	 * @param maturityDate
	 * @param createdDate
	 * @param isExpired
	 */
	public Trade(String tradeId, int version, String counterPartyId, String bookId, String maturityDate,
			String createdDate, String isExpired) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.counterPartyId = counterPartyId;
		this.bookId = bookId;
		this.maturityDate = maturityDate;
		this.createdDate = createdDate;
		this.isExpired = isExpired;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(String isExpired) {
		this.isExpired = isExpired;
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", tradeId=" + tradeId + ", version=" + version + ", counterPartyId="
				+ counterPartyId + ", bookId=" + bookId + ", maturityDate=" + maturityDate + ", createdDate="
				+ createdDate + ", isExpired=" + isExpired + "]";
	}

}
