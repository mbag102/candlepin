#!/bin/bash -x

# compares a reference db (a deployed postgresql by default) to
# the hibernate annotations

CP_CLASSPATH=$(buildr -s candlepin:cli_classpath)

# liquibase hibernate ext if not bundled
# from: http://mvnrepository.com/artifact/org.liquibase.ext/liquibase-hibernate
#       https://github.com/liquibase/liquibase-hibernate
LH="${M2_REPO}/org/liquibase/ext/liquibase-hibernate/2.0.0/liquibase-hibernate-2.0.0.jar"

# our classes
CP_CLASSES="target/classes/"
CLASSPATH="${CP_CLASSES}:${CP_CLASSPATH}:${LH}"

DB1_USERNAME="candlepin"
DB1_URL="hibernate:/hibernate.cfg.xml"
#DB1_URL="persistence:persistence.xml"

REF_DRIVER="org.postgresql.Driver"
REF_USERNAME="candlepin"
REF_URL="jdbc:postgresql:candlepin"

CHANGELOG="changelog-all.xml"

# script needs a hibernate.cfg.xml, so copy what
# appears to be a wrong one into the class path
cp hibernate.cfg.xml "${CP_CLASSES}/"


liquibase  --logLevel=debug \
           --changeLogFile="${CHANGELOG}" \
           --url="${DB1_URL}" \
           --referenceDriver="${REF_DRIVER}" \
           --referenceUrl="${REF_URL}" \
           --referenceUsername="${REF_USERNAME}" \
           --classpath="${CLASSPATH}" \
           diff

           #--username="${DB1_USERNAME}" \
#--changeLogFile=src/main/resources/db/changelog/changelog-create.xml \
