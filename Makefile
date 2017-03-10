containerName := approval.mysql
imageName := $(shell docker images -q $(containerName))
all: start
build: rmi
	docker build -t $(containerName) .
	run
run: 
	docker run -d -p 3306:3306 --name $(containerName) $(containerName)
start: stop
	docker start "$(containerName)"
stop:
	docker stop "$(containerName)" || true;
rm: stop
	docker rm $(containerName) || true;
getcontainername:
	containerName:=$(docker images -q $(containerName))
rmi: rm
	docker rmi -f $(containerName) || true;
dbclean: rm run
