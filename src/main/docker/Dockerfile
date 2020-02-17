FROM openjdk:8-jre-alpine
ENV TZ=Europe/Brussels
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh jhipster
WORKDIR /home/jhipster

# https://candrews.integralblue.com/2019/06/trusting-aws-rds-certificates-in-docker-and-beanstalk/
RUN curl "https://s3-us-gov-west-1.amazonaws.com/rds-downloads/rds-combined-ca-us-gov-bundle.pem" --output /etc/pki/ca-trust/source/anchors/rds-combined-ca-us-gov-bundle.pem \
&amp;&amp; curl "https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem" --output /etc/pki/ca-trust/source/anchors/rds-combined-ca-bundle.pem \
&amp;&amp; update-ca-trust \
&amp;&amp; update-ca-trust force-enable

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh
USER jhipster

ADD *.war app.war

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

