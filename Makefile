.PHONY: run

run:
	@./gradlew --stop
	@echo "Running ./gradlew docker"
	@./gradlew docker -Pprofile=local
	@echo "Running docker-compose up -d"
	@docker-compose up -d
