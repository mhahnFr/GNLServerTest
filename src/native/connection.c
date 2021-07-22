#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include "connection.h"


struct GNLClient_Connection
{
	struct sockaddr_in  addr;
	unsigned int        fd;
};


struct GNLClient_Connection*  GNLClient_Connection_New(const char* host, unsigned int port)
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

char* GNLClient_Connection_ReadLine(struct GNLClient_Connection* self)
{
	return NULL; /* TODO */
}

void GNLClient_Connection_WriteLine(struct GNLClient_Connection* self, const char* msg)
{
	write(self->fd, msg, strlen(msg));
}


