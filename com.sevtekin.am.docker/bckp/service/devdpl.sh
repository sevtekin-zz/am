echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -nsu -f ../../com.sevtekin.am.build/build.service &&
echo "---> STOPPING CONTAINER" &&
docker stop servicedev 2>&1 >/dev/null &&
echo "---> REMOVING CONTAINER"
docker rm servicedev 2>&1 >/dev/null &&
echo "---> BUILDING IMAGE" &&
docker build -f devbuild -m 200M --memory-swap=200M -t ci/servicedev . &&
echo "---> RUNNING CONTAINER" &&
docker run -i -t -d -p 6001:8443 --name=servicedev -v /shared/am/prod/db/snapshots:/snapshots ci/servicedev &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
