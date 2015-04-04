CREATE OR REPLACE function create_invoice(v_person_id IN NUMBER,
	v_accomodation_id IN NUMBER,
	v_date_increment NUMBER,
	v_request_no NUMBER,
	v_mode_of_payment person_acc_staff.mode_of_payment%TYPE)
AS

v_person_id   					PERSON.person_id%TYPE;
v_accomodation_id 				PERSON_ACCOMODATION_LEASE.accomodation_id%TYPE;

v_living_rent 					invoice_person_lease.MONTHLY_HOUSING_RENT%TYPE;
v_parking_fees 					invoice_person_lease.MONTHLY_PARKING_RENT%TYPE;
v_late_fees 					NUMBER :=50;
v_parking_fees					NUMBER :=100;
v_incidentalCharges				NUMBER:=20;

v_max_invoice_no				invoice_person_lease.invoice_no%TYPE;
v_move_in_date					DATE;
v_payment_date					DATE;
v_lease_no						person_accomodation_lease.lease_no%TYPE;
v_mode_of_payment				person_acc_staff.mode_of_payment%TYPE;
v_payment_status				VARCHAR2(40) :='Outstanding';
v_total_payment_due				invoice_person_lease.payment_due%TYPE;
v_damage_charges				NUMBER:=0;


BEGIN
   

	BEGIN
		select fa.monthly_rent,pc.fees,pal.lease_move_in_date,lease_no into v_living_rent,v_parking_fees,v_move_in_date,v_lease_no
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

	BEGIN
		select max(invoice_no) into v_max_invoice_no from invoice_person_lease;

	EXCEPTION

		WHEN OTHERS THEN
			RETURN -1;
	END

+		ps.setString(6, paymentDateString); get move in date from lease + inputINcrementVal

v_payment_date := v_move_in_date + v_date_increment;


-- +		ps.setString(7, modeofPayment); from PERSON_ACC_STAFF


-- BEGIN

-- 	SELECT mode_of_payment into v_mode_of_payment from person_acc_staff where person_id = v_person_id and application_request_no = v_request_no;

-- EXCEPTION
-- 	WHEN NO_DATA_FOUND THEN
-- 		dbms_output.put_line('No data found for mode of payment from person_acc_staff');
-- 		v_mode_of_payment:=''	;


-- END




-- +		ps.setInt(8, newLeaseNumber); from person_accomodation_lease
-- +		ps.setString(9, paymentStatus); hardcode to 'OUTSTANDING' Refer constants.java
-- +		ps.setInt(10, totalPaymentDue); monthly_housing_rent+monthly_parking_rent+late_fees+incidental_charges

		v_total_payment_due := v_living_rent+v_parking_fees+v_late_fees+v_incidentalCharges;



-- +		ps.setInt(11, damageCharges); 0
-- +		ps.setInt(12, personID);




		BEGIN
				INSERT INTO invoice_person_lease values(v_living_rent,
														v_parking_fees,
														v_late_fees,
														v_incidentalCharges,
														v_max_invoice_no+1,
														v_payment_date,
														v_mode_of_payment,
														v_lease_no,
														v_payment_status,
														v_total_payment_due,
														v_damage_charges,
														v_person_id);

				COMMIT;

		EXCEPTION
			WHEN OTHERS THEN
				dbms_output.put_line('Insertion failed due to some reason')
				err_code := SQLCODE;
      			err_msg := SUBSTR(SQLERRM, 1, 200);
      			dbms_output.put_line('error_code: '||err_code||' error_message: '||err_msg);
		END;



END;
/