/* select * from realproperty p where 
apnClean in 
( select apnClean from realproperty group by apnClean having count(apnClean)>1 ); */

/* select * from propertyTransaction p where property_id != null;*/
/* select count(*) from property_transaction p where realProperty_id is not null; */
/* drop table property_pransaction; */
/* select count(*) from property_transaction p where state = "CA";*/

/*
select apn,propertyAddress, addressClean from real_property p where 
addressClean in 
( select addressClean from real_property group by addressClean having count(addressClean)>1 );
*/
/*
select count(*) from property_transaction where realProperty_id is not null;
select count(*) from property_transaction;
select count(*) from real_property;*/

/*
drop table agent;
*/

/*
select status,  
count(ListingAgentName), count(SellingAgent1Name), count(CoListAgentName), count(CoSellAgentName), 
count(ListingAgentLicenseID), count(SellingAgent1LicenseId), count(CoListAgentLicenseId), count(CoSellAgentLicenseId),
count(ListAgentBreNum), count(SoldAgentBreNum), count(CoListAgentBreNum), count(CoSellAgentBreNum),
count(listingAgent_id), count(sellingAgent_id), count(listingAgent2_id), count(sellingAgent2_id)
from property_transaction group by status;
*/


select buildingType, status, 
count(realProperty_id), count(id), (count(id) - count(realProperty_id)) /count(id)
from property_transaction
where zip like "94611%"
group by  buildingType, status;


/*
select landUse,
count(id), count(id)
from real_property group by landUse;
*/

/*
select 
count(id)
from real_property
where propertyZip like "94611%";
*/
 
/*
where lastSaleDate is not null and propertyZip = '94611';
*/
/*
between CAST('2014-01-01' AS DATE) and cast('2015-01-01' as date);
*/

/*
select count(*) from agent;
*/


/*
--select ListingAgentName, ListingAgentLicenseID, ListAgentBreNum,
-- SellingAgent1Name, SellingAgent1LicenseId,SoldAgentBreNum,
--CoListAgentLicenseId, CoSellAgentLicenseId, CoListAgentBreNum, 
--CoListAgentName, CoSellAgentName, CoSellAgentBreNum
--from property_transaction;
*/
/*
select lower(listingAgentName), listingAgentLicenseID from property_transaction
where listingAgentLicenseId is null and lower(listingAgentName) not in (
select lower(listingAgentName) from property_transaction
where listingAgentLicenseID is not null and listingAgentName is not null
);
*/
/*
select lower(listingAgentName) from property_transaction where
listingAgentName is not null;
*/
/*
ALTER TABLE agent DROP index UK8e2w9ma3jkny0yhpsrltoe0qw;
*/

/*
 ALTER TABLE agent MODIFY COLUMN lastName varchar(255) NULL;
 *

/*
select concat(lower(listingAgentName), listingAgentLicenseID) from property_transaction
group by concat(lower(listingAgentName), listingAgentLicenseID)
having count(concat(lower(listingAgentName), listingAgentLicenseID)) > 1;
*/

/*
select status, count(id) from property_transaction group by status;
*/
/*
select count(*) from property_transaction;
*/
/*
drop table real_property;
drop table agent;
drop table agent_volume;
drop table property_transaction;
*/

/*
select count(status),status from property_transaction group by status;
select count(*) from property_transaction;
*/


/*
select lastSaleDate from real_property where lastSaleDate is not null;
*/





