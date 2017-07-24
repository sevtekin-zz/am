echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -nsu -f ../../../com.sevtekin.am.build/build.service &&
cp -r ../../service/bin/ . &&
echo "---> STOPPING CONTAINER" &&
docker stop service || true &&
echo "---> REMOVING CONTAINER"
docker rm service || true &&
echo "---> BUILDING IMAGE" &&
docker build -m 200M --memory-swap=200M -t ci/service . &&
echo "---> RUNNING CONTAINER" &&
docker run --restart=always -i -t -d -p 5001:8443 --name=service -v /shared/am/prod/db/snapshots:/snapshots -v /etc/localtime:/etc/localtime:ro ci/service &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
