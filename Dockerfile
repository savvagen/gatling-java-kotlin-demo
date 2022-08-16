FROM gradle:7.2-jdk11

ARG SCALA_VERSION="2.13.8"

RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
# Install Dependencies
RUN apt-get install -qq -y net-tools wget curl nano zip unzip

# Install sdkman
RUN curl -s "https://get.sdkman.io" | bash \
    && chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh" \
    && source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk version \
    && sdk install scala $SCALA_VERSION

ENTRYPOINT [""]

