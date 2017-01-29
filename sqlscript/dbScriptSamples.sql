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

drop table agent;


