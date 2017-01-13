package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"MlsNo"}))
public class PropertyTransaction extends AbstractEntity {

	@Basic(optional=false) private Integer MlsNo;
	private String Status;
	private String Dom;
	private String Address;
	private String Unit;
	private String City;
	private String Area;
	private Double ListPrice;
	private Double SalesPrice;
	private String BuildingType;
	private Double Sqft;
	private Integer Bedrooms;
	private Integer Baths;
	private Integer BathsPartial;
	private String Gar;
	private Integer Garsp;
	private Date Yrblt;
	private Double Acres;
	private Double LotSqft;
	private Double HoaFee;
	private String HoaFeesFreq;
	private Date CloseDate;
	private Integer Age;
	private String Apn;
	private String CensusTract;
	private String CityTransferTax;
	private String PropClass;
	private String CoListAgentBreNum;
	private String CoSellAgentBreNum;
	private Double CompToSellingOffice;
	private String CompType;
	private String County;
	private Integer CumulativeDomls;
	private Integer DaysOnMls;
	private Date GeneralDate;
	private String DistNeighborhoodSubdiv;
	private String DualVariable;
	private Integer Fireplaces;
	private String FloorUnitIsOn;
	private String ForeclosureStatus;
	private String Hoa;
	private String HowSold;
	private Date InputDate;
	private Double ListPriceSqft;
	private String ListAgentBreNum;
	private Date ListDate;
	private String ListingBrokerBreNumber;
	private String ListingType;
	private Integer NumberOfOffers;
	private Date OffMarketDate;
	private Double OriginalPrice;
	private Date PendingDate;
	private String PendingLitigation;
	private Double OccPercent;
	private String PointOfSaleOrdinance;
	private String Pool;
	private Double PreviousPrice;
	private Double Price;
	private Date PriceDate;
	private Double PriceSqft;
	private Integer RoomsTotal;
	private Double SalePriceSqft;
	private Double SaleCoop;
	private Double SaleLastListPrice;
	private Double SaleOriginalPrice;
	private String SaleRent;
	private String SellingBrokerBreNumber;
	private String SoldAgentBreNum;
	private String TaxId;
	private Double TicPercentOwnerOffered;
	private Integer UnitsInComplex;
	private Date UpdateDate;
	private String Source;
	private String SpecialInformation;
	private String State;
	private Date StatusDate;
	private String Stories;
	private String Zip;
	private String Zoning;

	public PropertyTransaction() {
	}

	public PropertyTransaction(Integer id) {
		this.MlsNo = id;
	}

	@Override
	public String toString() {
		return String.format("PropTransaction [id=%d, Address='%s', Unit='%s', City='%s',Zip='%s']", 
				MlsNo, Address, Unit, City, Zip);
	}
	
	@Override
	public void saveOrUpdate() {
	}
	
	@Override
	public Example<AbstractEntity> getRefExample() {
		Example<AbstractEntity> e = Example.of( new PropertyTransaction(this.getMlsNo()));
		return e;
	}
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getDom() {
		return Dom;
	}

