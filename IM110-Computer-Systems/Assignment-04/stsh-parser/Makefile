FLEX = flex
BISON = bison
FLEXFLAGS = -d
BISONFLAGS = -dvty -o parser.cc

CXX = g++

TARGETS = stsh-parse-test parser.cc parser.h scanner.cc

# The CFLAGS variable sets compile flags for g: 
#  -g          compile with debug information
#  -Wall       give all diagnostic warnings
#  -pedantic   require compliance with ANSI standard
#  -O0         do not optimize generated code
#  -std=c++0x  use C++ 11 features like range-based for loops
CXXFLAGS = -g -Wall -pedantic -O0 -std=c++20 -I/usr/local/include

stsh-parse-test: stsh-parse-test.o stsh-parse.o scanner.cc parser.cc stsh-readline.o string-utils.o
	$(CXX) -o stsh-parse-test stsh-parse-test.o stsh-parse.o scanner.cc parser.cc stsh-readline.o string-utils.o -ll -lreadline

parser.cc: parser.y
	$(BISON) $(BISONFLAGS) $^

parser.h: parser.cc
	if [ -f parser.hh ]; then ln -sf parser.hh parser.h || cp parser.hh parser.h; fi

scanner.cc: scanner.l parser.h
	$(FLEX) $(FLEXFLAGS) -o $@ scanner.l

stsh-parse.cc: parser.h
# Need to force make to run bison before attempting to compile stsh-parse.cc

# clean up
clean:
	rm -f $(TARGETS) *.o *~ parser.output

spartan: clean
	rm -fr *~
