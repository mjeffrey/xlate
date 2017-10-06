#!/usr/bin/env bash

set -e

GEN_TEMP_DIR=$(mktemp -d /tmp/gentemp.XXXXXXXXXX)
echo TEMP DIR is ${GEN_TEMP_DIR}

JAVA_OPTS="-Dhttp.proxyHost=proxy.dll.corp -Dhttp.proxyPort=8080"

WORK_DIR=/cygdrive/d/wrk/
ROOT_TARGET_DIR=${WORK_DIR}/poc-tech-debt/atlas-web/src/main/angular/src/
I18N_TARGET_DIR=${ROOT_TARGET_DIR}/assets/i18n/
SOURCE_LANG=en
TARGET_LANGS=fr,nl

# copy the generated i18n file
cp  ${WORK_DIR}/thirdparty/swagger-codegen/localhost/assets/i18n/en.json ${GEN_TEMP_DIR}

# do the translation
pushd ${WORK_DIR}/thirdparty/xlate/target/ >/dev/null
echo MISSING GOOGLE_API_KEY
exit
export GOOGLE_API_KEY=
set -x
java ${JAVA_OPTS} -jar xlate-0.0.1-SNAPSHOT.jar $(cygpath -w ${GEN_TEMP_DIR})
popd >/dev/null

# merge the translations
for LANG in en fr nl
do
  echo ${LANG}
  jq -s '.[0] * .[1]' ${GEN_TEMP_DIR}/${LANG}.json ${I18N_TARGET_DIR}/${LANG}.json | tee ${I18N_TARGET_DIR}/${LANG}.json.new
done
