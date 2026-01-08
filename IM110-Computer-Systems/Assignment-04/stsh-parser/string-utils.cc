#include "string-utils.h"  // Replace with your actual header file name
#include <cctype> // for std::isspace

void trim(std::string& str) {
    size_t start = 0;
    size_t end = str.length();

    // Find the first non-space character
    while (start < end && std::isspace(static_cast<unsigned char>(str[start]))) {
        ++start;
    }

    // Find the last non-space character
    while (end > start && std::isspace(static_cast<unsigned char>(str[end - 1]))) {
        --end;
    }

    // Create the trimmed string (or assign substring back)
    str = str.substr(start, end - start);
}

