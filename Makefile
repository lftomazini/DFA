JAVAC = javac
JAVACFLAGS = 
SRC= phase4/Tester.java \
	phase4/REMatcher.java \
	phase4/DFA.java \
	phase4/ExpTree.java \
	phase4/Alphabet.java
CLS= $(SRC:.java=.class)

all: $(CLS)

.SUFFIXES : .class .java
.java.class :
	$(JAVAC) $(JAVACFLAGS) $<

clean: 
	rm -f phase4/*.class