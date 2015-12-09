JAVAC = javac
JAVACFLAGS = 
SRC= phase5/Tester.java \
	phase5/REMatcher.java \
	phase5/DFA.java \
	phase5/ExpTree.java \
	phase5/Alphabet.java
CLS= $(SRC:.java=.class)

all: $(CLS)

.SUFFIXES : .class .java
.java.class :
	$(JAVAC) $(JAVACFLAGS) $<

clean: 
	rm -f phase5/*.class