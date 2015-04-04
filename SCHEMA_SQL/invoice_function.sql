CREATE OR REPLACE function greetings
AS

v_person_id   					PERSON.person_id%TYPE;
v_accomodation_id 				PERSON_ACCOMODATION_LEASE.accomodation_id%TYPE;

v_living_rent 					invoice_person_lease.MONTHLY_HOUSING_RENT%TYPE;
v_parking_fees 					invoice_person_lease.MONTHLY_PARKING_RENT%TYPE;
v_late_fees 					NUMBER;
v_parking_fees					NUMBER :=100;
v_incidentalCharges				NUMBER:=20;

BEGIN
   

	BEGIN
		select fa.monthly_rent as living_rent,pc.fees parking_fees into 
		from family_apartment fa,parking_spot_has_class pc,personparkingspot_relation pr,person_accomodation_lease pal
		where pr.person_id = v_person_id
		and pr.accomodation_id = pal.person_id
		and pal.accomodation_id = fa.accomodation_id
		and fa.accomodation_id = v_accomodation_id
		and pr.spot_no = pc.spot_no;
		 
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN -1;

	END;




+		ps.setInt(5, invoiceNo); max+1
+		ps.setString(6, paymentDateString); get move in date from lease + inputINcrementVal




+		ps.setString(7, modeofPayment); from PERSON_ACC_STAFF
+		ps.setInt(8, newLeaseNumber); from person_accomodation_lease
+		ps.setString(9, paymentStatus); hardcode to 'OUTSTANDING' Refer constants.java
+		ps.setInt(10, totalPaymentDue); monthly_housing_rent+monthly_parking_rent+late_fees+incidental_charges
+		ps.setInt(11, damageCharges); 0
+		ps.setInt(12, personID);



END;
/