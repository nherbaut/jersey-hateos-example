.DEFAULT_GOAL: all

all: build docker 

build:
	mvn clean package

docker:
	docker build . -t nherbaut/smart-ms-stub

run:
	docker run -p 8080:8080 nherbaut/smart-ms-stub
