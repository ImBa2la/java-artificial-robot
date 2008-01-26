create table car_v as
select IF(currency_type = 'USD', price * 24.5, price) as price,  mark, 
engine_type, body_type, gear_type, steering_wheel, transmission,
run, year from car
where
resource_code  = 'www.cars.auto.ru' and
mark in ('AUDI', 'BMW', 'CHEVROLET', 'CHRYSLER', 'DAEWOO', 'DODGE', 'FORD',  'GAZ', 'HONDA', 'HYUNDAI', 'KIA',
'LEXUS', 'MAZDA', 'MERCEDES', 'MITSUBISHI', 'NISSAN', 'OPEL', 'PEUGEOT', 'PORSCHE', 'RENAULT', 'SUBARU', 'TOYOTA', 'VAZ', 'VOLKSWAGEN', 'VOLVO') and
body_type in ('ALLROAD', 'COUPE', 'HATCHBACK', 'MINI_WEN', 'SEDAN', 'UNIVERSAL') and
engine_type in ('CARBURETOR_GASOLINE', 'DIESEL_SUPERCHARGING_GASOLINE', 'INJECTOR_GASOLINE', 'TURBO_SUPERCHARGING_GASOLINE') and
gear_type in ('ALL_WHEEL_DRIVE', 'FORWARD_CONTROL') and
steering_wheel in ('LEFT', 'RIGHT') and
transmission in ('AUTOMATIC', 'MECHANICAL') and
run_metric = 'KILOMETRE'
order by rand()
limit 1000

drop table if exists car_v;

create table car_v as
select IF(currency_type = 'USD', price * 24.5, price) as price,  mark, 
engine_type, body_type, gear_type, steering_wheel, transmission, 
exp(-log(2)*(1 - (displacement-1000)/5000)/((5-1.4)/5)) as displacement,
exp(-log(2)* (IF(run_metric = 'THOUSAND_KILOMETRE', run * 1000, run))/1000000) as run, exp(-log(2)*(2007 - year)/6) as year from car
where
IF(currency_type = 'USD', price * 24.5, price) between 100000 and 5000000 and
currency_type in ('RUBLE', 'USD') and
state in ('EXCELLENT') and
resource_code  = 'www.cars.auto.ru' and
mark in ('AUDI', 'BMW', 'CHEVROLET', 'CHRYSLER', 'DAEWOO', 'DODGE', 'FORD',  'GAZ', 'HONDA', 'HYUNDAI', 'KIA',
'LEXUS', 'MAZDA', 'MERCEDES', 'MITSUBISHI', 'NISSAN', 'OPEL', 'PEUGEOT', 'PORSCHE', 'RENAULT', 'SUBARU', 'TOYOTA', 'VAZ', 'VOLKSWAGEN', 'VOLVO') and
body_type in ('ALLROAD', 'COUPE', 'HATCHBACK', 'MINI_WEN', 'SEDAN', 'UNIVERSAL') and
engine_type in ('CARBURETOR_GASOLINE', 'DIESEL_SUPERCHARGING_GASOLINE', 'INJECTOR_GASOLINE', 'TURBO_SUPERCHARGING_GASOLINE') and
gear_type in ('ALL_WHEEL_DRIVE', 'FORWARD_CONTROL') and
steering_wheel in ('LEFT', 'RIGHT') and
transmission in ('AUTOMATIC', 'MECHANICAL') and
run_metric in ('KILOMETRE', 'THOUSAND_KILOMETRE') and
displacement between 1000 and 6000 and
(IF(run_metric = 'THOUSAND_KILOMETRE', run * 1000, run) > 20000 or year >= 2006 )
order by rand()
limit 1000;