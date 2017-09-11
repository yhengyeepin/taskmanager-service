# taskmanager-service
Task Management Service - Spring REST Data, Docker

# Local Development
docker run -d -v /Users/ryan/Documents/projects/backend/database/data/:/var/lib/mysql --name db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=taskmanager mysql:8.0.2
mvn -P DEV/STAGING spring-boot:run

# Deploy service to bluemix kubernetes
1. mvn clean package

//build to local image
2. mvn install dockerfile:build 

//build to bluemix
3. docker build -t registry.ng.bluemix.net/yheng/taskmanager-service:latest .  
4. docker push registry.ng.bluemix.net/yheng/taskmanager-service:latest

5. kubectl create/delete -f kubernetes.yml
6. kubectl get pods,services,deployments

//check cluster ip
7. bx cs workers mycluster 
8. Import database  from taskmanager-database/schema

# Local docker build
docker-compose build --no-cache
Docker-compose up/down
