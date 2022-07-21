POM_VERSION = $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
version:
	echo $(POM_VERSION)
clean:
	mvn -Dmaven.artifact.threads=10 clean

compile:
	mvn compile

package:
	mvn clean package -Dusername=$(token)

verify: clean
	mvn -Dmaven.artifact.threads=30 verify -Dusername=$(token) -Dgpg.skip=true -Dmaven.repo.local=$(mountPath)

build-artifacts: verify
	cd devops && make build-artifacts version=$(POM_VERSION)

local-verify: clean
	mvn verify

publish-artifacts: build-artifacts
	cd devops && make build-package version=$(POM_VERSION)

deploy-artifacts:
	cd devops && make deploy-image version=$(POM_VERSION)