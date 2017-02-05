package com.afn.realstat;

import java.beans.Statement;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

@Entity
@Table(name = "real_property", uniqueConstraints = @UniqueConstraint(columnNames = { "apn" }), indexes = {
		@Index(name = "idx_apnClean", columnList = "apnClean"),
		@Index(name = "idx_addressClean", columnList = "addressClean") })

public class RealProperty extends AbstractEntity {

	@Basic(optional = false)
	private String apn;
	private String apnClean;
	private String owner1;
	private String title;
	private String firstName;
	private String middle;
	private String lastName;
	private String owner2;
	private String title2;
	private String firstName2;
	private String middle2;
	private String lastName2;
	private String ownerAddress;
	private String ownerCity;
	private String ownerState;
	private String ownerZip;
	private String improvementType;
	private String landUse;
	private String propertyAddress;
	private String propertyCity;
	private String propertyState;
	private String propertyZip;
	private String addressClean;
	private Double totalSquareFootage;
	private Date lastSaleDate;
	private Double lastSaleAmount;
	private String warrantyDeedBook;
	private String warrantyDeedPage;
	private String warrantyDeedDocumentNumber;
	private Double landValue;
	private Double improvementValue;
	private Double totalValue;
	private Date yearBuilt;
	private Double dimensions;
	private String subdivision;
	private Double acreage;
	private String block;
	private String lot;
	private Double lotSqFeet;
	private String lotDimensions;
	private String censusTract;

	public RealProperty() {
	}

	public RealProperty(String apn) {
		this.apn = apn;
	}

	@Override
	public String toString() {
		return String.format("Property [apn=%s, address=%s, city=%s, state=%s, zip=%s']", apn, propertyAddress,
				propertyCity, propertyState, propertyZip);
	}

	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of(new RealProperty(this.getApn()));
		return e;
	}
	
	@Override
	public boolean isValid() {
		return apn != null;
	}
	
	@Override
	public void clean() {
	}

	public void setFieldByString(Object bean, String field, String value) throws Exception {
		Statement stmt;
		stmt = new Statement(bean, field, new Object[] { value });
		stmt.execute();
	}

	public String getOwner1() {
		return owner1;
	}

	public void setOwner1(String owner1) {
		this.owner1 = owner1;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOwner2() {
		return owner2;
	}

	public void setOwner2(String owner2) {
		this.owner2 = owner2;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getFirstName2() {
		return firstName2;
	}

	public void setFirstName2(String firstName2) {
		this.firstName2 = firstName2;
	}

	public String getMiddle2() {
		return middle2;
	}

	public void setMiddle2(String middle2) {
		this.middle2 = middle2;
	}

	public String getLastName2() {
		return lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getOwnerCity() {
		return ownerCity;
	}

	public void setOwnerCity(String ownerCity) {
		this.ownerCity = ownerCity;
	}

	public String getOwnerState() {
		return ownerState;
	}

	public void setOwnerState(String ownerState) {
		this.ownerState = ownerState;
	}

	public String getOwnerZip() {
		return ownerZip;
	}

	public void setOwnerZip(String ownerZip) {
		this.ownerZip = ownerZip;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
		this.setApnClean(apn);
	}

	public String getApnClean() {
		return apnClean;
	}

	public void setApnClean(String apn) {
		this.apnClean = RealStatUtil.cleanApn(apn);
	}

	public String getImprovementType() {
		return improvementType;
	}

	public void setImprovementType(String improvementType) {
		this.improvementType = improvementType;
	}

	public String getLandUse() {
		return landUse;
	}

	public void setLandUse(String landUse) {
		this.landUse = landUse;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public String getPropertyCity() {
		return propertyCity;
	}

	public void setPropertyCity(String propertyCity) {
		this.propertyCity = propertyCity;
	}

	public String getPropertyState() {
		return propertyState;
	}

	public void setPropertyState(String propertyState) {
		this.propertyState = propertyState;
	}

	public String getPropertyZip() {
		return propertyZip;
	}

	public void setPropertyZip(String propertyZip) {
		this.propertyZip = propertyZip;
	}

	public String getAddressClean() {
		return addressClean;
	}

	public void setAddressClean() {
		Address adr = new Address(this.getPropertyAddress(), this.getPropertyCity(), this.getPropertyZip());

		// if it's a condo we want to make sure that the address has a unit
		// number associated with it
		if (this.getLandUse().equals("Residential Condominium")){
			if (adr.hasUnitInfo()) {
				this.addressClean = adr.getCleanAddress();
			} else {
				// if the owner is in the same building and has some unit info
				// try to use that address
				// Note: this may not be always reliable
				Address adrOwner = new Address(this.getOwnerAddress(), this.getOwnerCity(), this.getPropertyZip());
				if (adrOwner.isInSameBuilding(adr)) {
					this.addressClean = adrOwner.getCleanAddress();
				}
			}
		} else {
			this.addressClean = adr.getCleanAddress();
		}
	}

	public Double getTotalSquareFootage() {
		return totalSquareFootage;
	}

	public void setTotalSquareFootage(Double totalSquareFootage) {
		this.totalSquareFootage = totalSquareFootage;
	}

	public Date getLastSaleDate() {
		return lastSaleDate;
	}

	public void setLastSaleDate(Date lastSaleDate) {
		this.lastSaleDate = lastSaleDate;
	}

	public Double getLastSaleAmount() {
		return lastSaleAmount;
	}

	public void setLastSaleAmount(Double lastSaleAmount) {
		this.lastSaleAmount = lastSaleAmount;
	}

	public String getWarrantyDeedBook() {
		return warrantyDeedBook;
	}

	public void setWarrantyDeedBook(String warrantyDeedBook) {
		this.warrantyDeedBook = warrantyDeedBook;
	}

	public String getWarrantyDeedPage() {
		return warrantyDeedPage;
	}

	public void setWarrantyDeedPage(String warrantyDeedPage) {
		this.warrantyDeedPage = warrantyDeedPage;
	}

	public String getWarrantyDeedDocumentNumber() {
		return warrantyDeedDocumentNumber;
	}

	public void setWarrantyDeedDocumentNumber(String warrantyDeedDocumentNumber) {
		this.warrantyDeedDocumentNumber = warrantyDeedDocumentNumber;
	}

	public Double getLandValue() {
		return landValue;
	}

	public void setLandValue(Double landValue) {
		this.landValue = landValue;
	}

	public Double getImprovementValue() {
		return improvementValue;
	}

	public void setImprovementValue(Double improvementValue) {
		this.improvementValue = improvementValue;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Date getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(Date yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public Double getDimensions() {
		return dimensions;
	}

	public void setDimensions(Double dimensions) {
		this.dimensions = dimensions;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public Double getAcreage() {
		return acreage;
	}

	public void setAcreage(Double acreage) {
		this.acreage = acreage;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public Double getLotSqFeet() {
		return lotSqFeet;
	}

	public void setLotSqFeet(Double lotSqFeet) {
		this.lotSqFeet = lotSqFeet;
	}

	public String getLotDimensions() {
		return lotDimensions;
	}

	public void setLotDimensions(String lotDimensions) {
		this.lotDimensions = lotDimensions;
	}

	public String getCensusTract() {
		return censusTract;
	}

	public void setCensusTract(String censusTract) {
		this.censusTract = censusTract;
	}

	/*
	 * TODO remove public void test() { Long l = RealProperty_ }
	 */
}
