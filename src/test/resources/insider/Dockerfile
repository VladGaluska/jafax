FROM openjdk:8
WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
COPY ./build/libs/insider-1.0.4-ALPHA.jar /app/insider.jar
COPY config/ /app/config/

COPY ./insider.sh /app/insider.sh
RUN chmod +x /app/insider.sh
