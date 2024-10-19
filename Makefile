.PHONY: run

run:
	@echo "Running ./gradlew docker"
	@./gradlew docker -Pprofile=local
	@echo "Running docker-compose up -d"
	@docker-compose up -d
