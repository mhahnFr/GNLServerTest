#ifndef GNLCLIENT_CONNECTION_H
#define GNLCLIENT_CONNECTION_H

struct GNLClient_Connection;


struct GNLClient_Connection*  GNLClient_Connection_New(const char*, unsigned int);
void                          GNLClient_Connection_Delete(struct GNLClient_Connection*);
char*                         GNLClient_Connection_ReadLine(struct GNLClient_Connection*);
void                          GNLClient_Connection_WriteLine(struct GNLClient_Connection*, const char*);

#endif

