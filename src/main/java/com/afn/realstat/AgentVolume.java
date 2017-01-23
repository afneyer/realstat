package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

@Entity
@Table(name="agent_volume",uniqueConstraints=@UniqueConstraint(columnNames = {"BreNo","Year"}))
public class AgentVolume extends AbstractEntity {

	@Basic(optional=false) private String BreNo;
	@Basic(optional=false) private Date Year;
	private Integer Rank;
	private Integer UnitsListed;
	private Double VolumeListed;
	private Integer UnitsSold;
	private Double VolumeSold;
	private Integer UnitsTotal;
	private Double VolumeTotal;
	private String AgentRaw;
	private String OfficeRaw;
	private Double PercentMlsVolume;
	private Double AvgTtlPrice;
	private Double AvgDom;

	public AgentVolume() {
	}

	public AgentVolume(String id, Date year) {
		this.BreNo = id;
		this.Year = year;
	}

	@Override
	public String toString() {
		return String.format("AgentVolume [Bre='%s', Year='%s',  AgentRaw='%s', OfficeRaw='%s', UnitsTotal='%s', VolumeTotal='%s',]", 
				BreNo, Year, AgentRaw, OfficeRaw, UnitsTotal, VolumeTotal);
	}
	
	@Override
	public void saveOrUpdate() {
	}
	
	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of( new AgentVolume(getBreNo(), getYear()));
		return e;
	}
	
	public String extractBrefromAgentRaw() {
		String agr = getAgentRaw();
		String bre = agr.replaceAll("[A-Z,a-z,\\-, ]","");
		return bre;
	}
	
	// All getters and setters

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

	public String getAgentRaw() {
		return AgentRaw;
	}

	public void setAgentRaw(String agentRaw) {
		AgentRaw = agentRaw;
	}

	public String getOfficeRaw() {
		return OfficeRaw;
	}

	public void setOfficeRaw(String officeRaw) {
		OfficeRaw = officeRaw;
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

	public Integer getRank() {
		return Rank;
	}

	public void setRank(Integer rank) {
		Rank = rank;
	}

	
}
