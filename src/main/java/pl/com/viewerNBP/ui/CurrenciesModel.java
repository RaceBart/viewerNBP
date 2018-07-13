package pl.com.viewerNBP.ui;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="\"currencies\"")
public class CurrenciesModel implements Serializable{

	@Id
	@Column(name="\"id_serial\"")
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(columnDefinition = "serial")
	private Integer id_serial;
	@Column(name="currency_date")
	private Date currency_date;
	@Column(name="currency_name")
	private String currencyname;
	@Column(name="currency_value")
	private Double currency_value;
	
	public CurrenciesModel() {
		
	}
	
	public CurrenciesModel(Date currency_date, String currency_name, Double currency_value) {
		super();
//		this.id_serial = id_serial;
		this.currency_date = currency_date;
		this.currencyname = currency_name;
		this.currency_value = currency_value;
	}
	
	
	public Integer getId_serial() {
		return id_serial;
	}
	public void setId_serial(Integer id_serial) {
		this.id_serial = id_serial;
	}
	public Date getCurrency_date() {
		return currency_date;
	}
	public void setCurrency_date(Date currency_date) {
		this.currency_date = currency_date;
	}
	public String getCurrency_name() {
		return currencyname;
	}
	public void setCurrency_name(String currency_name) {
		this.currencyname = currency_name;
	}
	public Double getCurrency_value() {
		return currency_value;
	}
	public void setCurrency_value(Double currency_value) {
		this.currency_value = currency_value;
	}
	
}

