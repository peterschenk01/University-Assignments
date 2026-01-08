#include "string-utils.h"

#include <cstring>
#include <string>

bool startsWith(const char* str, const char* prefix) {
  if (!str || !prefix) return false;
  size_t lenPrefix = std::strlen(prefix);
  return std::strncmp(str, prefix, lenPrefix) == 0;
}

std::string trim(std::string str)
{
  size_t start = 0;
  while (start < str.length() && std::isspace(str[start]))
  {
    ++start;
  }

  size_t end = str.length();
  while (end > start && std::isspace(str[end - 1]))
  {
    --end;
  }

  return str.substr(start, end - start);
}