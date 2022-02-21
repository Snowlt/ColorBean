#!/usr/bin/env sh
cd $(dirname $0);
java --add-opens java.desktop/javax.swing.plaf.basic=ALL-UNNAMED -jar ColorBean.jar;
exit 0;