---------------------------------------------------------------
--This script contains table creation sql files for all tables
--used by University Housing Software
---------------------------------------------------------------

CREATE TABLE  Person(person_id INTEGER, 
 first_name varchar2(20),
 last_name varchar2(20),  
 street_no  varchar2(20),
 city  varchar2(20),
 postcode  INTEGER,
 phone_number INTEGER,
 gender varchar2(10),
 DOB date,
 nationality varchar2(20),
 smoker varchar2(50),
 special_needs varchar2(100),
 housing_status varchar2(10),
 study_field varchar2(50),
 alternate_phone_no INTEGER,
 comments varchar2(100),
 country varchar2(50),
PRIMARY KEY(person_id));



CREATE TABLE  Student(student_id INTEGER,
 person_id INTEGER,
 student_type varchar2(10),
 member_one varchar2(20),
 member_two varchar2(20),
 member_three varchar2(20),
 member_four varchar2(20),
 dob_member_one date,
 dob_member_two date,
 dob_member_three date,
 dob_member_four date,
 PRIMARY KEY(student_id),
 FOREIGN KEY(person_id) REFERENCES Person);


CREATE TABLE  Guest (approval_id INTEGER,
 person_id INTEGER,
 PRIMARY KEY(approval_id),
 FOREIGN KEY(person_id) REFERENCES Person);


drop table Housing_Staff cascade constraints;	
CREATE TABLE Housing_Staff (ticket_no  varchar2(10),
 staff_no  INTEGER,
 first_name  varchar2(20), 
 last_name  varchar2(20),
 sex varchar(6), 
 DOB date, 
 position varchar2(15),
 Street  varchar2(20),
 city  varchar2(20),
 postcode  INTEGER,
 PRIMARY KEY (staff_no));

drop table lease cascade constraints;
CREATE TABLE Lease(
lease_no INTEGER,
 deposit varchar2(10), 
mode_of_payment varchar2(10),
duration varchar2(10),
cutoff_date date,
PRIMARY KEY (lease_no));

CREATE TABLE Termination_Requests (
reason varchar2(50),
termination_request_number INTEGER,
status varchar(15),
termination_date date,
inspection_date date,
person_id INTEGER,
staff_no INTEGER,
PRIMARY KEY (termination_request_number),
FOREIGN KEY (person_id) REFERENCES Person,
FOREIGN KEY (staff_no) REFERENCES Housing_Staff);

CREATE TABLE lease_housing_staff_manage(
lease_no INTEGER,
duration VARCHAR2(40),
cutoff_date DATE,
mode_of_payment VARCHAR2(40),
deposit NUMBER(9,2),
date_of DATE,
staff_no INTEGER NOT NULL,
PRIMARY KEY(lease_no),
FOREIGN KEY(staff_no) REFERENCES housing_staff);


CREATE TABLE residence_hall(
hall_name VARCHAR2(40),
phone_number  INTEGER,
available_for  VARCHAR2(40),
hall_number INTEGER,
street_name VARCHAR2(40),
city_name VARCHAR2(50),
zip_code INTEGER,
PRIMARY KEY(hall_number));


CREATE TABLE Family_Apartment(
apt_no INTEGER,
apt_type VARCHAR2(20),   
accomodation_id INTEGER,
street_name VARCHAR2(40),				
city_name VARCHAR2(50),
zip_code INTEGER,
monthly_rent NUMBER(9,2),
PRIMARY KEY(apt_no),
FOREIGN KEY(accomodation_id) REFERENCES Accomodation);


CREATE TABLE General_Apartment(
apt_no INTEGER,
apt_type VARCHAR2(20),
no_of_bedrooms INTEGER,
no_of_bathrooms INTEGER,
accomodation_id INTEGER,
street_name VARCHAR2(40),
city_name VARCHAR2(50),
zip_code INTEGER,
society_name VARCHAR2(300),
PRIMARY KEY(apt_no,society_name),
FOREIGN KEY(accomodation_id) REFERENCES Accomodation);

CREATE TABLE residence_hall_provides_room(
residence_place_no INTEGER,
accomodation_id INTEGER,
hall_number INTEGER NOT NULL,
room_no INTEGER,
monthly_rent_rate NUMBER(9,2),
PRIMARY KEY(residence_place_no),
FOREIGN KEY(hall_number) REFERENCES residence_hall,
FOREIGN KEY(accomodation_id) REFERENCES Accomodation);



drop table bedroom cascade constraints;
CREATE TABLE bedroom(
bedroom_place_no INTEGER,
accomodation_id INTEGER,
room_no INTEGER,
monthly_rent_rate NUMBER(10),
apt_no INTEGER NOT NULL,
apt_place_no INTEGER,
PRIMARY KEY(apt_place_no),
FOREIGN KEY(accomodation_id) REFERENCES Accomodation);

drop table parking_lot cascade constraints;
Create table parking_lot(
lot_no number(5) ,
zip_code INTEGER,
PRIMARY KEY(lot_no));

drop table parking_lot_apt cascade constraints;

Create table parking_lot_apt(
lot_no INTEGER ,
apt_no INTEGER,
 PRIMARY KEY(lot_no,apt_no));


