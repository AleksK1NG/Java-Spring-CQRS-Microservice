.PHONY:

# ==============================================================================
# Docker

local:
	@echo Clearing kafka data
	rm -rf ./kafka_data
	@echo Clearing zookeeper data
	rm -rf ./zookeeper
	@echo Starting local docker compose
	docker-compose -f docker-compose.yaml up -d --build

clean_docker_data:
	@echo Clearing kafka data
	rm -rf ./kafka_data
	@echo Clearing zookeeper data
	rm -rf ./zookeeper


# ==============================================================================
# Docker support

FILES := $(shell docker ps -aq)

down-local:
	docker stop $(FILES)
	docker rm $(FILES)

clean:
	docker system prune -f

logs-local:
	docker logs -f $(FILES)