# Clone The paypal-topup-central repository

`sudo apt -y install git`

`git clone https://github.com/miirochristopher/paypal-topup-central.git`

# Create and access the Ubuntu Server 20.04 Box

`cd paypal-topup-central`

`vagrant up`

`vagrant ssh`

# Install Docker CE and other requirements

There are few dependencies needed to configure Docker repositories and do the actual package installation. 

```sh
sudo apt -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```

# Install Docker CE on Ubuntu 22.04|20.04|18.04

Import Docker repository GPG key:

```sh
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

Add Docker CE repository to Ubuntu

```sh
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

Finally install Docker CE on Ubuntu 20.04:

`sudo apt -y update`

`sudo apt -y install docker-ce docker-ce-cli containerd.io`

Add your user account to docker group.

`sudo usermod -aG docker $USER`

`newgrp docker`

# Paypal-topup-central

The requirements include Java 8, Apache Maven 3.6.0. Other dependencies are PostgreSQL, Keycloak and Consul all running in docker containers.

Install java, then run the commands below to create the docker containers for the backend dependencies.

# Install Java 8

`sudo apt -y install openjdk-8-jdk`

# Create the Network

Create a user defined network

`docker network create keycloak-network`

# Start a PostgreSQL instance

```sh
docker run -d --name postgres -p 5432:5432 --net keycloak-network -e POSTGRES_DB=keycloak -e POSTGRES_USER=keycloak -e POSTGRES_PASSWORD=password -e PGDATA=/var/lib/postgresql/data/pgdata -v postgres-data:/var/lib/postgresql/data postgres
```

# Start a Keycloak instance

Start a Keycloak instance and connect to the PostgreSQL instance:

```sh 
docker run -d --name keycloak --net keycloak-network -p 8081:8080 -e KEYCLOAK_USER=pal-admin -e KEYCLOAK_PASSWORD=password -e KEYCLOAK_DEFAULT_THEME=keycloak -e DB_VENDOR=POSTGRES -e DB_ADDR=192.168.99.75:5432 -e DB_PORT=5432 -e DB_DATABASE=keycloak -e DB_USER=keycloak -e DB_PASSWORD=password jboss/keycloak
```

## Access Keycloak

`http://192.168.99.75:8081/auth/`

`KEYCLOAK_USER: pal-admin KEYCLOAK_PASSWORD: password`

## Keycloak Config

Click on Administration Console, then login with KEYCLOAK_USER and KEYCLOAK_PASSWORD credentials. 

Go to the bottom of the pannel and click on Import, then navigate to the location of the realm-export.json file sent via email.

# Consul Server

```sh
docker run -d -p 8500:8500 -p 8600:8600 --name consul-server-zone1 -v consul-server-zone1-data:/consul/data -v consul-server-zone1-config:/consul/config consul agent -server -ui -node=server-zone1 -bootstrap-expect=1 -client=0.0.0.0
```

Get Consul IP Address [ NOTE: -join = < ip-address > for consul client ]

`docker exec consul-server-zone1 consul members`

# Consul Client

```sh
docker run -d --name=consul-client-zone1 -v consul-client-zone1-data:/consul/data consul agent -node=client-zone1 -join=172.17.0.2
```

## Access Consul

`http://192.168.99.75:8500/ui/dc1/services`

## Consul Config

Go to `http://192.168.99.75:8500/ui/dc1/kv` Click on Create and type: `config/monolith/data` in the Key or Folder field. Paste the contents of the `YAML` file provided via email. Select `YAML` On the top right-hand-side and click Save.

# Go To Project Folder

`cd topup/`

# Compile The Code.

`sudo apt -y install maven`

`mvn clean install`

# Run The Project

`mvn spring-boot:run`

# API Root URL

`http://192.168.99.75:1055/swagger-ui.html`

# PayPal Payments Form

`http://192.168.99.75:1055/payment`

