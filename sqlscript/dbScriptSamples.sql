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
/*--
--select count(*) from property_transaction where realProperty_id is not null;
--select count(*) from property_transaction;
--select count(*) from real_property;*/

/*
--drop table agent;
*/


/*select status, 
count(ListingAgentName), count(SellingAgent1Name), count(CoListAgentName), count(CoSellAgentName), 
count(ListingAgentLicenseID), count(SellingAgent1LicenseId), count(CoListAgentLicenseId), count(CoSellAgentLicenseId),
count(ListAgentBreNum), count(SoldAgentBreNum), count(CoListAgentBreNum), count(CoSellAgentBreNum),
count(listingAgent_id), count(sellingAgent_id), count(listingAgent2_id), count(sellingAgent2_id)
from property_transaction group by status;*/

/*
select ListingAgentName, ListingAgentLicenseID, ListAgentBreNum,
 SellingAgent1Name, SellingAgent1LicenseId,SoldAgentBreNum,
CoListAgentLicenseId, CoSellAgentLicenseId, CoListAgentBreNum, 
CoListAgentName, CoSellAgentName, CoSellAgentBreNum
from property_transaction;
*/

/*
select status, count(id) from property_transaction group by status;
*/
/*
drop table real_property;
drop table agent;
drop table agent_volume;
drop table property_transaction;
*/