	public void setDom(String dom) {
		Dom = dom;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public Double getListPrice() {
		return ListPrice;
	}

	public void setListPrice(Double listPrice) {
		ListPrice = listPrice;
	}

	public Double getSalesPrice() {
		return SalesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		SalesPrice = salesPrice;
	}

	public String getBuildingType() {
		return BuildingType;
	}

	public void setBuildingType(String buildingType) {
		BuildingType = buildingType;
	}

	public Double getSqft() {
		return Sqft;
	}

	public void setSqft(Double sqft) {
		Sqft = sqft;
	}

	public Integer getBedrooms() {
		return Bedrooms;
	}

	public void setBedrooms(Integer bedrooms) {
		Bedrooms = bedrooms;
	}

	public Integer getBaths() {
		return Baths;
	}

	public void setBaths(Integer baths) {
		Baths = baths;
	}

	public Integer getBathsPartial() {
		return BathsPartial;
	}

	public void setBathsPartial(Integer bathsPartial) {
		BathsPartial = bathsPartial;
	}

	public String getGar() {
		return Gar;
	}

	public void setGar(String gar) {
		Gar = gar;
	}

	public Integer getGarsp() {
		return Garsp;
	}

	public void setGarsp(Integer garsp) {
		Garsp = garsp;
	}

	public Date getYrblt() {
		return Yrblt;
	}

	public void setYrblt(Date yrblt) {
		Yrblt = yrblt;
	}

	public Double getAcres() {
		return Acres;
	}

	public void setAcres(Double acres) {
		Acres = acres;
	}

	public Double getLotSqft() {
		return LotSqft;
	}

	public void setLotSqft(Double lotSqft) {
		LotSqft = lotSqft;
	}

	public Double getHoaFee() {
		return HoaFee;
	}

	public void setHoaFee(Double hoaFee) {
		HoaFee = hoaFee;
	}

	public String getHoaFeesFreq() {
		return HoaFeesFreq;
	}

	public void setHoaFeesFreq(String hoaFeesFreq) {
		HoaFeesFreq = hoaFeesFreq;
	}

	public Date getCloseDate() {
		return CloseDate;
	}

	public void setCloseDate(Date closeDate) {
		CloseDate = closeDate;
	}

	public Integer getAge() {
		return Age;
	}

	public void setAge(Integer age) {
		Age = age;
	}

	public String getApn() {
		return Apn;
	}

	public void setApn(String apn) {
		Apn = apn;
	}

	public String getCensusTract() {
		return CensusTract;
	}

	public void setCensusTract(String censusTract) {
		CensusTract = censusTract;
	}

	public String getCityTransferTax() {
		return CityTransferTax;
	}

	public void setCityTransferTax(String cityTransferTax) {
		CityTransferTax = cityTransferTax;
	}

	public String getPropClass() {
		return PropClass;
	}

	public void setPropClass(String pClass) {
		PropClass = pClass;
	}

	public String getCoListAgentBreNum() {
		return CoListAgentBreNum;
	}

	public void setCoListAgentBreNum(String coListAgentBreNum) {
		CoListAgentBreNum = coListAgentBreNum;
	}

	public String getCoSellAgentBreNum() {
		return CoSellAgentBreNum;
	}

	public void setCoSellAgentBreNum(String coSellAgentBreNum) {
		CoSellAgentBreNum = coSellAgentBreNum;
	}

	public Double getCompToSellingOffice() {
		return CompToSellingOffice;
	}

	public void setCompToSellingOffice(Double compToSellingOffice) {
		CompToSellingOffice = compToSellingOffice;
	}

	public String getCompType() {
		return CompType;
	}

	public void setCompType(String compType) {
		CompType = compType;
	}

	public String getCounty() {
		return County;
	}

	public void setCounty(String county) {
		County = county;
	}

	public Integer getCumulativeDomls() {
		return CumulativeDomls;
	}

	public void setCumulativeDomls(Integer cumulativeDomls) {
		CumulativeDomls = cumulativeDomls;
	}

	public Integer getDaysOnMls() {
		return DaysOnMls;
	}

	public void setDaysOnMls(Integer daysOnMls) {
		DaysOnMls = daysOnMls;
	}

	public Date getGeneralDate() {
		return GeneralDate;
	}

	public void setGeneralDate(Date generalDate) {
		GeneralDate = generalDate;
	}

	public String getDistNeighborhoodSubdiv() {
		return DistNeighborhoodSubdiv;
	}

	public void setDistNeighborhoodSubdiv(String distNeighborhoodSubdiv) {
		DistNeighborhoodSubdiv = distNeighborhoodSubdiv;
	}

	public String getDualVariable() {
		return DualVariable;
	}

	public void setDualVariable(String dualVariable) {
		DualVariable = dualVariable;
	}

	public Integer getFireplaces() {
		return Fireplaces;
	}

	public void setFireplaces(Integer fireplaces) {
		Fireplaces = fireplaces;
	}

	public String getFloorUnitIsOn() {
		return FloorUnitIsOn;
	}

	public void setFloorUnitIsOn(String floorUnitIsOn) {
		FloorUnitIsOn = floorUnitIsOn;
	}

	public String getForeclosureStatus() {
		return ForeclosureStatus;
	}

	public void setForeclosureStatus(String foreclosureStatus) {
		ForeclosureStatus = foreclosureStatus;
	}

	public String getHoa() {
		return Hoa;
	}

	public void setHoa(String hoa) {
		Hoa = hoa;
	}

	public String getHowSold() {
		return HowSold;
	}

	public void setHowSold(String howSold) {
		HowSold = howSold;
	}

	public Date getInputDate() {
		return InputDate;
	}

	public void setInputDate(Date inputDate) {
		InputDate = inputDate;
	}

	public Double getListPriceSqft() {
		return ListPriceSqft;
	}

	public void setListPriceSqft(Double listPriceSqft) {
		ListPriceSqft = listPriceSqft;
	}

	public String getListAgentBreNum() {
		return ListAgentBreNum;
	}

	public void setListAgentBreNum(String listAgentBreNum) {
		ListAgentBreNum = listAgentBreNum;
	}

	public Date getListDate() {
		return ListDate;
	}

	public void setListDate(Date listDate) {
		ListDate = listDate;
	}

	public String getListingBrokerBreNumber() {
		return ListingBrokerBreNumber;
	}

	public void setListingBrokerBreNumber(String listingBrokerBreNumber) {
		ListingBrokerBreNumber = listingBrokerBreNumber;
	}

	public String getListingType() {
		return ListingType;
	}

	public void setListingType(String listingType) {
		ListingType = listingType;
	}

	public Integer getNumberOfOffers() {
		return NumberOfOffers;
	}

	public void setNumberOfOffers(Integer numberOfOffers) {
		NumberOfOffers = numberOfOffers;
	}

	public Date getOffMarketDate() {
		return OffMarketDate;
	}

	public void setOffMarketDate(Date offMarketDate) {
		OffMarketDate = offMarketDate;
	}

	public Double getOriginalPrice() {
		return OriginalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		OriginalPrice = originalPrice;
	}

	public Date getPendingDate() {
		return PendingDate;
	}

	public void setPendingDate(Date pendingDate) {
		PendingDate = pendingDate;
	}

	public String getPendingLitigation() {
		return PendingLitigation;
	}

	public void setPendingLitigation(String pendingLitigation) {
		PendingLitigation = pendingLitigation;
	}

	public Double getOccPercent() {
		return OccPercent;
	}

	public void setOccPercent(Double occPercent) {
		OccPercent = occPercent;
	}

	public String getPointOfSaleOrdinance() {
		return PointOfSaleOrdinance;
	}

	public void setPointOfSaleOrdinance(String pointOfSaleOrdinance) {
		PointOfSaleOrdinance = pointOfSaleOrdinance;
	}

	public String getPool() {
		return Pool;
	}

	public void setPool(String pool) {
		Pool = pool;
	}

	public Double getPreviousPrice() {
		return PreviousPrice;
	}

	public void setPreviousPrice(Double previousPrice) {
		PreviousPrice = previousPrice;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public Date getPriceDate() {
		return PriceDate;
	}

	public void setPriceDate(Date priceDate) {
		PriceDate = priceDate;
	}

	public Double getPriceSqft() {
		return PriceSqft;
	}

	public void setPriceSqft(Double priceSqft) {
		PriceSqft = priceSqft;
	}

	public Integer getRoomsTotal() {
		return RoomsTotal;
	}

	public void setRoomsTotal(Integer roomsTotal) {
		RoomsTotal = roomsTotal;
	}

	public Double getSalePriceSqft() {
		return SalePriceSqft;
	}

	public void setSalePriceSqft(Double salePriceSqft) {
		SalePriceSqft = salePriceSqft;
	}

	public Double getSaleCoop() {
		return SaleCoop;
	}

	public void setSaleCoop(Double saleCoop) {
		SaleCoop = saleCoop;
	}

	public Double getSaleLastListPrice() {
		return SaleLastListPrice;
	}

	public void setSaleLastListPrice(Double saleLastListPrice) {
		SaleLastListPrice = saleLastListPrice;
	}

	public Double getSaleOriginalPrice() {
		return SaleOriginalPrice;
	}

	public void setSaleOriginalPrice(Double saleOriginalPrice) {
		SaleOriginalPrice = saleOriginalPrice;
	}

	public String getSaleRent() {
		return SaleRent;
	}

	public void setSaleRent(String saleRent) {
		SaleRent = saleRent;
	}

	public String getSellingBrokerBreNumber() {
		return SellingBrokerBreNumber;
	}

	public void setSellingBrokerBreNumber(String sellingBrokerBreNumber) {
		SellingBrokerBreNumber = sellingBrokerBreNumber;
	}

	public String getSoldAgentBreNum() {
		return SoldAgentBreNum;
	}

	public void setSoldAgentBreNum(String soldAgentBreNum) {
		SoldAgentBreNum = soldAgentBreNum;
	}

	public String getTaxId() {
		return TaxId;
	}

	public void setTaxId(String taxId) {
		TaxId = taxId;
	}

	public Double getTicPercentOwnerOffered() {
		return TicPercentOwnerOffered;
	}

	public void setTicPercentOwnerOffered(Double ticPercentOwnerOffered) {
		TicPercentOwnerOffered = ticPercentOwnerOffered;
	}

	public Integer getUnitsInComplex() {
		return UnitsInComplex;
	}

	public void setUnitsInComplex(Integer unitsInComplex) {
		UnitsInComplex = unitsInComplex;
	}

	public Date getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(Date updateDate) {
		UpdateDate = updateDate;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public String getSpecialInformation() {
		return SpecialInformation;
	}

	public void setSpecialInformation(String specialInformation) {
		SpecialInformation = specialInformation;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public Date getStatusDate() {
		return StatusDate;
	}

	public void setStatusDate(Date statusDate) {
		StatusDate = statusDate;
	}

	public String getStories() {
		return Stories;
	}

	public void setStories(String stories) {
		Stories = stories;
	}

	public String getZip() {
		return Zip;
	}

	public void setZip(String zip) {
		Zip = zip;
	}

	public String getZoning() {
		return Zoning;
	}

	public void setZoning(String zoning) {
		Zoning = zoning;
	}

	public Integer getMlsNo() {
		return MlsNo;
	}

	public void setMlsNo(Integer mlsNo) {
		MlsNo = mlsNo;
	}
	



}
