#!/bin/bash

# List all your service directories
services=("configserver" "eurekaserver" "gateway" "notification" "order" "product" "user")

for service in "${services[@]}"; do
  echo "===================================="
  echo "Cleaning and building $service..."
  echo "===================================="

  # Enter the service directory
  cd $service || { echo "Directory $service not found"; exit 1; }

  # Clean and build the service, skipping tests for speed
  ./mvnw clean package -DskipTests

  # Return to the root directory
  cd ..
done

echo "All services cleaned and built successfully!"
