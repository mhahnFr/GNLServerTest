/*
 * Edit this path to your get_next_line.h.
 */
#include "../../../get_next_line/get_next_line.h"
#include "gnl-server-test.h"
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
	if (argc == 3) {
		int fd = establishConnection(atoi(argv[1]), argv[2]);
		printf("%d\n", fd);
		if (fd != -1) {
			char *line;
			while (1) {
				line = get_next_line(fd);
				if (line != NULL) {
					printf("%s", line);
				}
			}
		}
	} else {
		printf("Provide a number to asign a port and an IP address.\n");
	}
}