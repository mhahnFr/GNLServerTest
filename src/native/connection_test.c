#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <libc.h>
#include <stdbool.h>

#include "connection.h"
#include "utils.h"

/* Test file components */
#define PATH       "/testData/"
#define FILE_NAME  "test"
#define ENDING     ".txt"
#define TEST_COUNT 9

/* Colors */
#define RED   "\x1b[31m"
#define GREEN "\x1b[32m"
#define CLEAR "\x1b[0m"

void GNLClient_Connection_Test(struct GNLClient_Connection* this)
{
	char* a;
	char* path = GNLClient_Connection_CreatePath(&a);
	for (*a = '0' + 1; *a <= TEST_COUNT + '0'; (*a)++)
	{
		GNLClient_Connection_TestNo(this, path, *a - '0');
	}
}

void GNLClient_Connection_TestNo(struct GNLClient_Connection* this, char* file, int no)
{
	char* gnl = GNLClient_Connection_ReadLine(this);
	char* ogl = NULL;

	FILE* tf = fopen(file, "r");
	size_t ignoreIt = 1;
	ssize_t count = getline(&ogl, &ignoreIt, tf);
	bool succeeded = false;
	while (gnl != NULL && count != -1)
	{
		GNLClient_Connection_EditSystemLine(&ogl);
		if (strcmp(gnl, ogl) == 0)
			succeeded = true;
		else
			succeeded = false;
		free(gnl);
		free(ogl);
		ogl = NULL;
		gnl = GNLClient_Connection_ReadLine(this);
		count = getline(&ogl, &ignoreIt, tf);
	}
	fclose(tf);
	GNLClient_Connection_PrintResult(no, succeeded);
}

char* GNLClient_Connection_CreatePath(char** index)
{
	char* path = PATH FILE_NAME "0" ENDING;
	char* buf = malloc(MAXPATHLEN);
	char* pwd = getcwd(buf, MAXPATHLEN);
	char* aPath = stringJoiner(pwd, path);
	*index = aPath + strlen(pwd) + strlen(PATH) + strlen(FILE_NAME);
	free(buf);
	return aPath;
}

void GNLClient_Connection_EditSystemLine(char** line)
{
	int i = strlen(*line);
	if ((*line)[i] == '\0' && (*line)[i - 1] != '\n') {
		char* n = strdup(*line);
		free(*line);
		*line = calloc(i + 1, 1);
		strcpy(*line, n);
		(*line)[i] = '\n';
		(*line)[i + 1] = '\0';
	}
}

void GNLClient_Connection_PrintResult(int no, bool success)
{
	printf("%s[%d]: %s\n" CLEAR, success ? GREEN : RED, no, success ? "OK" : "KO");
}