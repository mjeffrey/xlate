#!/usr/bin/env bash

set -e


OPTIONS=a:g:s:x:
LONGOPTIONS=appDirectory:,genDir:,swagger:,xlateDir:

# -temporarily store output to be able to check for errors
# -activate advanced mode getopt quoting e.g. via “--options”
# -pass arguments only via   -- "$@"   to separate them correctly
PARSED=$(getopt --options=$OPTIONS --longoptions=$LONGOPTIONS --name "$0" -- "$@")
if [[ $? -ne 0 ]]; then
    # e.g. $? == 1
    #  then getopt has complained about wrong arguments to stdout
    exit 2
fi
# use eval with "$PARSED" to properly handle the quoting
eval set -- "$PARSED"

# now enjoy the options in order and nicely split until we see --
while true; do
    case "$1" in
        -a|--appDir)
            appDir="$2"
            shift 2
            ;;
        -g|--genDir)
            genDir="$2"
            shift 2
            ;;
        -s|--swagger)
            swagger="$2"
            shift 2
            ;;
        -x|--xlateDir)
            xlateDir="$2"
            shift 2
            ;;
        --)
            shift
            break
            ;;
        *)
            echo "Programming error"
            exit 3
            ;;
    esac
done
set -x
source ./api-keys.sh

# handle non-option arguments
if [[ $# -ne 0 ]]; then
    echo "$0: Unexpected argument."
    exit 4
fi

echo "appDir: '$appDir', genDir: '$genDir', swagger: '$swagger',  xlateDir: '$xlateDir'"


#GEN_TEMP_DIR=$(mktemp -d /tmp/gentemp.XXXXXXXXXX)
GEN_TEMP_DIR=~/tmp/generated/
echo TEMP DIR is ${GEN_TEMP_DIR}

JAVA_OPTS="-Dhttp.proxyHost=proxy.dll.corp -Dhttp.proxyPort=8080"


I18N_TARGET_DIR=${appDir}/assets/i18n/

SOURCE_LANG=en
TARGET_LANGS=fr,nl

# copy the generated i18n file
#cp  ${WORK_DIR}/thirdparty/swagger-codegen/localhost/assets/i18n/en.json ${GEN_TEMP_DIR}
#cp ${genDir}/assets/i18n/en.json ${GEN_TEMP_DIR}

# do the translation
#pushd ${WORK_DIR}/thirdparty/xlate/target/ >/dev/null
pushd ${xlateDir} >/dev/null
#java ${JAVA_OPTS} -jar xlate-0.0.1-SNAPSHOT.jar $(cygpath -w ${GEN_TEMP_DIR})
java ${JAVA_OPTS} -jar xlate-0.0.1-SNAPSHOT.jar ${GEN_TEMP_DIR}
popd >/dev/null

# merge the translations
for LANG in en fr nl
do
  echo ${LANG}
  jq -s '.[0] * .[1]' ${GEN_TEMP_DIR}/${LANG}.json ${I18N_TARGET_DIR}/${LANG}.json > ${I18N_TARGET_DIR}/${LANG}.json.new
done
