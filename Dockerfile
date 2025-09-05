FROM gradle:8-jdk17 as build-base

USER gradle
RUN mkdir /home/gradle/project
WORKDIR /home/gradle/project

ADD ./app/build.gradle .
ADD ./app/src ./src

FROM build-base as development-build

RUN gradle shadowJar

FROM amazoncorretto:17-al2-jdk as base

# VS Code dev containers apparently needs tar & gzip to install the VS Code Server in the container
#RUN yum -y install tar gzip && yum -y clean all  && rm -rf /var/cache

RUN mkdir /opt/app
COPY --from=development-build /home/gradle/project/build/libs/project-all.jar /opt/app
WORKDIR /opt/app

FROM base as development

ENTRYPOINT ["java", "-cp", "project-all.jar", "com.trumedia.nfl.App"]
# Default to these arguments if none are provided on the command line
CMD ["/input/input-2.csv", "/output/output-2.json"]