drop table parking_lot_residence_hall cascade constraints;
Create table parking_lot_residence_hall(
lot_no INTEGER ,
hall_number INTEGER,
 PRIMARY KEY(lot_no,hall_number));




CREATE TABLE parkingSpot_belongs_parkingLot(
lot_no INTEGER,
spot_no INTEGER,
PRIMARY KEY(spot_no),
availability varchar2(10),
permit_id INTEGER UNIQUE,
zip_code INTEGER,
FOREIGN KEY(lot_no) REFERENCES parking_lot);

CREATE TABLE parkingSpot_parkingLot(
lot_no INTEGER,
spot_no INTEGER,
PRIMARY KEY(spot_no));

Create table parking_spot_has_class(
vehicle_type varchar2(50),
fees number(7,2),
spot_no number(5), 
PRIMARY KEY (spot_no,vehicle_type),
FOREIGN KEY (spot_no) REFERENCES parkingSpot_parkingLot);

CREATE TABLE invoice_person_lease(
monthly_housing_rent number(9,2),
monthly_parking_rent number(9,2),
late_fees number(9,2),
incidental_charges number(9,2),
invoice_no number(10),
payment_date DATE,
payment_method varchar2(20),
lease_no INTEGER NOT NULL,
payment_status VARCHAR2(20),
payment_due NUMBER(9,2),
damage_charges NUMBER(9,2),
person_id INTEGER NOT NULL,
PRIMARY KEY(invoice_no),
FOREIGN KEY(lease_no) REFERENCES lease,
FOREIGN KEY(person_id) REFERENCES person);

CREATE TABLE ticket_person_staff(
staff_no INTEGER NOT NULL,
ticket_no INTEGER, 
ticket_status varchar2(10), 
ticket_severity varchar2(10),
description varchar2(200),
ticket_type varchar2(20),
PRIMARY KEY(ticket_no),
person_id INTEGER NOT NULL,
FOREIGN KEY(staff_no) REFERENCES housing_staff,
FOREIGN KEY(person_id) REFERENCES person); 

CREATE TABLE person_accomodation_lease( 
accomodation_id INTEGER,
person_id INTEGER,
lease_no INTEGER,
accomodation_type VARCHAR2(40),
permit_id INTEGER,
lease_move_in_date date,
PRIMARY KEY(person_id),
FOREIGN KEY(person_id) REFERENCES person,
FOREIGN KEY(lease_no) REFERENCES lease);

	
CREATE TABLE PERSON_ACC_STAFF (
application_request_no INTEGER,
accomodation_type VARCHAR2(50),
person_id INTEGER,
staff_no INTEGER,
request_status VARCHAR2(50),
mode_of_payment VARCHAR2(20),
lease_move_in_date DATE,
duration VARCHAR2(10),
PREFERENCE1 VARCHAR2(50),
PREFERENCE2 VARCHAR2(50),
PREFERENCE3 VARCHAR2(50),
payment_gateway varchar2(20),
PRIMARY KEY(application_request_no),
FOREIGN KEY (person_id) REFERENCES person);


CREATE TABLE KIN_STUDENT(
student_id INTEGER NOT NULL,
first_name VARCHAR2(40),
last_name VARCHAR2(40),
street_name VARCHAR2(50),
city_name VARCHAR2(40),
zip_code INTEGER,
phone_number INTEGER,
gender varchar2(10),
DOB date,
PRIMARY KEY(student_id,first_name),
FOREIGN KEY(student_id) REFERENCES student);

CREATE TABLE hall_manages(
hall_name VARCHAR2(40),
phone_number  INTEGER,
available_for  VARCHAR2(40),
hall_number INTEGER,
street_name VARCHAR2(40),
city_name VARCHAR2(50),
zip_code INTEGER,
staff_no INTEGER,
PRIMARY KEY(hall_number),
FOREIGN KEY(staff_no) REFERENCES housing_staff);


CREATE TABLE login_credentials(
password VARCHAR2(20),
login_id INTEGER,
designation VARCHAR2(20),
PRIMARY KEY(login_id),
);

CREATE TABLE PersonParkingSpot_Relation (
lot_no INTEGER,
spot_no INTEGER,
student_id INTEGER,
request_status VARCHAR2(20),
request_no INTEGER,
PRIMARY KEY(request_no),
FOREIGN KEY(lot_no) REFERENCES parking_lot,
FOREIGN KEY(spot_no) REFERENCES parkingSpot_parkingLot,
FOREIGN KEY(student_id) REFERENCES Student);

CREATE TABLE Accomodation (
accomodation_id INTEGER,
accomodation_type VARCHAR2(20),
PRIMARY KEY(accomodation_id))
;



CREATE TABLE Private_Accomodation(
accomodation_id INTEGER,
accomodation_type varchar2(50),
private_place_no INTEGER,
street_name varchar2(50),
city varchar2(50),
post_code INTEGER,
PRIMARY KEY(private_place_no),
FOREIGN KEY(accomodation_id) REFERENCES Accomodation);

--creating sequence for ticket_person_staff

-- create sequence ticket_seq_no
-- 	start with 32
-- 	increment by 1
-- 	maxvalue 10000
-- 	nocycle;


