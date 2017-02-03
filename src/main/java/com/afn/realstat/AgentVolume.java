package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

@Entity
@Table(name = "agent_volume", uniqueConstraints = @UniqueConstraint(columnNames = { "BreNo", "Year" }))
public class AgentVolume extends AbstractEntity {

	@Basic(optional = false)
	private String breNo;
	@Basic(optional = false)
	private Date year;
	private Integer rank;
	private Integer unitsListed;
	private Double volumeListed;
	private Integer unitsSold;
	private Double volumeSold;
	private Integer unitsTotal;
	private Double volumeTotal;
	private String agentRaw;
	private String officeRaw;
	private Double percentMlsVolume;
	private Double avgTtlPrice;
	private Double avgDom;

	public AgentVolume() {
	}

	public AgentVolume(String id, Date year) {
		this.breNo = id;
		this.year = year;
	}

	@Override
	public String toString() {
		return String.format(
				"AgentVolume [Bre='%s', Year='%s',  AgentRaw='%s', OfficeRaw='%s', UnitsTotal='%s', VolumeTotal='%s',]",
				breNo, year, agentRaw, officeRaw, unitsTotal, volumeTotal);
	}

	@Override
	public void saveOrUpdate() {
	}

	@Override
	public void clean() {
	}

	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of(new AgentVolume(getBreNo(), getYear()));
		return e;
	}

	@Override
	public boolean isValid() {
		return (breNo != null && year != null);
	}

	public String extractBrefromAgentRaw() {
		String agr = getAgentRaw();
		String bre = agr.replaceAll("[A-Z,a-z,\\-, ]", "");
		return bre;
	}

	// All getters and setters

	public String getBreNo() {
		return breNo;
	}

	public void setBreNo(String breNo) {
		this.breNo = breNo;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Integer getUnitsListed() {
		return unitsListed;
	}

	public void setUnitsListed(Integer unitsListed) {
		this.unitsListed = unitsListed;
	}

	public Double getVolumeListed() {
		return volumeListed;
	}

	public void setVolumeListed(Double volumeListed) {
		this.volumeListed = volumeListed;
	}

	public Integer getUnitsSold() {
		return unitsSold;
	}

	public void setUnitsSold(Integer unitsSold) {
		this.unitsSold = unitsSold;
	}

	public Double getVolumeSold() {
		return volumeSold;
	}

	public void setVolumeSold(Double volumeSold) {
		this.volumeSold = volumeSold;
	}

	public Integer getUnitsTotal() {
		return unitsTotal;
	}

	public void setUnitsTotal(Integer unitsTotal) {
		this.unitsTotal = unitsTotal;
	}

	public Double getVolumeTotal() {
		return volumeTotal;
	}

	public void setVolumeTotal(Double volumeTotal) {
		this.volumeTotal = volumeTotal;
	}

	public String getAgentRaw() {
		return agentRaw;
	}

	public void setAgentRaw(String agentRaw) {
		this.agentRaw = agentRaw;
	}

	public String getOfficeRaw() {
		return officeRaw;
	}

	public void setOfficeRaw(String officeRaw) {
		this.officeRaw = officeRaw;
	}

	public Double getPercentMlsVolume() {
		return percentMlsVolume;
	}

	public void setPercentMlsVolume(Double percentMlsVolume) {
		this.percentMlsVolume = percentMlsVolume;
	}

	public Double getAvgTtlPrice() {
		return avgTtlPrice;
	}

	public void setAvgTtlPrice(Double avgTtlPrice) {
		this.avgTtlPrice = avgTtlPrice;
	}

	public Double getAvgDom() {
		return avgDom;
	}

	public void setAvgDom(Double avgDom) {
		this.avgDom = avgDom;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}
