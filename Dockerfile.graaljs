# Run the stand java build on openjdk
FROM maven AS build
COPY . src
WORKDIR src
RUN mvn package

# Copy the result to graaljs and test node.js+java in the same vm
FROM ghcr.io/graalvm/nodejs-community:23.0.2-jvm17-ol9-20231024 AS graal
COPY --from=build /src /app/src
WORKDIR src
RUN npm test && npm run testwithjava && npm run lint
