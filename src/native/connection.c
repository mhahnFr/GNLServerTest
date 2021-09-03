#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include "connection.h"
#include "get_next_line.h"

struct GNLClient_Connection
{
	struct sockaddr_in  addr;
	int                 fd;
};

struct GNLClient_Connection*  GNLClient_Connection_New(const char* host, int port)
{
	struct GNLClient_Connection*  connection;

	connection     = malloc(sizeof(struct GNLClient_Connection));

	if (connection == NULL) return NULL;

	connection->fd = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (connection->fd == -1) {
		free(connection);
		return NULL;
	}

	bzero(&connection->addr, sizeof(connection->addr));
	connection->addr.sin_addr.s_addr = inet_addr(host);
	connection->addr.sin_family      = AF_INET;
	connection->addr.sin_port        = htons(port);

	if (connect(connection->fd, (struct sockaddr*) &connection->addr, sizeof(connection->addr))) {
		GNLClient_Connection_Delete(connection);
		return NULL;
	}

	return connection;
}

void GNLClient_Connection_Delete(struct GNLClient_Connection* self)
{
	if (self == NULL) return;

	close(self->fd);
	free(self);
}

char* GNLClient_Connection_SystemReadLine(struct GNLClient_Connection* self)
{
	char* ogl = NULL;
	size_t ignoreIt = 1;
	ssize_t count = getline(&ogl, &ignoreIt, fdopen(self->fd, "r"));

	if (count == -1) return NULL;

	return ogl;
}

char* GNLClient_Connection_ReadLine(struct GNLClient_Connection* self)
{
	return get_next_line(self->fd);
}

void GNLClient_Connection_WriteLine(struct GNLClient_Connection* self, const char* msg)
{
	if (self == NULL || msg == NULL) return;

	write(self->fd, msg, strlen(msg));
}