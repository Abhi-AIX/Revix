# Docker Interview Questions

A comprehensive guide covering standard, intermediate, and tricky Docker questions — plus how to think about Docker in the modern AI era.

---

## Standard Questions (Beginner)

### 1. What is Docker?
**Answer:** Docker is a platform for developing, shipping, and running applications in containers. Containers package an application with all its dependencies, ensuring it runs consistently across different environments.

### 2. What is the difference between a Container and a Virtual Machine?

| Container | Virtual Machine |
|-----------|-----------------|
| Shares host OS kernel | Has its own OS |
| Lightweight (MBs) | Heavy (GBs) |
| Starts in seconds | Starts in minutes |
| Less isolation | Full isolation |
| Uses Docker Engine | Uses Hypervisor |

### 3. What is a Docker Image vs Container?
- **Image:** A read-only template with instructions to create a container (like a class in OOP)
- **Container:** A running instance of an image (like an object in OOP)

### 4. What is a Dockerfile?
A text file containing instructions to build a Docker image. Common instructions:
- `FROM` — base image
- `COPY` — copy files into image
- `RUN` — execute commands during build
- `CMD` — default command when container starts
- `EXPOSE` — document which port the app uses

### 5. What is Docker Hub?
A cloud-based registry where Docker images are stored and shared. Like GitHub for Docker images.

### 6. Basic Docker Commands?
```bash
docker pull <image>        # Download image
docker run <image>         # Run container
docker ps                  # List running containers
docker ps -a               # List all containers
docker stop <container>    # Stop container
docker rm <container>      # Remove container
docker images              # List images
docker rmi <image>         # Remove image
```

### 7. What is Docker Compose?
A tool to define and run multi-container applications using a YAML file. Instead of running multiple `docker run` commands, you define everything in `docker-compose.yml` and run `docker compose up`.

---

## Intermediate Questions

### 8. What are Docker Volumes? Why use them?
Volumes persist data outside the container's filesystem.

**Why:**
- Data survives container restarts/removal
- Share data between containers
- Better performance than bind mounts

**Types:**
- Named volumes (`postgres-data:/var/lib/data`)
- Bind mounts (`./local:/container/path`)
- tmpfs mounts (in-memory)

### 9. Explain Docker Networking
Docker provides different network drivers:

| Driver | Use Case |
|--------|----------|
| `bridge` | Default; containers on same host communicate |
| `host` | Container uses host's network directly |
| `none` | No networking |
| `overlay` | Multi-host communication (Swarm) |

Containers in same network can reach each other by service name.

### 10. What is the difference between CMD and ENTRYPOINT?

| CMD | ENTRYPOINT |
|-----|------------|
| Default command, easily overridden | Main executable, harder to override |
| `docker run image newcmd` replaces CMD | `docker run image args` appends to ENTRYPOINT |

**Best practice:** Use ENTRYPOINT for the main executable, CMD for default arguments.

### 11. How do you reduce Docker image size?
1. Use smaller base images (`alpine` instead of `ubuntu`)
2. Multi-stage builds
3. Combine RUN commands to reduce layers
4. Use `.dockerignore` to exclude unnecessary files
5. Remove cache/temp files in same RUN command

### 12. What is a multi-stage build?
Building in multiple stages to keep final image small:

```dockerfile
# Stage 1: Build
FROM maven:3.8 AS builder
COPY . .
RUN mvn package

# Stage 2: Run (only copy the JAR)
FROM openjdk:17-slim
COPY --from=builder /target/app.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
```

### 13. What is the difference between COPY and ADD?
- `COPY` — Simple copy from host to container
- `ADD` — Can also extract tar files and fetch URLs

**Best practice:** Use `COPY` unless you need ADD's extra features.

### 14. How does Docker handle logging?
- Default: logs written to JSON files on host
- View logs: `docker logs <container>`
- Log drivers: json-file, syslog, journald, awslogs, etc.
- Configure in daemon.json or per-container

### 15. What is Docker Healthcheck?
Monitors if a container is working correctly:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
  interval: 30s
  timeout: 10s
  retries: 3
