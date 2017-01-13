package com.afn.realstat;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"BreNo"}))
public class AgentVolume extends AbstractEntity {

	private String BreNo;
	private Date Year;
	private Integer UnitsListed;
	private Double VolumeListed;
	private Integer UnitsSold;
	private Double VolumeSold;
	private Integer UnitsTotal;
	private Double VolumeTotal;
	private String AgentRawFormat;
	private String OfficeRawFormat;
	private Double PercentMlsVolume;
	private Double AvgTtlPrice;
	private Double AvgDom;

	public AgentVolume() {
	}

	public AgentVolume(String id) {
		this.BreNo = id;
	}

	@Override
	public String toString() {
		return String.format("AgentVolume [Bre='%s', Year='%s',  AgentRaw='%s', OfficeRaw='%s', UnitsTotal='%s', VolumeTotal='%s',]", 
				BreNo, Year, AgentRawFormat, OfficeRawFormat, UnitsTotal, VolumeTotal);
	}
	
	@Override
	public void saveOrUpdate() {
	}
	
	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of( new AgentVolume(this.getBreNo()));
		return e;
	}

	public String getBreNo() {
		return BreNo;
	}

	public void setBreNo(String breNo) {
		BreNo = breNo;
	}

	public Date getYear() {
		return Year;
	}

	public void setYear(Date year) {
		Year = year;
	}

	public Integer getUnitsListed() {
		return UnitsListed;
	}

	public void setUnitsListed(Integer unitsListed) {
		UnitsListed = unitsListed;
	}

	public Double getVolumeListed() {
		return VolumeListed;
	}

	public void setVolumeListed(Double volumeListed) {
		VolumeListed = volumeListed;
	}

	public Integer getUnitsSold() {
		return UnitsSold;
	}

	public void setUnitsSold(Integer unitsSold) {
		UnitsSold = unitsSold;
	}

	public Double getVolumeSold() {
		return VolumeSold;
	}

	public void setVolumeSold(Double volumeSold) {
		VolumeSold = volumeSold;
	}

	public Integer getUnitsTotal() {
		return UnitsTotal;
	}

	public void setUnitsTotal(Integer unitsTotal) {
		UnitsTotal = unitsTotal;
	}

	public Double getVolumeTotal() {
		return VolumeTotal;
	}

	public void setVolumeTotal(Double volumeTotal) {
		VolumeTotal = volumeTotal;
	}

	public String getAgentRawFormat() {
		return AgentRawFormat;
	}

	public void setAgentRawFormat(String agentRawFormat) {
		AgentRawFormat = agentRawFormat;
	}

	public String getOfficeRawFormat() {
		return OfficeRawFormat;
	}

	public void setOfficeRawFormat(String officeRawFormat) {
		OfficeRawFormat = officeRawFormat;
	}
	
	public Double getPercentMlsVolume() {
		return PercentMlsVolume;
	}

	public void setPercentMlsVolume(Double percentMlsVolume) {
		PercentMlsVolume = percentMlsVolume;
	}

	public Double getAvgTtlPrice() {
		return AvgTtlPrice;
	}

	public void setAvgTtlPrice(Double avgTtlPrice) {
		AvgTtlPrice = avgTtlPrice;
	}

	public Double getAvgDom() {
		return AvgDom;
	}

	public void setAvgDom(Double avgDom) {
		AvgDom = avgDom;
	}
}
