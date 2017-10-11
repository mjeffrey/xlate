#!/usr/bin/env bash

source ./api-keys.sh
source ./default-paths.sh

appDir=/cygdrive/d/wrk/poc-tech-debt/atlas-web/src/main/angular
genOutDir=/cygdrive/d/wrk/gen
xlateJar=/cygdrive/d/wrk/thirdparty/xlate/target/xlate-0.0.1-SNAPSHOT.jar
codegenJar=/cygdrive/d/wrk/thirdparty/swagger-codegen/modules/swagger-codegen-cli/target/swagger-codegen-cli.jar
swaggerResource=http://alakazam.dll.corp:9011/v2/api-docs
componentName=driver

LIGHT_BLUE='\033[0;34m'
NC='\033[0m'

function toFile() {
  case "$(uname -s)" in
    CYGWIN*)    cygpath -w ${1};;
    *)          echo $1;;
esac
}

read -e -p $'\e[34mApplication directory:\e[0m ' -i "${appDir}" appDir
read -e -p $'\e[34mSwagger codegen jar:\e[0m   ' -i "${codegenJar}" codegenJar
#read -e -p $'\e[34mTranslations jar:\e[0m      ' -i "${xlateJar}" xlateJar
read -e -p $'\e[34mSwagger resource:\e[0m      ' -i "${swaggerResource}" swaggerResource
read -e -p $'\e[34mAngular Component:\e[0m     ' -i "${componentName}" componentName

rm -rf ${genOutDir}
mkdir -p ${genOutDir}
java -jar `toFile $codegenJar` generate -l typescript-angular -i ${swaggerResource} -o `toFile ${genOutDir}`

read -e -p $'\e[34mPress return to continue with copy to app:\e[0m ' -i "" dummy

if [[ ! -d ${appDir}/src/app/driver/ ]]
then
  pushd $appDir  >/dev/null
  ng generate component ${componentName}
  popd >/dev/null
fi

cp -v ${genOutDir}/model/${componentName}*.* ${appDir}/src/app/driver/
cp -v ${genOutDir}/model/calculation-settings.ts ${appDir}/src/app/driver/

exit

XLATE_JAVA_OPTS="-Dhttp.proxyHost=proxy.dll.corp -Dhttp.proxyPort=8080"
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