```

---

## Tricky Questions

### 16. Container exited immediately. How do you debug?
```bash
docker logs <container>              # Check logs
docker run -it <image> /bin/sh       # Run interactively
docker inspect <container>           # Check exit code
docker run --rm <image> cat /etc/os-release  # Quick test
```

### 17. What happens when you run `docker run`?
1. Docker checks if image exists locally
2. If not, pulls from registry
3. Creates a new container from image
4. Allocates filesystem and mounts volumes
5. Creates network interface
6. Sets up IP address
7. Executes specified command (CMD/ENTRYPOINT)

### 18. How do you pass secrets to containers securely?
**Bad:** Hardcode in Dockerfile or image  
**Good:**
- Environment variables (ok for non-sensitive)
- Docker secrets (Swarm mode)
- Mount secret files as volumes
- Use external secret managers (Vault, AWS Secrets Manager)

### 19. What is the PID 1 problem in Docker?
The first process (PID 1) in a container should handle:
- Signal forwarding (SIGTERM, etc.)
- Reaping zombie processes

**Problem:** Many apps don't handle this properly.  
**Solution:** Use `--init` flag or tini as init system.

### 20. Explain Docker layer caching. Why does order in Dockerfile matter?
Docker caches each instruction as a layer. If a layer changes, all subsequent layers rebuild.

**Bad order:**
```dockerfile
COPY . .                    # Changes often
RUN npm install             # Reinstalls every time
```

**Good order:**
```dockerfile
COPY package*.json .        # Changes rarely
RUN npm install             # Cached unless package.json changes
COPY . .                    # Only this rebuilds on code change
```

### 21. What is the difference between `docker compose up` and `docker compose run`?
- `up` — Starts all services defined in compose file
- `run` — Runs a one-off command against a service (doesn't start dependencies by default)

### 22. How would you run a database migration before the app starts?
Options:
1. Use `depends_on` with healthcheck condition
2. Init container / sidecar pattern
3. Entrypoint script that waits for DB then runs migration
4. Separate migration container that runs first

### 23. What is the difference between `expose` and `ports` in docker-compose?
- `expose` — Makes port available to other containers only (internal)
- `ports` — Maps port to host (external access)

### 24. Your container works locally but fails in production. How do you debug?
1. Compare environment variables
2. Check resource limits (memory, CPU)
3. Verify network connectivity
4. Check volume mounts and permissions
5. Review logs and healthcheck status
6. Compare Docker/image versions

### 25. How do you handle timezone in containers?
```yaml
environment:
  - TZ=Asia/Kolkata
volumes:
  - /etc/localtime:/etc/localtime:ro
```

---

## Docker in the Modern AI Era

### Why Docker Still Matters (2025+)

1. **AI/ML Workloads**
   - Package ML models with dependencies (PyTorch, TensorFlow)
   - Reproducible training environments
   - GPU support via NVIDIA Container Toolkit
   - Consistent inference across dev/staging/prod

2. **Microservices & Cloud Native**
   - Kubernetes runs containers (Docker images)
   - Serverless (AWS Lambda, Cloud Run) uses containers
   - GitOps and CI/CD pipelines rely on containers

3. **Development Environments**
   - Dev Containers in VS Code / JetBrains
   - Consistent environments across team
   - AI coding assistants (like GitHub Copilot) can generate Dockerfiles

### How to Think About Docker Today

| Old Thinking | Modern Thinking |
|--------------|-----------------|
| "Containers are for ops" | "Containers are for everyone" |
| "VM alternative" | "Application packaging standard" |
| "Docker Swarm vs K8s" | "Docker for build, K8s for orchestration" |
| "Manual Dockerfile writing" | "AI-assisted + best practice templates" |

### Key Skills to Focus On

1. **Fundamentals** — Images, containers, volumes, networking
2. **Security** — Non-root users, minimal images, scanning
3. **Optimization** — Multi-stage builds, layer caching
4. **Orchestration basics** — Docker Compose, Kubernetes awareness
5. **AI integration** — Running LLMs locally, GPU containers

### Interview Tip

When asked about Docker:
1. Explain the concept simply
2. Give a real-world example
3. Mention trade-offs or gotchas
4. Connect to modern practices (K8s, cloud, AI workloads)

---

## Quick Reference

```bash
# Build image
docker build -t myapp:1.0 .

# Run with port and volume
docker run -d -p 8080:8080 -v data:/app/data myapp:1.0

# Enter running container
docker exec -it <container> /bin/sh

# View resource usage
docker stats

# Clean up
docker system prune -a

# Compose commands
docker compose up -d
docker compose down -v
docker compose logs -f
```

---

## Summary

| Level | Focus Areas |
|-------|-------------|
| Standard | Images, containers, basic commands, Dockerfile basics |
| Intermediate | Volumes, networking, multi-stage builds, compose |
| Tricky | Layer caching, PID 1, debugging, security, production issues |
| Modern | AI/ML workloads, Kubernetes, cloud-native, dev containers |

**Remember:** Docker is not just about running containers — it's about **reproducible, portable, and consistent** application deployment.
