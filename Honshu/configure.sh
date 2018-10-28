#!/bin/sh

install_dependencies() {
	UNAME=`uname -s`
		if test $UNAME=Linux ; then
			apt-get install make
			apt-get install doxygen
			apt-get install libcunit1
			apt-get install libcunit1-dev
			apt-get install libcunit1-doc
		elif test $UNAME=Darwin ; then
			brew install make
			brew install doxygen
			brew install cunit
		else
			echo "OS not handled yet.
				Please, manually install 'doxygen' and 'cunit'"
		fi
}

usage() {
	s="$(basename "$1") [-h] [-i] -- Honshu setup helper.
where:
   -h show this help text
   -i install dependencies"
   echo $s
}

if test $# -eq 0 ; then
	usage $0
fi

while test $# -gt 0; do
	case "$1" in
		-h | --help ) usage $0 ; shift ;;
		-i | --install ) install_dependencies ; shift ;;
		*) usage $0 ; shift ;;
	esac
done

