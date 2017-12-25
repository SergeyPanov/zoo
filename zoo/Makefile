all: setup-pathes assemble

setup-pathes:
	mvn install:install-file -Dfile=./lib/ext/ordim.jar -DgroupId=local.group.ordim -DartifactId=local.artifact.ordim -Dversion=LATEST -Dpackaging=jar
	mvn install:install-file -Dfile=./lib/ext/sdoapi.jar -DgroupId=local.group.sdoapi -DartifactId=local.artifact.sdoapi -Dversion=LATEST -Dpackaging=jar
	mvn install:install-file -Dfile=./lib/ext/ojdbc6.jar -DgroupId=local.group.ojdbc6 -DartifactId=local.artifact.ojdbc6 -Dversion=LATEST -Dpackaging=jar
assemble:
	mvn package -DskipTests

run:
	java -jar target/zoo-0.0.1-SNAPSHOT.jar

clear:
	rm -r target/
