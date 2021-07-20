#include <unistd.h>
#include <strings.h>
#include <string.h>
#include <netdb.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <libc.h>

char establishConnection(int port, char *addr) {
	if (port == 0)
		return -1;
	int fd = socket(AF_INET, SOCK_STREAM, 0);
	if (fd != -1) {
		struct sockaddr_in server;
		bzero((void *) &server, sizeof(server));
		unsigned long a = inet_addr(addr);
		memcpy(&server.sin_addr, &a, sizeof(a));
		server.sin_family = AF_INET;
		server.sin_port = htons(port);
		if (connect(fd, (struct sockaddr *) &server, sizeof(server)) < 0) {
			return -1;
		}
	}
	return fd;
}