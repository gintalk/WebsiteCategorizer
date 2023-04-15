# WebsiteCategorizer

This service has two exposed APIs:
- /web-texts: receives a single parameter called `url` pointing to a website on the Internet, returns the page source
cleaned of html tags and scripts
- /web-texts/categories: receives multiple `url` parameters, returns the categories which each URL falls into

# Deploying to a local Kubernetes Cluster, with the help of `minikube`, `kubectl` and `docker`

## Start `minikube` and enable `ingress` addons

Type these commands in a terminal on the host machine:
```
minikube start
minikube addons enable ingress
```

## Deploy app container to a `minikube` cluster

Type this command in the IDE terminal:

`kubectl apply -f deployment/app-deployment.yaml`

The current deployment configuration has a replication factor of 3, which would provide
decent fault tolerance capacity for our application

## Deploy a load balancing service to balance the load among our replicas

While still in the IDE terminal, type this command:

`kubectl apply -f deployment/load-balancer-deployment.yaml`

## Open a connection from the host machine to the load balancing service

Run this command on a separate terminal on the host machine and leave it there:

`minikube tunnel`

Now we can access the app via http://127.0.0.1:8080