echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -nsu -f ../../../com.sevtekin.am.build/build.w3 &&
cp -r ../../w3/bin/ . &&
echo "---> STOPPING CONTAINER" &&
docker stop w3 || true &&
echo "---> REMOVING CONTAINER"
docker rm w3 || true &&
echo "---> BUILDING IMAGE" &&
docker build -m 200M --memory-swap=200M -t ci/w3 . &&
echo "---> RUNNING CONTAINER" &&
docker run --restart=always -i -t -d -p 5002:8443 --name=w3 -v /etc/localtime:/etc/localtime:ro ci/w3 &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
