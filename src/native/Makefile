NAME = ./client.exe

SOURCES = \
		  connection.c \
		  main.c
OBJECTS = $(patsubst %.c,%.o,$(SOURCES))

all: $(NAME)


%.o: %.c
	$(CC) -c -o $@ $<

$(NAME): $(OBJECTS)
	$(CC) $(OBJECTS) -o $(NAME)

clean:
	- $(RM) $(OBJECTS)
	- $(RM) $(NAME)

.phony: run
run: $(NAME)
	$(NAME) "127.0.0.1" 8888

