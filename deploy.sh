#!/bin/bash

# 폴더 이름 추출
FOLDER_NAME=$(basename $(dirname $(realpath "$0")))

# 도커 정지 및 삭제 함수
function docker_cleanup {
  local container="$1"

  if sudo docker ps -a --format '{{.Names}}' | grep -Eq "^${container}\$"; then
    echo "Stopping and removing Docker container for $container"
    sudo docker stop "$container" && sudo docker rm "$container"
  else
    echo "Can't stop and remove Docker container for $container: Not found"
  fi
}

# 도커 이미지 삭제 함수
function docker_image_remove {
  local image="$1"

  if sudo docker images --format '{{.Repository}}' | grep -Eq "^${image}\$"; then
    echo "Removing Docker image for $image"
    sudo docker rmi "$image"
  else
    echo "Can't remove Docker image for $image: Not found"
  fi
}

# 스프링 부트 빌드 함수
function build_spring {
  local build_path="$1"

  echo "Building Spring Boot for $build_path"
  ./"$build_path"/gradlew clean build
}

# GIT 최신화
git pull origin dev

# 인자로 전달된 서비스 처리
for service in "$@"; do
  docker_cleanup "$service"
done

# 해당 서버의 도커 이미지 삭제
for service in "$@"; do
  docker_image_remove "$FOLDER_NAME-$service"
done

# 해당 스프링부트를 빌드
for service in "$@"; do
  build_spring "$(echo "$service" | cut -d"-" -f 1)"
done

# NGINX 컨테이너 중지 및 이미지 삭제
docker_cleanup "${FOLDER_NAME}-nginx-1" && docker_image_remove "nginx:stable-perl"

# NGINX 이미지 PULL
sudo docker pull nginx:stable-perl

# DOCKER-COMPOSE 빌드 및 실행
sudo docker-compose up -d

echo "Docker Compose has started. Check docker-compose.log for details."
