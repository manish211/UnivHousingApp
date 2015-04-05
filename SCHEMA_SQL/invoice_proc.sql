CREATE OR REPLACE procedure create_invoice(v_person_id IN NUMBER,
											v_accomodation_id IN NUMBER,
											v_date_increment IN NUMBER,
											v_request_no IN NUMBER,
											v_mode_of_payment IN person_acc_staff.mode_of_payment%TYPE,
											v_accomodation_type IN VARCHAR2,
											v_output OUT NUMBER)
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
   

   IF v_accomodation_type = "Family Apartment" THEN

	   	BEGIN

			select fa.monthly_rent, pal.lease_move_in_date,pal.lease_no
			from family_apartment fa,person_accomodation_lease pal
			where fa.accomodation_id = pal.accomodation_id
			and pal.accomodation_id = v_accomodation_id
			and pal.person_id = v_person_id;

		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				v_output=-1;
				return;

		END;

   ELSIF (v_accomodation_type = "Apartment") THEN --GENERAL APARTMENT

   		BEGIN

			select fa.monthly_rent, pal.lease_move_in_date,pal.lease_no
			from general_apartment ga,person_accomodation_lease pal
			where ga.accomodation_id = pal.accomodation_id
			and pal.accomodation_id = v_accomodation_id
			and pal.person_id = v_person_id;

		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				v_output=-1;
				return;

		END;

   ELSIF (v_accomodation_type = "Residence Hall") THEN

   		BEGIN

			select fa.monthly_rent, pal.lease_move_in_date,pal.lease_no
			from residence_hall_provides_room ra,person_accomodation_lease pal
			where ra.accomodation_id = pal.accomodation_id
			and pal.accomodation_id = v_accomodation_id
			and pal.person_id = v_person_id;

		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				v_output=-1;
				return;

		END;

   ELSIF (v_accomodation_type = "Bedroom") THEN

   		BEGIN

			select fa.monthly_rent, pal.lease_move_in_date,pal.lease_no
			from bedroom bd,person_accomodation_lease pal
			where bd.accomodation_id = pal.accomodation_id
			and pal.accomodation_id = v_accomodation_id
			and pal.person_id = v_person_id;

		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				v_output=-1;
				return;

		END;

	END; --END OF IF CONDITION

	BEGIN
		select fa.monthly_rent,pc.fees,pal.lease_move_in_date,lease_no into v_living_rent,v_parking_fees,v_move_in_date,v_lease_no
		from family_apartment fa,parking_spot_has_class pc,personparkingspot_relation pr,person_accomodation_lease pal
		where pr.person_id = v_person_id
		and pr.person_id = pal.person_id
		and pal.accomodation_id = fa.accomodation_id
		and fa.accomodation_id = v_accomodation_id
		and pr.spot_no = pc.spot_no;
		 
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			v_output=-1;

	END;


	BEGIN
		select max(invoice_no) into v_max_invoice_no from invoice_person_lease;

	EXCEPTION

		WHEN OTHERS THEN
			v_output=-1;
	END

	v_payment_date := v_move_in_date + v_date_increment;


	v_total_payment_due := v_living_rent+v_parking_fees+v_late_fees+v_incidentalCharges;

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
  			v_output= -1;
	END;


END;
/