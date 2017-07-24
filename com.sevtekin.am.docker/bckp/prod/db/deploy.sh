echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -nsu -f ../../../com.sevtekin.am.build/build.data &&
echo "---> STOPPING CONTAINER" &&
docker stop db || true &&
echo "---> REMOVING CONTAINER"
docker rm db || true &&
echo "---> BUILDING IMAGE" &&
docker build -m 200M -t ci/db . &&
echo "---> RUNNING CONTAINER" &&
docker run --restart=always -i -t -d -p 5005:3306 --name=db -v /shared/am/prod/db:/var/lib/mysql -v /etc/localtime:/etc/localtime:ro ci/db &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
