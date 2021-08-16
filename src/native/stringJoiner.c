#include <stdlib.h>
#include <string.h>

#include "utils.h"

char	*stringJoiner(char const *s1, char const *s2)
{
	char	*ret;
	size_t	len1;
	size_t	len2;

	if (s1 == NULL || s2 == NULL)
		return (NULL);
	len1 = strlen(s1);
	len2 = strlen(s2);
	ret = malloc(len1 + len2 + 1);
	if (ret != NULL)
	{
		memcpy(ret, s1, len1);
		memcpy(&ret[len1], s2, len2);
		ret[len1 + len2] = '\0';
	}
	return (ret);
}
