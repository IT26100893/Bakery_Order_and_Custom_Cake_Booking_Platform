# Crumb & Co. - Bakery Order and Custom Cake Booking Platform
Spring Boot + Thymeleaf, data stored in text files under `data/`.
Run: `mvn spring-boot:run` (or run BakeryOrderSystemApplication in IntelliJ) then open http://localhost:8080
CRUD modules: Users, Products, Custom cake bookings, Orders.
OOP: abstract User->Customer/Admin, abstract BakeryProduct->Cake/Pastry/Bread, abstract Payment->Cash/Card/Online (polymorphic fee), generic FileRepository<T>.

## Logins
- Admin: admin@bakery.lk / admin123  -> /admin (dashboard, products, orders, custom cakes, users)
- Customer: nimal@mail.com / nimal123 -> /customer (own orders, own cake bookings, profile)
- New customers can register at /register (role is always CUSTOMER).
