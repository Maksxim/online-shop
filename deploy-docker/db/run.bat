docker run -p 3306:3306 --name online-shop-mysql --network online-shop-network -e MYSQL_ROOT_PASSWORD=admin0975 -e MYSQL_DATABASE=online_shop_db -d mysql:8.0.32