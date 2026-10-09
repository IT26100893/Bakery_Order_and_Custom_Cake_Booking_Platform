-- Demo logins (admin@bakery.lk / nimal@mail.com) are created by DataInitializer on start-up.

-- Products with specific image mappings
INSERT IGNORE INTO products VALUES ('P001','BREAD','Sliced Bread 550g',180.00,10,'Wheat');
INSERT IGNORE INTO products VALUES ('P002','BREAD','Cheese Sandwich',250.00,3,'Cheese');
INSERT IGNORE INTO products VALUES ('P003','BREAD','Dinner Bun',140.00,15,'Plain');
INSERT IGNORE INTO products VALUES ('P004','BREAD','Sourdough Loaf',650.00,20,'Sourdough');

INSERT IGNORE INTO bookings(id,customer_name,phone,flavour,size_kg,message,pickup_date,status) VALUES ('BKA1B2C','Nimal Perera','0771234567','Vanilla',2.0,'Happy Birthday Amaya','2026-11-15','PENDING');